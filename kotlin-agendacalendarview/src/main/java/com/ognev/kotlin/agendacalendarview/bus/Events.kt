package com.ognev.kotlin.agendacalendarview.bus

import com.ognev.kotlin.agendacalendarview.calendar.day.DayItem

/**
 * Events emitted by the bus provider.
 */
internal sealed class Event

internal class DayClicked(val day: DayItem) : Event()
