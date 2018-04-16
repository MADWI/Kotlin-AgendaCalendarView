package com.ognev.kotlin.agendacalendarview.utils

import com.ognev.kotlin.agendacalendarview.models.IDayItem

import java.util.Calendar

/**
 * Events emitted by the bus provider.
 */
sealed class Event

class CalendarScrolled : Event()

class AgendaListViewTouched : Event()

class DayClicked(val day: IDayItem) : Event() {
    var calendar: Calendar = Calendar.getInstance().also {
        it.time = day.date
    }
}
