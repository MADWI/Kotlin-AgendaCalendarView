package com.ognev.kotlin.agendacalendarview

import android.content.Context
import android.util.AttributeSet
import android.widget.FrameLayout
import com.ognev.kotlin.agendacalendarview.attributes.AttributesProvider
import com.ognev.kotlin.agendacalendarview.bus.BusProvider
import com.ognev.kotlin.agendacalendarview.bus.DayClicked
import com.ognev.kotlin.agendacalendarview.bus.Event
import com.ognev.kotlin.agendacalendarview.calendar.CalendarAnimator
import com.ognev.kotlin.agendacalendarview.calendar.day.DayItem
import com.ognev.kotlin.agendacalendarview.calendar.week.WeeksProvider
import com.ognev.kotlin.agendacalendarview.event.CalendarEvent
import com.ognev.kotlin.agendacalendarview.event.EventsProvider
import com.ognev.kotlin.agendacalendarview.render.CalendarEventRenderer
import com.ognev.kotlin.agendacalendarview.utils.inflateWithAttach
import kotlinx.android.synthetic.main.agenda_calendar.view.*
import org.joda.time.LocalDate
import rx.Subscription

/**
 * View holding the agenda and calendar view together.
 */
class AgendaCalendarView(context: Context, attrs: AttributeSet) : FrameLayout(context, attrs) {

    private val weeksProvider = WeeksProvider()
    private val eventsProvider = EventsProvider()
    private val viewAttributes = AttributesProvider().getAttributes(context, attrs)
    private lateinit var agendaEvents: MutableList<CalendarEvent>
    private var subscription: Subscription? = null
    private var onDayChangedListener: ((DayItem) -> Unit)? = null
    private val calendarAnimator: CalendarAnimator by lazy { CalendarAnimator(calendarView) }
    private var isCollapsed = true

    init {
        inflateWithAttach(R.layout.agenda_calendar, true)
        calendarView.setBackgroundColor(viewAttributes.calendarColor)
        subscribeOnEvents()
        setupAgendaOnDayChangedListener()
        setupCalendarToggleButtonClickListener()
    }

    private fun subscribeOnEvents() {
        subscription = BusProvider.instance.toObservable()
            .subscribe { handleEvent(it) }
    }

    private fun handleEvent(event: Event) {
        if (event is DayClicked) {
            onDayChangedListener?.invoke(event.day)
        }
    }

    private fun setupAgendaOnDayChangedListener() {
        agendaView.onDayChangedListener = {
            calendarView.scrollToDay(it)
            onDayChangedListener?.invoke(it)
        }
    }

    private fun setupCalendarToggleButtonClickListener() {
        calendarToggleButton.setOnClickListener {
            if (isCollapsed) {
                calendarAnimator.expandView()
                isCollapsed = false
            } else {
                isCollapsed = true
                calendarAnimator.collapseView()
            }
        }
    }

    fun setDayChangedListener(dayListener: (DayItem) -> Unit) {
        this.onDayChangedListener = dayListener
    }

    fun init(minDate: LocalDate, maxDate: LocalDate, eventRenderer: CalendarEventRenderer<CalendarEvent>, events: List<CalendarEvent>) {
        val weeks = weeksProvider.getWeeksBetweenDates(minDate, maxDate)
        this.agendaEvents = eventsProvider.getAgendaEvents(events, weeks)
        agendaView.init(agendaEvents, eventRenderer)
        calendarView.init(weeks, viewAttributes)
        moveToDate(LocalDate.now())
    }

    fun moveToDate(date: LocalDate) =
        agendaEvents.find { date.isEqual(it.day.date) }
            ?.let { BusProvider.instance.send(DayClicked(it.day)) }

    fun dispose() {
        subscription?.unsubscribe()
        calendarView.dispose()
        calendarAnimator.dispose()
        agendaView.dispose()
    }
}
