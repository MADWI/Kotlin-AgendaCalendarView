package com.ognev.kotlin.agendacalendarview.event

import com.ognev.kotlin.agendacalendarview.calendar.day.DayItem

class EmptyCalendarEvent(override var day: DayItem) : CalendarEvent {

    override var event: Any? = null
}
