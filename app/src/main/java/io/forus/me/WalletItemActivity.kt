package io.forus.me

import android.content.Intent
import android.os.Bundle
import android.support.design.widget.TabLayout
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.view.View
import android.widget.Toast
import io.forus.me.entities.Service
import io.forus.me.entities.Token
import io.forus.me.entities.base.EthereumItem
import io.forus.me.entities.base.WalletItem
import io.forus.me.helpers.JsonHelper
import io.forus.me.helpers.ThreadHelper
import io.forus.me.services.IdentityService
import io.forus.me.services.ServiceService
import io.forus.me.services.TokenService
import io.forus.me.views.wallet.*

/**
 * Created by martijn.doornik on 30/03/2018.
 */
class WalletItemActivity : AppCompatActivity() {
    companion object {
        val CANCEL_RESULT: Int = 4
        val OK_RESULT: Int = 3

        // Because we don't want another "ServiceService"
        val RETRIEVE_REQUEST: Int = 2
        val SEND_REQUEST: Int = 1

        val ADDRESS_KEY = "address"
        val WALLET_ITEM_KEY = "walletItem"
    }

    lateinit var pager: WalletItemDetailPager
    lateinit var pagerAdapter: TitledFragmentPagerAdapter

    fun goBack(view:View?) {
        finish()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            RETRIEVE_REQUEST -> {
                when (resultCode) {
                    OK_RESULT -> {}
                    CANCEL_RESULT -> {}
                }
            }
            SEND_REQUEST -> {
                when (resultCode) {
                    OK_RESULT -> {
                        this.pager.setCurrentItem(WalletItemDetailPager.OVERVIEW_PAGE, false)
                        val address = intent!!.extras.getString(ADDRESS_KEY)
                        val walletItem = JsonHelper.toWalletItem(intent!!.extras.getString(WALLET_ITEM_KEY))
                        if (walletItem != null) {
                            // TODO Handle scanned result
                            Toast.makeText(this.baseContext, "//TODO Success!", Toast.LENGTH_LONG).show()
                        }
                    }
                    CANCEL_RESULT -> {
                        this.onSendCanceled()
                    }
                }
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        try {
            if (!intent.hasExtra(WALLET_ITEM_KEY)) {
                throw NullPointerException()
            }
            val item = EthereumItem.fromString(intent.getStringExtra(WALLET_ITEM_KEY)) as WalletItem? ?: throw NullPointerException()
            ThreadHelper.dispense(ThreadHelper.MAIN_THREAD).postTask(Runnable {
                if (item is Token) {
                    val compare = TokenService.getTokenByAddressByIdentity(item.address, IdentityService.currentAddress)
                    if (compare != null) {
                        item.value = compare.value
                    }
                } else if (item is Service) {
                    val compare = ServiceService.getServiceByAddressByIdentity(item.address, IdentityService.currentAddress)
                    if (compare != null) {
                        item.value = compare.value
                    }
                }
            })
            setContentView(R.layout.activity_wallet_item)

            val toolbar: Toolbar = findViewById(R.id.toolbar)
            toolbar.title = item.name

            val navigation: TabLayout = findViewById(R.id.navigation)
            pager = findViewById(R.id.pager)
            pagerAdapter = TitledFragmentPagerAdapter(supportFragmentManager, listOf(
                    WalletItemRequestFragment().also {it.title = resources.getString(R.string.request)}.also { it.walletItem = item },
                    WalletItemDetailFragment().also { it.title = resources.getString(R.string.overview) }.also { it.walletItem = item },
                    WalletItemSendFragment().also { it.title = resources.getString(R.string.send) }.also { it.walletItem = item }
            ))
            pager.adapter = pagerAdapter
            pager.setCurrentItem(1, false)
            navigation.setupWithViewPager(pager)

        } catch (e: NullPointerException) {
            setResult(1)
            finish()
        }
    }

    fun onSendCanceled() {
        (this.pager.getChildAt(WalletItemDetailPager.SEND_PAGE) as WalletItemSendFragment).onCancel()

    }




}