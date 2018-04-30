package com.ognev.kotlin.agendacalendarview.calendar

import android.content.Context
import android.util.AttributeSet
import android.widget.LinearLayout
import com.ognev.kotlin.agendacalendarview.R
import com.ognev.kotlin.agendacalendarview.attributes.ViewAttributes
import com.ognev.kotlin.agendacalendarview.bus.BusProvider
import com.ognev.kotlin.agendacalendarview.bus.DayClicked
import com.ognev.kotlin.agendacalendarview.calendar.day.DayItem
import com.ognev.kotlin.agendacalendarview.calendar.week.WeekItem
import com.ognev.kotlin.agendacalendarview.calendar.week.WeeksAdapter
import com.ognev.kotlin.agendacalendarview.utils.inflateWithAttach
import kotlinx.android.synthetic.main.view_calendar.view.*
import org.joda.time.LocalDate
import rx.Subscription

/**
 * The calendar view is a freely scrolling view that allows the user to browse between days of the
 * year.
 */
class CalendarView(context: Context, attrs: AttributeSet) : LinearLayout(context, attrs) {

    private var selectedDay: DayItem? = null
    private lateinit var weeksAdapter: WeeksAdapter
    private lateinit var weeks: List<WeekItem>
    private var currentWeekIndex = 0
    private var subscription: Subscription? = null

    init {
        inflateWithAttach(R.layout.view_calendar, true)
        orientation = VERTICAL
    }

    fun init(weeks: List<WeekItem>, viewAttributes: ViewAttributes) {
        this.weeks = weeks
        daysNamesHeaderView.setBackgroundColor(viewAttributes.daysNamesHeaderColor)
        daysNamesHeaderView.setTextColor(viewAttributes.daysNamesTextColor)
        setupAdapter(weeks, viewAttributes)
        scrollToCurrentWeek(weeks)
        subscribeOnEvents()
    }

    private fun setupAdapter(weeks: List<WeekItem>, viewAttributes: ViewAttributes) {
        weeksAdapter = WeeksAdapter(viewAttributes)
        weeksView.adapter = weeksAdapter
        weeksAdapter.updateWeeksItems(weeks)
    }

    private fun scrollToCurrentWeek(weeks: List<WeekItem>) {
        val today = LocalDate.now()
        val weekIndex = weeks.indexOfFirst { today.isSameWeek(it.days[0].date) }
        weeksView.scrollToPosition(weekIndex)
    }

    private fun subscribeOnEvents() {
        subscription = BusProvider.instance.toObservable()
            .subscribe {
                if (it is DayClicked) updateSelectedDay(it.day)
            }
    }

    private fun updateSelectedDay(dayItem: DayItem) {
        updateSelection(dayItem)
        val dayWeekIndex = weeks.indexOfFirst { it.days[0].date.isSameWeek(dayItem.date) }
        if (dayWeekIndex != -1) {
            if (dayWeekIndex != currentWeekIndex) {
                weeksAdapter.notifyItemChanged(currentWeekIndex)
            }
            currentWeekIndex = dayWeekIndex
            weeksAdapter.notifyItemChanged(dayWeekIndex)
        }
    }

    private fun updateSelection(dayItem: DayItem) {
        if (dayItem != selectedDay) {
            dayItem.isSelected = true
            selectedDay?.isSelected = false
            selectedDay = dayItem
        }
    }

    fun scrollToDay(day: DayItem) {
        updateSelectedDay(day)
        weeksView.scrollToPosition(currentWeekIndex)
    }

    fun dispose() {
        subscription?.unsubscribe()
    }

    private fun LocalDate.isSameWeek(date: LocalDate): Boolean {
        return this.weekOfWeekyear == date.weekOfWeekyear
    }
}
