package com.ognev.kotlin.agendacalendarview.sample

import android.content.Context
import android.support.v4.content.ContextCompat
import android.view.View
import android.widget.TextView
import android.widget.Toast
import com.ognev.kotlin.agendacalendarview.CalendarManager
import com.ognev.kotlin.agendacalendarview.models.CalendarEvent
import com.ognev.kotlin.agendacalendarview.models.EmptyCalendarEvent
import com.ognev.kotlin.agendacalendarview.render.CalendarEventRenderer
import com.ognev.kotlin.agendacalendarview.utils.DateHelper
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

/**
 * Sample event adapter
 */
class SampleEventAgendaRenderer(private var context: Context) : CalendarEventRenderer<CalendarEvent> {
    private var format: SimpleDateFormat = SimpleDateFormat(context.getString(R.string.header_date), Locale.ENGLISH)

    override fun getHeaderLayout() = R.layout.view_agenda_header

    override fun setupHeaderItemView(headerItemView: View, day: Calendar) {
        val txtDayOfMonth = headerItemView.findViewById(R.id.view_agenda_day_of_month) as TextView
        val today = CalendarManager.instance!!.today

        if (DateHelper.sameDate(day, today)) {
            txtDayOfMonth.setTextColor(ContextCompat.getColor(context, R.color.main_blue))
        } else {
            txtDayOfMonth.setTextColor(ContextCompat.getColor(context, R.color.text_light_color))
        }

        txtDayOfMonth.text = format.format(day.time)
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
