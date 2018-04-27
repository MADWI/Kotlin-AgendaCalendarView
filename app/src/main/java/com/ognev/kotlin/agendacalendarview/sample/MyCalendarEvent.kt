package com.ognev.kotlin.agendacalendarview.sample

import com.ognev.kotlin.agendacalendarview.calendar.day.DayItem
import com.ognev.kotlin.agendacalendarview.models.CalendarEvent
import org.joda.time.LocalDate

class MyCalendarEvent(override val date: LocalDate, override var day: DayItem,
    override var event: Any?) : CalendarEvent
