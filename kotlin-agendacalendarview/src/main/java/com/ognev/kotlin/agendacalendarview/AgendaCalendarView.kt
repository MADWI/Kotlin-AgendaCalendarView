package com.ognev.kotlin.agendacalendarview

import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.widget.FrameLayout
import com.ognev.kotlin.agendacalendarview.agenda.AgendaAdapter
import com.ognev.kotlin.agendacalendarview.attributes.AttributesProvider
import com.ognev.kotlin.agendacalendarview.calendar.CalendarAnimator
import com.ognev.kotlin.agendacalendarview.calendar.day.AgendaScroll
import com.ognev.kotlin.agendacalendarview.calendar.day.CalendarClick
import com.ognev.kotlin.agendacalendarview.calendar.day.DayItem
import com.ognev.kotlin.agendacalendarview.calendar.day.None
import com.ognev.kotlin.agendacalendarview.calendar.week.WeekItem
import com.ognev.kotlin.agendacalendarview.calendar.week.WeeksProvider
import com.ognev.kotlin.agendacalendarview.calendar.week.WeeksViewModel
import com.ognev.kotlin.agendacalendarview.event.CalendarEvent
import com.ognev.kotlin.agendacalendarview.event.EventsProvider
import com.ognev.kotlin.agendacalendarview.render.CalendarEventRenderer
import com.ognev.kotlin.agendacalendarview.utils.inflateWithAttach
import kotlinx.android.synthetic.main.agenda_calendar.view.*
import org.joda.time.LocalDate

/**
 * View holding the agenda and calendar view together.
 */
class AgendaCalendarView(context: Context, attrs: AttributeSet) : FrameLayout(context, attrs) {

    private val weeksProvider = WeeksProvider()
    private val eventsProvider = EventsProvider()
    private val viewAttributes = AttributesProvider().getAttributes(context, attrs)
    private val calendarAnimator: CalendarAnimator by lazy { CalendarAnimator(calendarView) }
    private lateinit var agendaEvents: MutableList<CalendarEvent>
    private var onDayChangedListener: ((DayItem) -> Unit)? = null

    init {
        inflateWithAttach(R.layout.agenda_calendar, true)
        calendarView.setBackgroundColor(viewAttributes.calendarColor)
        setupAgendaOnDayChangedListener()
        setupCalendarToggleButtonClickListener()
    }

    fun setDayChangedListener(dayListener: (DayItem) -> Unit) {
        this.onDayChangedListener = dayListener
    }

    fun init(minDate: LocalDate, maxDate: LocalDate, eventRenderer: CalendarEventRenderer<CalendarEvent>, events: List<CalendarEvent>) {
        val weekItems = weeksProvider.getWeeksBetweenDates(minDate, maxDate)
        this.agendaEvents = eventsProvider.getAgendaEvents(events, weekItems)
        agendaView.adapter = AgendaAdapter(agendaEvents, eventRenderer)
        setupWeeksViewModel(weekItems)
        calendarView.init(weekItems, viewAttributes)
        selectDate(LocalDate.now())
    }

    fun selectDate(date: LocalDate) =
        agendaEvents.find { date.isEqual(it.day.date) }
            ?.let { it.day.selectedBy = CalendarClick() }

    fun dispose() = calendarAnimator.dispose()

    private fun setupWeeksViewModel(weekItems: List<WeekItem>) {
        WeeksViewModel(weekItems, {
            calendarView.scrollToWeekIndex(it)
            log("onWeekIndexChanged: $it")
        }, {
            log("onSelectedDayChanged: ${it.date}, isSelected: ${it.isSelected}")
            scrollAgendaToDate(it.date)
        } )
    }

    private fun setupAgendaOnDayChangedListener() {
        agendaView.onHeaderChangedListener = {
            val day = agendaEvents[it].day
            log("onHeaderChangedListener Date: ${day.date}, isSelected: ${day.isSelected}")
            if (day.selectedBy is None) {
                day.selectedBy = AgendaScroll()
            }
        }
    }

    private fun log(message: String) {
        Log.e("AgendaCalendar", message)
    }

    private fun setupCalendarToggleButtonClickListener() {
        expandCalendarButton.setOnClickListener {
            if (expandCalendarButton.willAnimateForward()) {
                calendarAnimator.expandView()
            } else {
                calendarAnimator.collapseView()
            }
            expandCalendarButton.startAnimation()
        }
    }

    private fun scrollAgendaToDate(date: LocalDate) {
        val selection = agendaEvents.indexOfFirst { date.isEqual(it.day.date) }
        agendaView.setSelection(selection)
    }
}
