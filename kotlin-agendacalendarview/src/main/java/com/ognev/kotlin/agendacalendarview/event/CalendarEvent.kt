package com.ognev.kotlin.agendacalendarview.event

import com.ognev.kotlin.agendacalendarview.calendar.day.DayItem

interface CalendarEvent {

    var event: Any?

    var day: DayItem

    fun hasEvent() = event != null
}
