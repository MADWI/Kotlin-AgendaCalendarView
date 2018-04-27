package com.ognev.kotlin.agendacalendarview.sample.event

import com.ognev.kotlin.agendacalendarview.calendar.day.DayItem
import com.ognev.kotlin.agendacalendarview.event.CalendarEvent

class MyCalendarEvent(override var day: DayItem, override var event: Any?) : CalendarEvent
