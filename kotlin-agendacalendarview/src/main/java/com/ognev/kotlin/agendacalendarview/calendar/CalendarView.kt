package com.ognev.kotlin.agendacalendarview.calendar

import android.animation.ValueAnimator
import android.content.Context
import android.support.v7.widget.LinearLayoutManager
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import com.ognev.kotlin.agendacalendarview.CalendarManager
import com.ognev.kotlin.agendacalendarview.R
import com.ognev.kotlin.agendacalendarview.calendar.weekslist.WeekListView
import com.ognev.kotlin.agendacalendarview.calendar.weekslist.WeeksAdapter
import com.ognev.kotlin.agendacalendarview.models.AgendaCalendarViewAttributes
import com.ognev.kotlin.agendacalendarview.models.CalendarEvent
import com.ognev.kotlin.agendacalendarview.models.IDayItem
import com.ognev.kotlin.agendacalendarview.models.IWeekItem
import com.ognev.kotlin.agendacalendarview.utils.AgendaListViewTouched
import com.ognev.kotlin.agendacalendarview.utils.BusProvider
import com.ognev.kotlin.agendacalendarview.utils.CalendarScrolled
import com.ognev.kotlin.agendacalendarview.utils.DateHelper
import com.ognev.kotlin.agendacalendarview.utils.DayClicked
import rx.Subscription
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

/**
 * The calendar view is a freely scrolling view that allows the user to browse between days of the
 * year.
 */
open class CalendarView : LinearLayout {

    /**
     * The current highlighted day in blue
     */
    var selectedDay: IDayItem? = null

    /**
     * Part of the calendar view layout always visible, the weeks list
     */
    lateinit var listViewWeeks: WeekListView
        private set
    /**
     * The adapter for the weeks list
     */

    private var mWeeksAdapter: WeeksAdapter? = null

    /**
     * Top of the calendar view layout, the week days list
     */
    private lateinit var dayNamesHeader: LinearLayout

    /**
     * The current row displayed at top of the list
     */
    private var mCurrentListPosition: Int = 0
    private var subscription: Subscription? = null

    private val expandedCalendarHeight: Int by lazy {
        val headerHeight = resources.getDimension(R.dimen.calendar_header_height)
        val rowsHeight = resources.getDimension(R.dimen.day_cell_height) * 5
        (headerHeight + rowsHeight).toInt()
    }

    private val collapsedCalendarHeight: Int by lazy {
        val headerHeight = resources.getDimension(R.dimen.calendar_header_height)
        val rowsHeight = resources.getDimension(R.dimen.day_cell_height) * 2
        (headerHeight + rowsHeight).toInt()
    }

    private val heightAnimationDuration: Long by lazy {
        resources.getInteger(android.R.integer.config_shortAnimTime).toLong()
    }

    constructor(context: Context) : super(context)

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        LayoutInflater.from(context).inflate(R.layout.view_calendar, this, true)
        orientation = VERTICAL
    }

    override fun onFinishInflate() {
        super.onFinishInflate()
        dayNamesHeader = findViewById(R.id.cal_day_names)
        listViewWeeks = findViewById(R.id.list_week)
        //TODO move initialization to listViewWeeks
        listViewWeeks.layoutManager = LinearLayoutManager(context)
        listViewWeeks.setHasFixedSize(true)
        listViewWeeks.itemAnimator = null
        listViewWeeks.setSnapEnabled(true)
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        subscription = BusProvider.instance.toObservable()
            .subscribe { event ->
                when (event) {
                    is CalendarScrolled -> expandCalendarView()
                    is AgendaListViewTouched -> collapseCalendarView()
                    is DayClicked -> updateSelectedDay(event.calendar, event.day)
                }
            }
    }

    fun init(calendarManager: CalendarManager, agendaCalendarViewAttributes: AgendaCalendarViewAttributes) {
        val today = calendarManager.today
        val locale = calendarManager.locale
        val weekDayFormatter = calendarManager.weekdayFormatter
        val weeks = calendarManager.weeks

        setUpHeader(today, weekDayFormatter, locale)
        setUpAdapter(today, weeks, agendaCalendarViewAttributes)
        scrollToDate(today, weeks)
    }

    /**
     * Fired when the Agenda list view changes section.

     * @param calendarEvent The event for the selected position in the agenda listview.
     */
    fun scrollToDate(calendarEvent: CalendarEvent) {
        listViewWeeks.post { scrollToPosition(updateSelectedDay(calendarEvent.instanceDay, calendarEvent.dayReference)) }
    }

    private fun scrollToDate(today: Calendar, weeks: List<IWeekItem>) {
        var currentWeekIndex: Int? = null

        for (c in 0 until weeks.size) {
            if (DateHelper.sameWeek(today, weeks[c])) {
                currentWeekIndex = c
                break
            }
        }

        if (currentWeekIndex != null) {
            val finalCurrentWeekIndex = currentWeekIndex
            listViewWeeks.post { scrollToPosition(finalCurrentWeekIndex.toInt()) }
        }
    }

    override fun setBackgroundColor(color: Int) {
        listViewWeeks.setBackgroundColor(color)
    }

    private fun scrollToPosition(targetPosition: Int) {
        val layoutManager = listViewWeeks.layoutManager as LinearLayoutManager
        layoutManager.scrollToPosition(targetPosition)
    }

    private fun updateItemAtPosition(position: Int) {
        val weeksAdapter = listViewWeeks.adapter as WeeksAdapter
        weeksAdapter.notifyItemChanged(position)
    }

    /**
     * Creates a new adapter if necessary and sets up its parameters.
     */
    private fun setUpAdapter(today: Calendar, weeks: List<IWeekItem>, viewAttributes: AgendaCalendarViewAttributes) {
        if (mWeeksAdapter == null) {
            mWeeksAdapter = WeeksAdapter(context, today, viewAttributes)
            listViewWeeks.adapter = mWeeksAdapter
        }
        mWeeksAdapter!!.updateWeeksItems(weeks)
    }

    private fun setUpHeader(today: Calendar, weekDayFormatter: SimpleDateFormat, locale: Locale) {
        val daysPerWeek = 7
        val dayLabels = arrayOfNulls<String>(daysPerWeek)
        val cal = Calendar.getInstance(CalendarManager.getInstance(context).locale)
        cal.time = today.time
        val firstDayOfWeek = cal.firstDayOfWeek
        for (count in 0..6) {
            cal.set(Calendar.DAY_OF_WEEK, firstDayOfWeek + count)
            dayLabels[count] = weekDayFormatter.format(cal.time).toUpperCase(locale)
        }

        for (i in 0 until dayNamesHeader.childCount) {
            val txtDay = dayNamesHeader.getChildAt(i) as TextView
            txtDay.text = dayLabels[i]
        }
    }

    private fun expandCalendarView() = animateCalendarHeightToValue(expandedCalendarHeight)

    private fun collapseCalendarView() = animateCalendarHeightToValue(collapsedCalendarHeight)

    private fun animateCalendarHeightToValue(endHeight: Int) {
        val animator = ValueAnimator.ofInt(measuredHeight, endHeight)
        animator.duration = heightAnimationDuration
        animator.addUpdateListener { setCalendarHeight(it.animatedValue as Int) }
        animator.start()
    }

    private fun setCalendarHeight(height: Int) {
        val layoutParams = layoutParams as ViewGroup.LayoutParams
        layoutParams.height = height
        setLayoutParams(layoutParams)
    }

    /**
     * Update a selected cell day item.

     * @param calendar The Calendar instance of the day selected.
     * *
     * @param dayItem  The DayItem information held by the cell item.
     * *
     * @return The selected row of the weeks list, to be updated.
     */
    private fun updateSelectedDay(calendar: Calendar, dayItem: IDayItem): Int {
        var currentWeekIndex: Int? = null

        // update highlighted/selected day
        if (dayItem != selectedDay) {
            dayItem.isSelected = true
            selectedDay?.isSelected = false
            selectedDay = dayItem
        }

        for (c in 0 until CalendarManager.instance!!.weeks.size) {
            if (DateHelper.sameWeek(calendar, CalendarManager.instance!!.weeks[c])) {
                currentWeekIndex = c
                break
            }
        }

        if (currentWeekIndex != null) {
            // highlighted day has changed, update the rows concerned
            if (!currentWeekIndex.equals(mCurrentListPosition)) {
                updateItemAtPosition(mCurrentListPosition)
            }
            mCurrentListPosition = currentWeekIndex.toInt()
            updateItemAtPosition(currentWeekIndex.toInt())
        }

        //TODO remove currentSelectedDay
        CalendarManager.instance!!.currentSelectedDay = calendar
        CalendarManager.instance!!.currentListPosition = mCurrentListPosition

        return mCurrentListPosition
    }

    fun dispose() = subscription?.unsubscribe()
}
