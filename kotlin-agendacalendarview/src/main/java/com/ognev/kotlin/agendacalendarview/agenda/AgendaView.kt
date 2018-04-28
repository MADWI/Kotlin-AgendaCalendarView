package com.ognev.kotlin.agendacalendarview.agenda

import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import com.ognev.kotlin.agendacalendarview.bus.AgendaListViewTouched
import com.ognev.kotlin.agendacalendarview.bus.BusProvider
import com.ognev.kotlin.agendacalendarview.bus.DayClicked
import com.ognev.kotlin.agendacalendarview.bus.Event
import com.ognev.kotlin.agendacalendarview.calendar.day.DayItem
import com.ognev.kotlin.agendacalendarview.event.CalendarEvent
import com.ognev.kotlin.agendacalendarview.render.CalendarEventRenderer
import org.joda.time.LocalDate
import rx.Subscription
import se.emilsjolander.stickylistheaders.StickyListHeadersListView

class AgendaView(context: Context, attrs: AttributeSet) : StickyListHeadersListView(context, attrs),
    StickyListHeadersListView.OnStickyHeaderChangedListener {

    lateinit var events: List<CalendarEvent>
    lateinit var onDayChangeListener: (day: DayItem) -> Unit
    private val agendaAdapter = AgendaAdapter()
    private var subscription: Subscription? = null

    override
    fun onStickyHeaderChanged(stickyListHeadersListView: StickyListHeadersListView, header: View, position: Int, headerId: Long) =
        onDayChangeListener.invoke(events[position].day)

    override fun onFinishInflate() {
        super.onFinishInflate()
        subscription = BusProvider.instance.toObservable()
            .subscribe { event -> handleEvent(event) }
    }

    init {
        setOnStickyHeaderChangedListener(this)
        this.adapter = agendaAdapter
    }

    fun init(events: List<CalendarEvent>, eventRenderer: CalendarEventRenderer<CalendarEvent>) {
        this.events = events
        agendaAdapter.eventRenderer = eventRenderer
        agendaAdapter.setEvents(events)
    }

    override
    fun dispatchTouchEvent(event: MotionEvent): Boolean {
        if (event.action == MotionEvent.ACTION_MOVE) {
            BusProvider.instance.send(AgendaListViewTouched())
        }
        return super.dispatchTouchEvent(event)
    }

    fun dispose() = subscription?.unsubscribe()

    private fun handleEvent(event: Event) {
        when (event) {
            is DayClicked -> scrollToDate(event.day.date)
        }
    }

    private fun scrollToDate(date: LocalDate) {
        val selection = events.indexOfFirst { date.compareTo(it.day.date) == 0 }
        post { setSelection(selection) }
    }
}
