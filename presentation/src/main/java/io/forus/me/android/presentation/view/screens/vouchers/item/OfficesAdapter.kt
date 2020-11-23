package io.forus.me.android.presentation.view.screens.vouchers.item

import android.content.Context
import android.content.Intent
import android.support.v4.view.PagerAdapter
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import io.forus.me.android.presentation.R
import io.forus.me.android.presentation.models.vouchers.Office

class OfficesAdapter(private val items: List<Office>, private val context: Context) : PagerAdapter() {
    private var layoutInflater: LayoutInflater? = null
    override fun getCount(): Int {
        return items.size
    }

    override fun isViewFromObject(view: View, obj: Any): Boolean {
        return view == obj
    }

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        layoutInflater = LayoutInflater.from(context)
        val view = layoutInflater!!.inflate(R.layout.item_office, container, false)
        val addressTV: TextView = view.findViewById(R.id.addressTV)
        val showMapTV: TextView = view.findViewById(R.id.showMapTV)
        val phoneTV: TextView = view.findViewById(R.id.phoneTV)
        val timeTV: TextView = view.findViewById(R.id.timeTV)


        val office = items[position]

        addressTV.text = office.address
        phoneTV.text = office.phone
        //timeTV.text = office.organization.

        /*title.setText(item[position].getTitle())
        desc.setText(item[position].getDesc())
        view.setOnClickListener {
            val intent = Intent(context, DetailActivity::class.java)
            intent.putExtra("param", item[position].getTitle())
            context.startActivity(intent)
            // finish();
        }*/
        container.addView(view, 0)
        return view
    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        container.removeView(`object` as View)
    }
}