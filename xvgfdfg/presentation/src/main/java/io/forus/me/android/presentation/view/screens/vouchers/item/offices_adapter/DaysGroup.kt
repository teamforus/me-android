package io.forus.me.android.presentation.view.screens.vouchers.item.offices_adapter

import android.content.Context
import io.forus.me.android.presentation.R
import io.forus.me.android.presentation.models.vouchers.Schedule

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

        return when (day) {
            0 -> context.getString(R.string.monday)
            1 -> context.getString(R.string.tuesday)
            2 -> context.getString(R.string.wednesday)
            3 -> context.getString(R.string.thursday)
            4 -> context.getString(R.string.friday)
            5 -> context.getString(R.string.saturday)
            6 -> context.getString(R.string.sunday)
            else -> ""
        }
    }

}