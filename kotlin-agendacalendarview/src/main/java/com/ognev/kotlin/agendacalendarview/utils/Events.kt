package com.ognev.kotlin.agendacalendarview.utils

import com.ognev.kotlin.agendacalendarview.models.IDayItem

/**
 * Events emitted by the bus provider.
 */
sealed class Event

class CalendarScrolled : Event()

class AgendaListViewTouched : Event()

class DayClicked(val day: IDayItem) : Event()
