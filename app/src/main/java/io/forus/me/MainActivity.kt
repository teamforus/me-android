package io.forus.me

import android.app.Dialog
import android.arch.lifecycle.Observer
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.support.design.widget.Snackbar
import android.support.design.widget.TabLayout
import android.support.v7.app.AppCompatActivity
import io.forus.me.entities.Asset
import io.forus.me.entities.Record
import io.forus.me.entities.Service
import io.forus.me.entities.Token
import io.forus.me.entities.base.EthereumItem
import io.forus.me.helpers.ThreadHelper
import io.forus.me.services.*
import io.forus.me.views.main.MainPager
import io.forus.me.views.main.MainPagerAdapter
import io.forus.me.views.me.MeFragment
import io.forus.me.views.record.RecordsFragment
import io.forus.me.views.wallet.WalletFragment
import io.forus.me.web3.TokenContract

import kotlinx.android.synthetic.main.alert_add_qr.*
import java.util.concurrent.Callable


class MainActivity : AppCompatActivity(), MeFragment.QrListener {

    private lateinit var mainPager: MainPager
    private lateinit var meFragment: MeFragment
    private lateinit var navigation: TabLayout
    private lateinit var walletFragment: WalletFragment

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == MainActivity.RequestCode.CREATE_ACCOUNT) {
            val intent = Intent(this, CreateIdentityActivity::class.java)
            startActivityForResult(intent, RequestCode.CREATE_IDENTITY)
        } else if (requestCode == RequestCode.CREATE_IDENTITY) {
            if (data != null && data.getBooleanExtra(CreateIdentityActivity.Result.IS_FIRST, false)) {
                val intent = Intent(this, AssignDelegatesActivity::class.java)
                startActivityForResult(intent, RequestCode.ASSIGN_DELEGATES)
            }
        } else if (requestCode == RequestCode.ASSIGN_DELEGATES) {
            initMainView()
        }
    }

    private fun initMainView() {
        setContentView(R.layout.activity_main)
        this.mainPager = findViewById(R.id.main_pager)
        //this.mainPager.setPageTransformer(false, MainTransformer())
        //this.mainPager.addOnPageChangeListener(this)

        meFragment = MeFragment().with(this).also { it.title = resources.getString(R.string.navigation_qr) }
        walletFragment = WalletFragment().also { it.title = resources.getString(R.string.navigation_wallet) }

        val fragments = listOf(
                walletFragment,
                meFragment,
                RecordsFragment().also { it.title = resources.getString(R.string.navigation_records) }
        )

        val adapter = MainPagerAdapter(supportFragmentManager, fragments)
        mainPager.adapter = adapter
        navigation = findViewById(R.id.navigation)
        navigation.setupWithViewPager(mainPager)
        for (i in 0 until navigation.tabCount) {
            when (i) {
                MainPager.ME_VIEW -> navigation.getTabAt(i)!!.setIcon(R.drawable.ic_qr_code)
                MainPager.WALLET_VIEW -> navigation.getTabAt(i)!!.setIcon(R.drawable.ic_account_balance_wallet_black_24dp)
                MainPager.RECORDS_VIEW -> navigation.getTabAt(i)!!.setIcon(R.drawable.ic_mode_edit_black_24dp)
            }
            //navigation.getTabAt(i)!!.icon!!.setTint(resources.getColor(R.color.colorPrimary, theme))
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        var delay = 2200L
        if (savedInstanceState != null) {
            delay = 0L
        } else {
            setContentView(R.layout.welcome)
        }
        // Show welcome screen for 1 second
        Handler().postDelayed({
            // Require login to continue
            Web3Service.initialize(this)
            if (requireLogin()) {
                // If logged in, show main view; otherwise, this is done in onActivityResult
                initMainView()
            }
        }, delay)
    }

    override fun onQrError(code: Int) {
        when (code) {
            MeFragment.QrListener.ErrorCode.INVALID_OBJECT -> {
                Snackbar.make(this.mainPager, "QR Code is ongeldig.", Snackbar.LENGTH_LONG).show()
            }
        }
        // Make sure the error doesn't happen over and over again
        Thread.sleep(100)
        meFragment.resumeScanner()
    }

    /**
     * Handle the result of a successful scan in the MeFragment class
     */
    override fun onQrResult(result: EthereumItem) {
        // TODO Search for duplicates
        val alert = Dialog(this)//LayoutDialog().with(R.layout.alert_add_qr)
        alert.setContentView(R.layout.alert_add_qr)
        alert.nameToolbar.title = result.name
        alert.alertAddress.setAddress(result.address)
        alert.alertBody.text = String.format(alert.alertBody.text.toString(), result.name)
        alert.alertPositiveButton.setOnClickListener {
            if (result is Record) {
                RecordService.addRecord(result)
                this.mainPager.currentItem = MainPager.RECORDS_VIEW
            } else {
                when (result) {
                    is Token -> {
                        walletFragment.showTokens()
                        TokenService.addToken(result)
                    }
                    is Service -> {
                        walletFragment.showServices()
                        ServiceService.addService(result)
                    }
                    is Asset -> {
                        walletFragment.showAssets()
                        AssetService.addAsset(result)
                    }
                }
                this.mainPager.currentItem = MainPager.WALLET_VIEW
            }
            alert.dismiss()
            meFragment.resumeScanner()
        }
        alert.alertCancelButton.setOnClickListener {
            alert.dismiss()
            meFragment.resumeScanner()
        }
        alert.show()
    }

    private fun requireLogin(): Boolean {
        DatabaseService.prepare(this)
        if (Web3Service.account == null) {
            val intent = Intent(this, CreateAccountActivity::class.java)
            startActivityForResult(intent, MainActivity.RequestCode.CREATE_ACCOUNT)
            return false
        }
        val identity = ThreadHelper.await(Callable {
            IdentityService.loadCurrentIdentity(this)
        })
        if (identity == null) {
            val intent = Intent(this, CreateIdentityActivity::class.java)
            startActivityForResult(intent, MainActivity.RequestCode.CREATE_IDENTITY)
            return false
        }
        return true
    }

    private class RequestCode {
        companion object {
            val CREATE_ACCOUNT: Int = 42
            val CREATE_IDENTITY: Int = 43
            val ASSIGN_DELEGATES: Int = 44
        }
    }

    class ResultCode {
        companion object {
            val OK: Int = 1
        }
    }
}
