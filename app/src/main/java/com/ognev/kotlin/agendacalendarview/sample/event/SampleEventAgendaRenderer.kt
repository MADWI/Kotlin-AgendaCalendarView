package com.ognev.kotlin.agendacalendarview.sample.event

import android.content.Context
import android.support.v4.content.ContextCompat
import android.view.View
import android.widget.TextView
import android.widget.Toast
import com.ognev.kotlin.agendacalendarview.event.CalendarEvent
import com.ognev.kotlin.agendacalendarview.event.EmptyCalendarEvent
import com.ognev.kotlin.agendacalendarview.render.CalendarEventRenderer
import com.ognev.kotlin.agendacalendarview.sample.R
import net.danlew.android.joda.DateUtils
import org.joda.time.LocalDate
import org.joda.time.format.DateTimeFormat

/**
 * Sample event adapter
 */
class SampleEventAgendaRenderer(private var context: Context) : CalendarEventRenderer<CalendarEvent> {
    private var headerFormatter = DateTimeFormat.forPattern("EEEE, d MMMM")

    override fun getHeaderLayout() = R.layout.view_agenda_header

    override fun setupHeaderItemView(headerItemView: View, day: LocalDate) {
        val txtDayOfMonth = headerItemView.findViewById(R.id.view_agenda_day_of_month) as TextView
        if (DateUtils.isToday(day)) {
            txtDayOfMonth.setTextColor(ContextCompat.getColor(context, R.color.main_blue))
        } else {
            txtDayOfMonth.setTextColor(ContextCompat.getColor(context, R.color.text_light_color))
        }
        txtDayOfMonth.text = headerFormatter.print(day)
    }

    override fun setupEventItemView(eventItemView: View, event: CalendarEvent, position: Int) {
        when (event) {
            is EmptyCalendarEvent -> setupEmptyCalendarEventView(eventItemView, position)
            is MyCalendarEvent -> setupCalendarEventView(event, eventItemView, position)
        }
    }

    private fun setupCalendarEventView(event: CalendarEvent, eventItemView: View, position: Int) {
        val myEvent = event as MyCalendarEvent
        val myObject: SampleEvent? = myEvent.event as SampleEvent?

        if (myEvent.hasEvent()) {
            val name = eventItemView.findViewById(R.id.name) as TextView
            val description = eventItemView.findViewById(R.id.description) as TextView

            name.text = myObject?.name
            description.text = myObject?.description
        }

        eventItemView.setOnClickListener {
            Toast.makeText(eventItemView.context, "Item: ${position.plus(1)}", Toast.LENGTH_SHORT).show()
        }
    }

    override fun getEventLayout(isEmptyEvent: Boolean) =
        if (isEmptyEvent) R.layout.view_agenda_event else R.layout.view_agenda_empty_event

    private fun setupEmptyCalendarEventView(eventItemView: View, position: Int) {
        eventItemView.setOnClickListener {
            Toast.makeText(eventItemView.context, "Item: ${position.plus(1)}", Toast.LENGTH_SHORT).show()
        }
    }
}
