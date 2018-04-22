package com.ognev.kotlin.agendacalendarview.calendar

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.LinearLayout
import android.widget.TextView
import com.ognev.kotlin.agendacalendarview.R
import com.ognev.kotlin.agendacalendarview.calendar.weekslist.WeekListView
import com.ognev.kotlin.agendacalendarview.calendar.weekslist.WeeksAdapter
import com.ognev.kotlin.agendacalendarview.models.AgendaCalendarViewAttributes
import com.ognev.kotlin.agendacalendarview.models.CalendarEvent
import com.ognev.kotlin.agendacalendarview.models.DayItem
import com.ognev.kotlin.agendacalendarview.models.WeekItem
import com.ognev.kotlin.agendacalendarview.utils.AgendaListViewTouched
import com.ognev.kotlin.agendacalendarview.utils.BusProvider
import com.ognev.kotlin.agendacalendarview.utils.CalendarScrolled
import com.ognev.kotlin.agendacalendarview.utils.DayClicked
import org.joda.time.LocalDate
import org.joda.time.format.DateTimeFormat
import rx.Subscription

/**
 * The calendar view is a freely scrolling view that allows the user to browse between days of the
 * year.
 */
class CalendarView(context: Context, attrs: AttributeSet) : LinearLayout(context, attrs) {

    private var selectedDay: DayItem? = null
    private val dayNameFormatter = DateTimeFormat.forPattern("EEE")
    private lateinit var weekListView: WeekListView
    private lateinit var weeksAdapter: WeeksAdapter
    private lateinit var dayNamesHeader: LinearLayout
    private lateinit var weeks: List<WeekItem>
    private var currentWeekIndex = 0
    private var subscription: Subscription? = null
    private val calendarAnimator = CalendarAnimator(this)

    init {
        LayoutInflater.from(context).inflate(R.layout.view_calendar, this, true)
        orientation = VERTICAL
    }

    fun init(weeks: List<WeekItem>, viewAttributes: AgendaCalendarViewAttributes) {
        this.weeks = weeks
        setupDayNamesHeader()
        setupAdapter(weeks, viewAttributes)
        scrollToCurrentWeek(weeks)
    }

    private fun setupDayNamesHeader() {
        val today = LocalDate.now()
        for (i in 0 until dayNamesHeader.childCount) {
            val dayTextView = dayNamesHeader.getChildAt(i) as TextView
            val day = today.withDayOfWeek(i + 1)
            dayTextView.text = dayNameFormatter.print(day).toUpperCase()
        }
    }

    private fun setupAdapter(weeks: List<WeekItem>, viewAttributes: AgendaCalendarViewAttributes) {
        weeksAdapter = WeeksAdapter(context, viewAttributes)
        weekListView.adapter = weeksAdapter
        weeksAdapter.updateWeeksItems(weeks)
    }

    private fun scrollToCurrentWeek(weeks: List<WeekItem>) {
        val today = LocalDate.now()
        val weekIndex = weeks.indexOfFirst { today.isSameWeek(it.firstDay) }
        weekListView.scrollToPosition(weekIndex)
    }

    override fun onFinishInflate() {
        super.onFinishInflate()
        dayNamesHeader = findViewById(R.id.cal_day_names)
        weekListView = findViewById(R.id.list_week)
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
        val dayWeekIndex = weeks.indexOfFirst { it.firstDay.isSameWeek(dayItem.date) }
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
        updateSelectedDay(calendarEvent.dayReference)
        weekListView.scrollToPosition(currentWeekIndex)
    }

    override fun setBackgroundColor(color: Int) = weekListView.setBackgroundColor(color)

    fun dispose() {
        subscription?.unsubscribe()
        calendarAnimator.dispose()
    }

    private fun LocalDate.isSameWeek(date: LocalDate): Boolean {
        return this.weekOfWeekyear == date.weekOfWeekyear
    }
}
