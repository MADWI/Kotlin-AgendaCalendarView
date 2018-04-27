package com.ognev.kotlin.agendacalendarview.utils

import com.ognev.kotlin.agendacalendarview.calendar.day.DayItem

/**
 * Events emitted by the bus provider.
 */
sealed class Event

class CalendarScrolled : Event()

class AgendaListViewTouched : Event()

class DayClicked(val day: DayItem) : Event()
