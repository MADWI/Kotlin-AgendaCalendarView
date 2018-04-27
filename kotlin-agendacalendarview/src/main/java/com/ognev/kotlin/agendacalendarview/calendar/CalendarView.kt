package com.ognev.kotlin.agendacalendarview.calendar

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.LinearLayout
import com.ognev.kotlin.agendacalendarview.R
import com.ognev.kotlin.agendacalendarview.calendar.day.DayItem
import com.ognev.kotlin.agendacalendarview.calendar.week.WeekItem
import com.ognev.kotlin.agendacalendarview.calendar.week.WeeksAdapter
import com.ognev.kotlin.agendacalendarview.event.CalendarEvent
import com.ognev.kotlin.agendacalendarview.models.ViewAttributes
import com.ognev.kotlin.agendacalendarview.bus.AgendaListViewTouched
import com.ognev.kotlin.agendacalendarview.bus.BusProvider
import com.ognev.kotlin.agendacalendarview.bus.CalendarScrolled
import com.ognev.kotlin.agendacalendarview.bus.DayClicked
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
    private val calendarAnimator = CalendarAnimator(this)

    init {
        LayoutInflater.from(context).inflate(R.layout.view_calendar, this, true)
        orientation = VERTICAL
    }

    fun init(weeks: List<WeekItem>, viewAttributes: ViewAttributes) {
        this.weeks = weeks
        daysNamesHeaderView.setBackgroundColor(viewAttributes.daysNamesHeaderColor)
        daysNamesHeaderView.setTextColor(viewAttributes.daysNamesTextColor)
        setupAdapter(weeks, viewAttributes)
        scrollToCurrentWeek(weeks)
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

    override fun onFinishInflate() {
        super.onFinishInflate()
        subscribeOnEvents()
    }

    private fun subscribeOnEvents() {
        subscription = BusProvider.instance.toObservable()
            .subscribe { event ->
                when (event) {
                    is CalendarScrolled -> calendarAnimator.expandView()
                    is AgendaListViewTouched -> calendarAnimator.collapseView()
                    is DayClicked -> updateSelectedDay(event.day)
                }
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

    fun scrollToDate(calendarEvent: CalendarEvent) {
        updateSelectedDay(calendarEvent.day)
        weeksView.scrollToPosition(currentWeekIndex)
    }

    fun dispose() {
        subscription?.unsubscribe()
        calendarAnimator.dispose()
    }

    private fun LocalDate.isSameWeek(date: LocalDate): Boolean {
        return this.weekOfWeekyear == date.weekOfWeekyear
    }
}
