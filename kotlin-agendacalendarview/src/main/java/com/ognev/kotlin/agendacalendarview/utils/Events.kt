package com.ognev.kotlin.agendacalendarview.utils

import com.ognev.kotlin.agendacalendarview.models.IDayItem

import java.util.Calendar

/**
 * Events emitted by the bus provider.
 */
sealed class Event

class FetchedEvent : Event()

class CalendarScrolledEvent : Event()

class AgendaListViewTouchedEvent : Event()

class DayClickedEvent(val day: IDayItem) : Event() {
    var calendar: Calendar = Calendar.getInstance().also {
        it.time = day.date
    }
}
