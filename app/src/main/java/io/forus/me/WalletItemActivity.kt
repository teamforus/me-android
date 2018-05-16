package io.forus.me

import android.arch.lifecycle.Observer
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.support.design.widget.TabLayout
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.view.MenuItem
import android.view.View
import android.widget.ImageView
import android.widget.PopupMenu
import android.widget.Toast
import io.forus.me.entities.Asset
import io.forus.me.entities.Token
import io.forus.me.entities.Voucher
import io.forus.me.entities.base.WalletItem
import io.forus.me.helpers.JsonHelper
import io.forus.me.helpers.ThreadHelper
import io.forus.me.services.AssetService
import io.forus.me.services.TokenService
import io.forus.me.services.VoucherService
import io.forus.me.views.wallet.*
import java.util.concurrent.Callable

/**
 * Created by martijn.doornik on 30/03/2018.
 */
class WalletItemActivity : AppCompatActivity(), PopupMenu.OnMenuItemClickListener {

    lateinit var item: WalletItem
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
                    //OK_RESULT -> {}
                    //CANCEL_RESULT -> {}
                }
            }
            SendWalletItemActivity.RequestCode.SEND_REQUEST -> {
                when (resultCode) {
                    SendWalletItemActivity.ResultCode.SUCCESS_RESULT -> {
                        this.pager.setCurrentItem(WalletItemDetailPager.OVERVIEW_PAGE, false)
                        val address = intent!!.extras.getString(SendWalletItemActivity.RequestCode.RECIPIENT)
                        val walletItem = JsonHelper.toWalletItem(intent!!.extras.getString(SendWalletItemActivity.RequestCode.TRANSFER_OBJECT))
                        if (walletItem != null) {
                            // TODO Handle scanned result
                            Toast.makeText(this.baseContext, "//TODO Success!", Toast.LENGTH_LONG).show()
                        }
                    }
                    SendWalletItemActivity.ResultCode.CANCEL_RESULT -> {
                        this.onSendCanceled()
                    }
                }
            }
        }
    }

    fun onChange(walletItem: WalletItem) {
        this.item = walletItem
        refreshToolbar()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        this.setContentView(R.layout.loading)
        try {
            if (!intent.hasExtra(RequestCode.WALLET_ITEM_KEY)) {
                throw NullPointerException()
            }
            item = JsonHelper.toWalletItem(intent.getStringExtra(RequestCode.WALLET_ITEM_KEY)) ?: throw NullPointerException()
            ThreadHelper.await(Callable{
                when (item) {
                    is Asset -> AssetService().getLiveItem(item.address).observe(this, LiveWalletItemObserver<Asset>(this))
                    is Token -> TokenService().getLiveItem(item.address).observe(this, LiveWalletItemObserver<Token>(this))
                    is Voucher -> VoucherService().getLiveItem(item.address).observe(this, LiveWalletItemObserver<Voucher>(this))
                }
            })
            setContentView(R.layout.activity_wallet_item)

            val toolbar: Toolbar = findViewById(R.id.toolbar)
            toolbar.title = item.name

            val optionsButton: ImageView = findViewById(R.id.optionsButton)
            optionsButton.setOnClickListener {
                val menu = PopupMenu(baseContext, optionsButton)
                menu.setOnMenuItemClickListener(this)
                menu.inflate(R.menu.wallet_item_options)
                menu.show()
            }

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

    override fun onMenuItemClick(item: MenuItem?): Boolean {
        when (item?.itemId) {
            R.id.sync -> {
                this.item.sync()
            }
            R.id.delete -> {
                this.setContentView(R.layout.loading)
                when(this.item) {
                    is Asset -> AssetService().delete(this.item as Asset)
                    is Token -> TokenService().delete(this.item as Token)
                    is Voucher -> VoucherService().delete(this.item as Voucher)
                }
                finish()
            }
        }
        return false
    }

    fun onSendCanceled() {
        (this.pager.getChildAt(WalletItemDetailPager.SEND_PAGE) as WalletItemSendFragment).onCancel()

    }

    private fun refreshToolbar() {
        val toolbar: Toolbar? = findViewById(R.id.toolbar)
        if (toolbar != null) {
            toolbar.title = item.name
            toolbar.subtitle = item.amount
        }

    }

    private inner class LiveWalletItemObserver<ITEM: WalletItem>(private val listener: WalletItemActivity): Observer<ITEM> {
        override fun onChanged(t: ITEM?) {
            if (t != null) {
                listener.onChange(t)
            }
        }

    }

    companion object {
        val RETRIEVE_REQUEST: Int = 2
    }

    class RequestCode {
        companion object {
            const val WALLET_ITEM_KEY = "walletItem"
        }
    }
}