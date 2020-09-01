package io.forus.me.android.presentation.view.screens.vouchers.voucher_with_actions.adapter

import android.content.Context
import android.graphics.Movie
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import io.forus.me.android.domain.models.vouchers.ProductAction
import io.forus.me.android.presentation.R



class ActionsAdapter(var items: ArrayList<ProductAction>, val callback: Callback) : RecyclerView.Adapter<ActionsAdapter.MainHolder>() {

    private val context: Context? = null
    //private val movieList: List<Movie>? = null
    private val LOADING = 0
    private val ITEM = 1
    private var isLoadingAdded = false

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int)
            = MainHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_action, parent, false))

    override fun getItemCount() = items.size

    override fun onBindViewHolder(holder: MainHolder, position: Int) {
        holder.bind(items[position])
    }

    inner class MainHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        private val nameTV = itemView.findViewById<TextView>(R.id.nameTV)



        fun bind(item: ProductAction) {
            nameTV.text = item.name

        }
    }

    interface Callback {
        fun onItemClicked(item: ProductAction)
    }

    /*fun setList(goods: List<Foo>) {

        Log.d("my","ADAPTER_goods_size = ${goods.size}")

        items.clear();
        items.addAll(goods);
        this.notifyDataSetChanged();
    }

    fun addList(goods: List<Foo>) {

        Log.d("my","ADAPTER_goods_size = ${goods.size}")

        //items.clear();
        items.addAll(goods);
        this.notifyDataSetChanged();
    }*/

   /* override fun getItemCount(): Int {
        return movieList?.size ?: 0
    }*/

    override fun getItemViewType(position: Int): Int {
        return if (position == items!!.size - 1 && isLoadingAdded) LOADING else ITEM
    }

    fun addLoadingFooter() {
        isLoadingAdded = true
       // add(Movie())
    }

    /*fun removeLoadingFooter() {
        isLoadingAdded = false
        val position = movieList!!.size - 1
        val result = getItem(position)
        if (result != null) {
            movieList.removeAt(position)
            notifyItemRemoved(position)
        }
    }*/

    fun add(movie: ProductAction) {
        items.add(movie!!)
        notifyItemInserted(items!!.size - 1)
    }

    fun addAll(moveResults: List<ProductAction>) {
        for (result in moveResults) {
            add(result)
        }
    }

    fun getItem(position: Int): ProductAction {
        return items!![position]
    }

}
