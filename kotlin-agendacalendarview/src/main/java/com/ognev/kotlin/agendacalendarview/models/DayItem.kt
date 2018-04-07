package com.ognev.kotlin.agendacalendarview.models

import android.content.Context
import com.ognev.kotlin.agendacalendarview.R
import com.ognev.kotlin.agendacalendarview.utils.DateHelper
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

/**
 * Day model class.
 */
class DayItem(override var date: Date, override var value: Int,
    override var isToday: Boolean, override var month: String) : IDayItem {

    override var eventsCount: Int = 0
    override var isFirstDayOfTheMonth: Boolean = false
    override var isSelected: Boolean = false
    private var hasEvents: Boolean = false

    override fun setHasEvents(hasEvents: Boolean) {
        this.hasEvents = hasEvents
    }

    override fun hasEvents(): Boolean {
        return hasEvents
    }

    override
    fun toString(): String {
        return "DayItem{"
            .plus("Date='")
            .plus(date.toString())
            .plus(", value=")
            .plus(value)
            .plus('}')
    }

    companion object {

        fun buildDayItemFromCal(calendar: Calendar, context: Context, locale: Locale): DayItem {
            val date = calendar.time
            val isToday = DateHelper.sameDate(calendar, Calendar.getInstance())
            val value = calendar.get(Calendar.DAY_OF_MONTH)
            val monthNameFormat = SimpleDateFormat(context.getString(R.string.month_half_name_format), locale)
            val dayItem = DayItem(date, value, isToday, monthNameFormat.format(date))
            if (value == 1) {
                dayItem.isFirstDayOfTheMonth = true
            }
            dayItem.isToday = isToday
            return dayItem
        }
    }
}
