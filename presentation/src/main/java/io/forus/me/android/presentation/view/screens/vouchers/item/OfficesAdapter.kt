package io.forus.me.android.presentation.view.screens.vouchers.item

import android.content.Context
import android.content.res.Resources
import android.support.v4.view.PagerAdapter
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import io.forus.me.android.presentation.R
import io.forus.me.android.presentation.models.vouchers.Office
import io.forus.me.android.presentation.models.vouchers.Schedule
import java.util.*

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

        val groupSchedulers = groupSchedulers(office.schedulers)
        var schedulersText = ""



        groupSchedulers.map {
            schedulersText += it.scheduleText(timeTV.context)
            schedulersText += "\n"

        }

        timeTV.text = schedulersText

        container.addView(view, 0)
        return view
    }


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


    class DaysGroup {

        val group = mutableListOf<Schedule>()

        fun addSchedule(schedule: Schedule) {
            group.add(schedule)
        }

        fun isDayCompatibleInGroup(schedule: Schedule): Boolean {

            if (group.isNotEmpty()) {
                if (group[0].startTime == schedule.startTime && group[0].endTime == schedule.endTime) {
                    group.add(schedule)
                    return true
                } else {
                    return false
                }
            } else {
                return true
            }
        }

        fun scheduleText(ctx: Context): String {
            return when {
                group.isEmpty() -> {
                    ""
                }
                group.size == 1 -> {
                    val day = group.first()
                    getDayOfWeekName(ctx, day.weekDay.toInt()) + ": " + day.startTime + " - " + day.endTime
                }
                else -> {
                    val firstDay = group.first()
                    val lastDay = group.last()
                    (getDayOfWeekName(ctx, firstDay.weekDay.toInt()) + " - " + getDayOfWeekName(ctx, lastDay.weekDay.toInt()) + ": "
                            + firstDay.startTime + " - " + firstDay.endTime)
                }
            }
        }

        fun isEmptyGroup():Boolean{
            return group.isEmpty()
        }

        fun getDayOfWeekName(context: Context, day: Int): String {

            when (day) {
                0 -> return context.getString(R.string.monday)
                1 -> return context.getString(R.string.tuesday)
                2 -> return context.getString(R.string.wednesday)
                3 -> return context.getString(R.string.thursday)
                4 -> return context.getString(R.string.friday)
                5 -> return context.getString(R.string.saturday)
                6 -> return context.getString(R.string.sunday)
                else -> return ""
            }
        }

    }
}