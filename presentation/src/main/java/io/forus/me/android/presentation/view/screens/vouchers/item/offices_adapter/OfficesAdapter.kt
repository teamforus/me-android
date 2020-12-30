package io.forus.me.android.presentation.view.screens.vouchers.item.offices_adapter

import android.content.Context
import android.support.v4.view.PagerAdapter
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import io.forus.me.android.presentation.R
import io.forus.me.android.presentation.models.vouchers.Office
import io.forus.me.android.presentation.models.vouchers.Schedule

class OfficesAdapter(private val items: List<Office>, private val context: Context) : PagerAdapter() {
    private var layoutInflater: LayoutInflater? = null
    override fun getCount(): Int {
        return items.size
    }

    override fun isViewFromObject(view: View, obj: Any): Boolean {
        return view == obj
    }

    var showMapCallback : ShowMapCallback? = null

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        layoutInflater = LayoutInflater.from(context)
        val view = layoutInflater!!.inflate(R.layout.item_office, container, false)
        val addressTV: TextView = view.findViewById(R.id.addressTV)
        val showMapTV: TextView = view.findViewById(R.id.showMapTV)
        val phoneTV: TextView = view.findViewById(R.id.phoneTV)
        val timeTV: TextView = view.findViewById(R.id.timeTV)



        val office = items[position]

        showMapTV.setOnClickListener {
            if(showMapCallback != null){
                showMapCallback!!.showMap(office)
            }
        }


        addressTV.text = office.address
        phoneTV.text = office.phone

        val groupSchedulers = groupSchedulers2(office.schedulers)
        var schedulersText = ""



        groupSchedulers.map {
            schedulersText += it.scheduleText(timeTV.context)
            schedulersText += "\n"

        }

        timeTV.text = schedulersText

        container.addView(view, 0)
        return view
    }

    //Group by schedule time only any days
    fun groupSchedulers2(schedulers: List<Schedule>): List<DaysGroup> {
        val groups = mutableListOf<DaysGroup>()

        for (i in 0 until (schedulers.size)) {
            val gr = schedulers[i]

            if(gr.startTime.isNullOrEmpty() || gr.endTime.isNullOrEmpty()) {

            }else{

                var searchCompatibleGroup = false

                for(group in groups){
                    if (group.isDayCompatibleInGroup(gr)) {
                        group.addSchedule(gr)
                        searchCompatibleGroup = true
                    }
                }
                if(!searchCompatibleGroup) {
                    val group = DaysGroup()
                     groups.add(group)
                    group.addSchedule(gr)
                }
            }
        }

        return groups
    }

    //Group by schedule time only neighbor days
    fun groupSchedulers(schedulers: List<Schedule>): List<DaysGroup> {
        val groups = mutableListOf<DaysGroup>()
        var group = DaysGroup()
        groups.add(group)
        for (i in 0 until (schedulers.size)) {
            val gr = schedulers[i]

            if(gr.startTime.isNullOrEmpty() || gr.endTime.isNullOrEmpty()) {
                if(!group.isEmptyGroup()) {
                    group = DaysGroup()
                    groups.add(group)
                }
            }else{
                if (group.isDayCompatibleInGroup(gr)) {
                    group.addSchedule(gr)
                } else {
                    if(!group.isEmptyGroup()) {
                        group = DaysGroup()
                        groups.add(group)
                    }
                    group.addSchedule(gr)
                }
            }
            if(i==schedulers.size-1){
                if(group.isEmptyGroup()){
                    groups.remove(group)
                }
            }
        }

        return groups
    }

    override fun destroyItem(container: ViewGroup, position: Int, obj: Any) {
        container.removeView(obj as View)
    }




    public interface ShowMapCallback{
        fun showMap(office: Office)
    }
}