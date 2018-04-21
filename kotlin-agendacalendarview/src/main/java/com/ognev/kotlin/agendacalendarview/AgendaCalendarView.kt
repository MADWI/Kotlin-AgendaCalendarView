package com.ognev.kotlin.agendacalendarview

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.FrameLayout
import com.ognev.kotlin.agendacalendarview.agenda.AgendaAdapter
import com.ognev.kotlin.agendacalendarview.agenda.AgendaView
import com.ognev.kotlin.agendacalendarview.calendar.CalendarView
import com.ognev.kotlin.agendacalendarview.models.CalendarEvent
import com.ognev.kotlin.agendacalendarview.render.CalendarEventRenderer
import com.ognev.kotlin.agendacalendarview.utils.AttributesProvider
import com.ognev.kotlin.agendacalendarview.utils.BusProvider
import com.ognev.kotlin.agendacalendarview.utils.DayClicked
import com.ognev.kotlin.agendacalendarview.utils.Event
import com.ognev.kotlin.agendacalendarview.utils.EventsProvider
import com.ognev.kotlin.agendacalendarview.utils.WeeksProvider
import org.joda.time.LocalDate
import rx.Subscription
import se.emilsjolander.stickylistheaders.StickyListHeadersListView

/**
 * View holding the agenda and calendar view together.
 */
class AgendaCalendarView(context: Context, attrs: AttributeSet) : FrameLayout(context, attrs),
    StickyListHeadersListView.OnStickyHeaderChangedListener {

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
        calendarView.findViewById<View>(R.id.cal_day_names).setBackgroundColor(viewAttributes.headerColor)

        subscription = BusProvider.instance.toObservable()
            .subscribe { handleEvent(it) }
    }

    private fun handleEvent(event: Event) {
        if (event is DayClicked) {
            calendarController?.onDaySelected(event.day)
        }
    }

    override
    fun onStickyHeaderChanged(stickyListHeadersListView: StickyListHeadersListView, header: View, position: Int, headerId: Long) {
        val event = agendaEvents[position]
        calendarView.scrollToDate(event)
        calendarController?.onScrollToDate(event.date)
    }

    fun setCallbacks(calendarController: CalendarController) {
        this.calendarController = calendarController
    }

    fun init(minDate: LocalDate, maxDate: LocalDate, eventRenderer: CalendarEventRenderer<CalendarEvent>, events: List<CalendarEvent>) {
        val startDate = minDate.withDayOfMonth(1)
        val endDate = maxDate.withDayOfMonth(30)
        val weeks = weeksProvider.getWeeksBetweenDates(startDate, endDate)
        this.agendaEvents = eventsProvider.getAgendaEvents(events, weeks)

        val agendaAdapter = AgendaAdapter()
        agendaView.events = agendaEvents
        agendaView.adapter = agendaAdapter
        agendaView.setOnStickyHeaderChangedListener(this)

        agendaAdapter.setEvents(agendaEvents)
        agendaAdapter.setEventAdapter(eventRenderer)

        calendarView.init(weeks, viewAttributes)
    }

    fun dispose() {
        subscription?.unsubscribe()
        calendarView.dispose()
        agendaView.dispose()
    }
}
