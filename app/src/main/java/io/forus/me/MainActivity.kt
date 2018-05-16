package io.forus.me

import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.support.design.widget.TabLayout
import android.support.v4.view.ViewPager
import android.support.v7.app.AppCompatActivity
import android.widget.Toast
import io.forus.me.entities.Asset
import io.forus.me.entities.Record
import io.forus.me.entities.Voucher
import io.forus.me.entities.Token
import io.forus.me.entities.base.EthereumItem
import io.forus.me.helpers.ThreadHelper
import io.forus.me.services.*
import io.forus.me.views.main.MainPager
import io.forus.me.views.main.MainPagerAdapter
import io.forus.me.views.me.MeFragment
import io.forus.me.views.record.RecordsFragment
import io.forus.me.views.wallet.WalletFragment

import kotlinx.android.synthetic.main.activity_qr_result.*
import java.util.concurrent.Callable


class MainActivity : AppCompatActivity(), MeFragment.QrListener, ViewPager.OnPageChangeListener {

    private lateinit var mainPager: MainPager
    private lateinit var meFragment: MeFragment
    private lateinit var navigation: TabLayout
    private lateinit var recordsFragment: RecordsFragment
    private lateinit var walletFragment: WalletFragment

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        // User has created a new Account
        if (requestCode == CreateAccountActivity.RequestCode.REQUEST_ACCOUNT) {
            val intent = Intent(this, CreateIdentityActivity::class.java)
            startActivityForResult(intent, CreateIdentityActivity.RequestCode.REQUEST_IDENTITY)
        // User has created a new Identity (which uses account)
        } else if (requestCode == CreateIdentityActivity.RequestCode.REQUEST_IDENTITY) {
            if (data != null && data.getBooleanExtra(CreateIdentityActivity.Result.IS_FIRST, false)) {
                val intent = Intent(this, AssignDelegatesActivity::class.java)
                startActivityForResult(intent, AssignDelegatesActivity.RequestCode.ASSIGN_DELEGATES)
            }
        } else if (requestCode == AssignDelegatesActivity.RequestCode.ASSIGN_DELEGATES) {
            initMainView()
        } else if (requestCode == QrResultActivity.RequestCodes.NEW_RESULT) {
            when (resultCode) {
                QrResultActivity.ResultCodes.NEW_ASSET -> {
                    this.mainPager.currentItem = MainPager.WALLET_VIEW
                    this.walletFragment.showAssets(true)
                }
                QrResultActivity.ResultCodes.NEW_RECORD -> {
                    this.mainPager.currentItem = MainPager.RECORDS_VIEW
                }
                QrResultActivity.ResultCodes.NEW_TOKEN -> {
                    this.mainPager.currentItem = MainPager.WALLET_VIEW
                    this.walletFragment.showTokens(true)
                }
                QrResultActivity.ResultCodes.NEW_VOUCHER -> {
                    this.mainPager.currentItem = MainPager.WALLET_VIEW
                    this.walletFragment.showVouchers(true)
                }
                QrResultActivity.ResultCodes.CANCEL -> {
                }
                QrResultActivity.ResultCodes.ERROR -> {
                    Toast.makeText(baseContext, "Fout opgetreden bij verwerken van QR code. Probeer later nog eens.", Toast.LENGTH_SHORT).show()
                }
            }
            ThreadHelper.dispense(ThreadHelper.MAIN_THREAD).postTask(Runnable {
                if (resultCode == QrResultActivity.ResultCodes.ERROR) {
                    Thread.sleep(200)
                }
                runOnUiThread { meFragment.resumeScanner() }
            })
        }
    }

    private fun initMainView() {
        val serviceIntent = Intent(this, TokenTransactionWatcher::class.java)
        this.startService(serviceIntent)
        setContentView(R.layout.activity_main)
        this.mainPager = findViewById(R.id.main_pager)
        //this.mainPager.setPageTransformer(false, MainTransformer())
        //this.mainPager.addOnPageChangeListener(this)

        meFragment = MeFragment().with(this).also { it.title = resources.getString(R.string.navigation_qr) }
        walletFragment = WalletFragment().also { it.title = resources.getString(R.string.navigation_wallet) }
        recordsFragment = RecordsFragment().also { it.title = resources.getString(R.string.navigation_records) }
        val fragments = listOf(
                walletFragment,
                meFragment,
                recordsFragment
        )

        val adapter = MainPagerAdapter(supportFragmentManager, fragments)
        mainPager.adapter = adapter
        navigation = findViewById(R.id.navigation)
        navigation.setupWithViewPager(mainPager)
        mainPager.addOnPageChangeListener(this)
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

    override fun onPageSelected(position: Int) {
        if (position != MainPager.ME_VIEW) {
            meFragment.pauseScanner()
        } else {
            meFragment.resumeScanner()
        }
    }

    override fun onPageScrollStateChanged(state: Int) {}

    override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {}

    override fun onQrResult(result: String) {
        meFragment.pauseScanner()
        val intent = Intent(this, QrResultActivity::class.java)
        intent.putExtra(QrResultActivity.RESULT, result)
        startActivityForResult(intent, QrResultActivity.RequestCodes.NEW_RESULT)
    }

    private fun requireLogin(): Boolean {
        DatabaseService.prepare(this)
        if (Web3Service.account == null) {
            val intent = Intent(this, CreateAccountActivity::class.java)
            startActivityForResult(intent, CreateAccountActivity.RequestCode.REQUEST_ACCOUNT)
            return false
        }
        val identity = ThreadHelper.await(Callable {
            IdentityService.loadCurrentIdentity(this)
        })
        if (identity == null) {
            val intent = Intent(this, CreateIdentityActivity::class.java)
            startActivityForResult(intent, CreateIdentityActivity.RequestCode.REQUEST_IDENTITY)
            return false
        }
        return true
    }
}
