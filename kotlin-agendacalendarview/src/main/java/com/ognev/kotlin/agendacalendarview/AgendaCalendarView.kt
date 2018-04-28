package com.ognev.kotlin.agendacalendarview

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.FrameLayout
import com.ognev.kotlin.agendacalendarview.agenda.AgendaView
import com.ognev.kotlin.agendacalendarview.attributes.AttributesProvider
import com.ognev.kotlin.agendacalendarview.bus.BusProvider
import com.ognev.kotlin.agendacalendarview.bus.DayClicked
import com.ognev.kotlin.agendacalendarview.bus.Event
import com.ognev.kotlin.agendacalendarview.calendar.CalendarView
import com.ognev.kotlin.agendacalendarview.calendar.week.WeeksProvider
import com.ognev.kotlin.agendacalendarview.event.CalendarEvent
import com.ognev.kotlin.agendacalendarview.event.EventsProvider
import com.ognev.kotlin.agendacalendarview.render.CalendarEventRenderer
import org.joda.time.LocalDate
import rx.Subscription

/**
 * View holding the agenda and calendar view together.
 */
class AgendaCalendarView(context: Context, attrs: AttributeSet) : FrameLayout(context, attrs) {

    private val weeksProvider = WeeksProvider()
    private val eventsProvider = EventsProvider()
    private val viewAttributes = AttributesProvider().getAttributes(context, attrs)
    private lateinit var agendaView: AgendaView
    private lateinit var calendarView: CalendarView
    private lateinit var agendaEvents: MutableList<CalendarEvent>
    private var subscription: Subscription? = null
    private var calendarController: CalendarController? = null

    init {
        LayoutInflater.from(context).inflate(R.layout.view_agendacalendar, this, true)
    }

    override fun onFinishInflate() {
        super.onFinishInflate()
        calendarView = findViewById(R.id.calendar_view)
        agendaView = findViewById(R.id.agenda_view)
        subscription = BusProvider.instance.toObservable()
            .subscribe { handleEvent(it) }
        setupOnDayChangeListener()
    }

    private fun handleEvent(event: Event) {
        if (event is DayClicked) {
            calendarController?.onDaySelected(event.day)
        }
    }

    private fun setupOnDayChangeListener() {
        agendaView.onDayChangeListener = {
            calendarView.scrollToDay(it)
            calendarController?.onScrollToDate(it.date)
        }
    }

    fun setCallbacks(calendarController: CalendarController) {
        this.calendarController = calendarController
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
        agendaView.dispose()
    }
}
