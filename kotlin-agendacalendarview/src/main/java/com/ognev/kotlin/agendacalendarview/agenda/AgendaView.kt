package com.ognev.kotlin.agendacalendarview.agenda

import android.content.Context
import android.util.AttributeSet
import android.view.View
import com.ognev.kotlin.agendacalendarview.calendar.day.DayItem
import com.ognev.kotlin.agendacalendarview.event.CalendarEvent
import com.ognev.kotlin.agendacalendarview.render.CalendarEventRenderer
import org.joda.time.LocalDate
import se.emilsjolander.stickylistheaders.StickyListHeadersListView

class AgendaView(context: Context, attrs: AttributeSet) : StickyListHeadersListView(context, attrs),
    StickyListHeadersListView.OnStickyHeaderChangedListener {

    lateinit var onDayChangedListener: (day: DayItem) -> Unit
    private lateinit var events: List<CalendarEvent>

    init {
        setOnStickyHeaderChangedListener(this)
    }

    override
    fun onStickyHeaderChanged(stickyListHeadersListView: StickyListHeadersListView, header: View, position: Int, headerId: Long) =
        onDayChangedListener.invoke(events[position].day)

    fun init(events: List<CalendarEvent>, eventRenderer: CalendarEventRenderer<CalendarEvent>) {
        this.events = events
        val agendaAdapter = AgendaAdapter()
        agendaAdapter.eventRenderer = eventRenderer
        agendaAdapter.setEvents(events)
        adapter = agendaAdapter
    }

    fun scrollToDate(date: LocalDate) {
        val selection = events.indexOfFirst { date.compareTo(it.day.date) == 0 }
        post { setSelection(selection) }
    }
}
