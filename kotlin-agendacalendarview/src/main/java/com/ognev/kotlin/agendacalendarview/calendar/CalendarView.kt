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
open class CalendarView : LinearLayout {

    /**
     * The current highlighted day in blue
     */
    var selectedDay: DayItem? = null

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
    private val dayNameFormatter = DateTimeFormat.forPattern("EEE")
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
                    is DayClicked -> updateSelectedDay(event.day)
                }
            }
    }

    fun init(calendarManager: CalendarManager, agendaCalendarViewAttributes: AgendaCalendarViewAttributes) {
        val weeks = calendarManager.weeks

        setupDayNamesHeader()
        setUpAdapter(weeks, agendaCalendarViewAttributes)
        scrollToCurrentWeek(weeks)
    }

    /**
     * Fired when the Agenda list view changes section.

     * @param calendarEvent The event for the selected position in the agenda listview.
     */
    fun scrollToDate(calendarEvent: CalendarEvent) {
        listViewWeeks.post { scrollToPosition(updateSelectedDay(calendarEvent.dayReference)) }
    }

    private fun scrollToCurrentWeek(weeks: List<WeekItem>) {
        val toady = LocalDate.now()
        val weekIndex = weeks.indexOfFirst { toady.isSameWeek(it.date) }
        listViewWeeks.post { scrollToPosition(weekIndex) }
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
    private fun setUpAdapter(weeks: List<WeekItem>, viewAttributes: AgendaCalendarViewAttributes) {
        if (mWeeksAdapter == null) {
            mWeeksAdapter = WeeksAdapter(context, viewAttributes)
            listViewWeeks.adapter = mWeeksAdapter
        }
        mWeeksAdapter!!.updateWeeksItems(weeks)
    }

    private fun setupDayNamesHeader() {
        val today = LocalDate.now()
        for (i in 0 until dayNamesHeader.childCount) {
            val dayTextView = dayNamesHeader.getChildAt(i) as TextView
            val day = today.withDayOfWeek(i + 1)
            dayTextView.text = dayNameFormatter.print(day).toUpperCase()
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
     *
     * @param dayItem  The DayItem information held by the cell item.
     * *
     * @return The selected row of the weeks list, to be updated.
     */
    private fun updateSelectedDay(dayItem: DayItem): Int {
        // update highlighted/selected day
        if (dayItem != selectedDay) {
            dayItem.isSelected = true
            selectedDay?.isSelected = false
            selectedDay = dayItem
        }

        var currentWeekIndex: Int? = null
        val weeks = CalendarManager.instance!!.weeks
        for (c in 0 until weeks.size) {
            val week = weeks[c].date
            if (dayItem.date.isSameWeek(week)) {
                currentWeekIndex = c
                break
            }
        }

        if (currentWeekIndex != null) {
            // highlighted day has changed, update the rows concerned
            if (currentWeekIndex != mCurrentListPosition) {
                updateItemAtPosition(mCurrentListPosition)
            }
            mCurrentListPosition = currentWeekIndex
            updateItemAtPosition(currentWeekIndex)
        }

        return mCurrentListPosition
    }

    fun dispose() = subscription?.unsubscribe()

    private fun LocalDate.isSameWeek(date: LocalDate): Boolean {
        return this.weekOfWeekyear == date.weekOfWeekyear
    }
}
