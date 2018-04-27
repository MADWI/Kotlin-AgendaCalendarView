package com.ognev.kotlin.agendacalendarview.models

import com.ognev.kotlin.agendacalendarview.calendar.day.DayItem
import org.joda.time.LocalDate

interface CalendarEvent {

    var event: Any?

    val date: LocalDate

    var day: DayItem

    fun hasEvent() = event != null
}
