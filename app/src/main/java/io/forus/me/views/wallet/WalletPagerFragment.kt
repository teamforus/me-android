package io.forus.me.views.wallet

import android.content.Intent
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import io.forus.me.R
import io.forus.me.WalletItemActivity
import io.forus.me.entities.base.WalletItem
import io.forus.me.services.base.EthereumItemService
import io.forus.me.views.base.TitledFragment

abstract class WalletPagerFragment<T: WalletItem>(service: EthereumItemService<T>) : TitledFragment() {

    var adapter = WalletListAdapter(this.getLayoutResource(), service, this)

    abstract fun getLayoutResource(): Int

    fun notifyDataSetChanged() {
        this.adapter.notifyDataSetChanged()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.wallet_items_fragment, container, false)
        val listView: RecyclerView = view.findViewById(R.id.wallet_list)
        listView.layoutManager = LinearLayoutManager(context)
        listView.adapter = adapter
        return view
    }

    fun onItemSelect(selected: T) {
        val intent = Intent(this.context, WalletItemActivity::class.java)
        intent.putExtra(WalletItemActivity.RequestCode.WALLET_ITEM_KEY, selected.toJson().toString())
        startActivity(intent)
    }
}