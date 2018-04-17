package com.ognev.kotlin.agendacalendarview.agenda

import android.support.annotation.LayoutRes
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import com.ognev.kotlin.agendacalendarview.models.CalendarEvent
import com.ognev.kotlin.agendacalendarview.render.CalendarEventRenderer
import se.emilsjolander.stickylistheaders.StickyListHeadersAdapter

/**
 * Adapter for the agenda, implements StickyListHeadersAdapter.
 * Days as sections and CalendarEvents as list items.
 */
class AgendaAdapter : BaseAdapter(), StickyListHeadersAdapter {

    private lateinit var eventRenderer: CalendarEventRenderer<CalendarEvent>
    private val events: MutableList<CalendarEvent> = mutableListOf()

    override fun getHeaderView(position: Int, convertView: View?, parent: ViewGroup): View {
        var headerView = convertView
        if (headerView == null) {
            headerView = parent.inflate(eventRenderer.getHeaderLayout())
        }
        eventRenderer.setupHeaderItemView(headerView, getItem(position).date)
        return headerView
    }

    override fun getHeaderId(position: Int) = getEventId(position)

    override fun getItemId(position: Int) = getEventId(position)

    override fun getCount() = events.size

    override fun getItem(position: Int) = events[position]

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val event = events[position]
        val view = parent.inflate(getEventLayout(event))
        eventRenderer.setupEventItemView(view, event, position)
        return view
    }

    fun setEventAdapter(eventRenderer: CalendarEventRenderer<CalendarEvent>) {
        this.eventRenderer = eventRenderer
    }

    fun setEvents(events: List<CalendarEvent>) {
        this.events.clear()
        this.events.addAll(events)
    }

    private fun getEventLayout(event: CalendarEvent) = eventRenderer.getEventLayout(event.hasEvent())

    private fun getEventId(position: Int) = events[position].date.toDateTimeAtStartOfDay().millis

    private fun ViewGroup.inflate(@LayoutRes resource: Int): View =
        LayoutInflater.from(context).inflate(resource, this, false)
}
