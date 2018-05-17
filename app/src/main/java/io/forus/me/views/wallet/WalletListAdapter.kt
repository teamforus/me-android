package io.forus.me.views.wallet

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.os.AsyncTask
import android.support.v4.content.ContextCompat
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import io.forus.me.R
import io.forus.me.entities.Token
import io.forus.me.entities.base.WalletItem
import io.forus.me.views.base.LiveDataAdapter
import io.forus.me.helpers.QrHelper
import io.forus.me.services.base.EthereumItemService

/**
 * Created by martijn.doornik on 22/03/2018.
 */
class WalletListAdapter<T : WalletItem>(private val resourceLayout: Int, private val service: EthereumItemService<T>, private val listener: WalletPagerFragment<T>) : LiveDataAdapter<T, WalletListAdapter.WalletViewHolder>(listener, service) {

    override fun getItemCount(): Int {
        return items.size
    }
    @SuppressLint("StaticFieldLeak")
    override fun onBindViewHolder(holder: WalletViewHolder, position: Int) {
        if (holder.nameView != null) {
            holder.nameView.text = items[position].name
        }
        if (holder.qrView != null) {
            val onColor = ContextCompat.getColor(holder.itemView.context, R.color.black)
            val offColor = ContextCompat.getColor(holder.itemView.context, R.color.transparent)
            val task = object : AsyncTask<Any?, Any?, Bitmap?>() {
                override fun doInBackground(vararg params: Any?): Bitmap? {
                    return if (position < itemCount) {
                        QrHelper.getQrBitmap(
                                listener.requireContext(),
                                if (items[position] is Token && (items[position] as Token).isEther) "Ether" // TODO Better name
                                else items[position].address,
                                QrHelper.Sizes.SMALL, onColor, offColor)
                    } else {
                        null
                    }
                }

                override fun onPostExecute(result: Bitmap?) {
                    super.onPostExecute(result)
                    if (result != null) {
                        holder.qrView.setImageBitmap(result)
                    }
                }
            }
            task.execute()
        }
        if (holder.valueView != null) {
            if (!items[position].hasError) {
                holder.valueView.text = items[position].amount
            } else {
                holder.valueView.text = holder.valueView.context.getText(R.string.sync_error)
                holder.valueView.setTextColor(ContextCompat.getColor(holder.valueView.context, R.color.error))
            }
        }
        if (items[position].hasError && holder.errorView != null) {
            holder.errorView.visibility = View.VISIBLE
        }
        holder.itemView.setOnClickListener({
            listener.onItemSelect(items[position])
        })
        val task = object : AsyncTask<Any?, Any?, Boolean>() {
            override fun doInBackground(vararg params: Any?): Boolean {
                return if (position < itemCount) {
                    items[position].sync()
                } else { false }
            }

            override fun onPostExecute(result: Boolean) {
                super.onPostExecute(result)
                if (result) {
                    service.update(items[position])
                    notifyItemChanged(position)
                }
            }

        }
        task.execute()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WalletViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(resourceLayout, parent,false)
        return WalletViewHolder(view)
    }

    class WalletViewHolder(view:View) : RecyclerView.ViewHolder(view) {
        val qrView: ImageView? = view.findViewById(R.id.qrView)
        val nameView: TextView? = view.findViewById(R.id.name_view)
        val valueView: TextView? = view.findViewById(R.id.value_view)
        val errorView: ImageView? = view.findViewById(R.id.errorIcon)
    }
}