package io.forus.me

import android.os.Bundle
import android.support.design.widget.TabLayout
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.view.KeyEvent
import android.widget.ImageView
import android.widget.Toast
import io.forus.me.R
import io.forus.me.entities.base.EthereumItem
import io.forus.me.views.wallet.TitledFragmentPagerAdapter
import io.forus.me.views.wallet.WalletItemDetailFragment
import io.forus.me.views.wallet.WalletItemDetailPager

/**
 * Created by martijn.doornik on 30/03/2018.
 */
class WalletItemActivity : AppCompatActivity() {
    companion object {
        val WALLET_ITEM_KEY = "walletItem"
    }

    lateinit var pager: WalletItemDetailPager
    lateinit var pagerAdapter: TitledFragmentPagerAdapter

    fun goBack() {
        finish()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        try {
            if (!intent.hasExtra(WALLET_ITEM_KEY)) {
                throw NullPointerException()
            }
            val item = EthereumItem.fromString(intent.getStringExtra(WALLET_ITEM_KEY)) ?: throw NullPointerException()
            setContentView(R.layout.wallet_item_fragment)

            supportActionBar!!.title = item.name
            supportActionBar!!.setDisplayHomeAsUpEnabled(true)
            supportActionBar!!.setDisplayShowHomeEnabled(true)

            val navigation: TabLayout = findViewById(R.id.navigation)
            pager = findViewById(R.id.pager)
            pagerAdapter = TitledFragmentPagerAdapter(supportFragmentManager, listOf(
                WalletItemDetailFragment().also { it.title = resources.getString(R.string.overview) }.also { it.walletItem = item }
            ))
            pager.adapter = pagerAdapter
            navigation.setupWithViewPager(pager)

        } catch (e: NullPointerException) {
            setResult(1)
            finish()
        }
    }




}