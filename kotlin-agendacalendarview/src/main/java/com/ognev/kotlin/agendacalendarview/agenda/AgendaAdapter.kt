package com.ognev.kotlin.agendacalendarview.agenda

import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import com.ognev.kotlin.agendacalendarview.event.CalendarEvent
import com.ognev.kotlin.agendacalendarview.render.CalendarEventRenderer
import com.ognev.kotlin.agendacalendarview.utils.inflateWithAttach
import se.emilsjolander.stickylistheaders.StickyListHeadersAdapter

/**
 * Adapter for the agenda, implements StickyListHeadersAdapter.
 * Days as sections and CalendarEvents as list items.
 */
class AgendaAdapter(private val events: List<CalendarEvent>, private val eventRenderer: CalendarEventRenderer<CalendarEvent>)
    : BaseAdapter(), StickyListHeadersAdapter {

    override fun getHeaderView(position: Int, convertView: View?, parent: ViewGroup): View {
        var headerView = convertView
        if (headerView == null) {
            headerView = parent.inflateWithAttach(eventRenderer.getHeaderLayout(), false)
        }
        eventRenderer.setupHeaderItemView(headerView, getItem(position).day.date)
        return headerView
    }

    override fun getHeaderId(position: Int) = getEventId(position)

    override fun getItemId(position: Int) = getEventId(position)

    override fun getCount() = events.size

    override fun getItem(position: Int) = events[position]

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val event = events[position]
        val view = parent.inflateWithAttach(getEventLayout(event), false)
        eventRenderer.setupEventItemView(view, event, position)
        return view
    }

    private fun getEventLayout(event: CalendarEvent) = eventRenderer.getEventLayout(event.hasEvent())

    private fun getEventId(position: Int): Long {
        val date = events[position].day.date
        return date.toDateTimeAtStartOfDay().millis
    }
}
