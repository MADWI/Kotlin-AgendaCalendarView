package com.ognev.kotlin.agendacalendarview.calendar

import android.content.Context
import android.util.AttributeSet
import android.widget.LinearLayout
import com.ognev.kotlin.agendacalendarview.R
import com.ognev.kotlin.agendacalendarview.attributes.ViewAttributes
import com.ognev.kotlin.agendacalendarview.calendar.day.DayItem
import com.ognev.kotlin.agendacalendarview.calendar.week.WeekItem
import com.ognev.kotlin.agendacalendarview.calendar.week.WeeksAdapter
import com.ognev.kotlin.agendacalendarview.utils.inflateWithAttach
import kotlinx.android.synthetic.main.calendar.view.*
import org.joda.time.LocalDate

/**
 * The calendar view is a freely scrolling view that allows the user to browse between days of the
 * year.
 */
class CalendarView(context: Context, attrs: AttributeSet) : LinearLayout(context, attrs) {

    private var selectedDay: DayItem? = null
    private lateinit var weeks: List<WeekItem>
    private var currentWeekIndex = 0

    init {
        inflateWithAttach(R.layout.calendar, true)
        orientation = VERTICAL
    }

    fun init(weeks: List<WeekItem>, viewAttributes: ViewAttributes) {
        this.weeks = weeks
        daysNamesHeaderView.setBackgroundColor(viewAttributes.daysNamesHeaderColor)
        daysNamesHeaderView.setTextColor(viewAttributes.daysNamesTextColor)
        setupAdapter(weeks, viewAttributes)
    }

    fun scrollToDay(day: DayItem) {
        updateSelectedDay(day)
        weeksView.scrollToPosition(currentWeekIndex)
    }

    private fun setupAdapter(weeks: List<WeekItem>, viewAttributes: ViewAttributes) {
        weeksView.adapter = WeeksAdapter(weeks, viewAttributes)
    }

    private fun updateSelectedDay(dayItem: DayItem) {
        updateSelection(dayItem)
        val dayWeekIndex = weeks.indexOfFirst { it.days[0].date.isSameWeek(dayItem.date) }
        if (dayWeekIndex != -1) {
            if (dayWeekIndex != currentWeekIndex) {
                weeksView.adapter.notifyItemChanged(currentWeekIndex)
            }
            currentWeekIndex = dayWeekIndex
            weeksView.adapter.notifyItemChanged(dayWeekIndex)
        }
    }

    private fun updateSelection(dayItem: DayItem) {
        if (dayItem != selectedDay) {
            dayItem.isSelected = true
            selectedDay?.isSelected = false
            selectedDay = dayItem
        }
    }

    private fun LocalDate.isSameWeek(date: LocalDate): Boolean {
        return this.weekOfWeekyear == date.weekOfWeekyear
    }
}
