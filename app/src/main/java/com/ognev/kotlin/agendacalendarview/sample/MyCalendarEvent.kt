package com.ognev.kotlin.agendacalendarview.sample

import com.ognev.kotlin.agendacalendarview.models.CalendarEvent
import com.ognev.kotlin.agendacalendarview.models.DayItem
import org.joda.time.LocalDate

class MyCalendarEvent(override val date: LocalDate, override var dayReference: DayItem,
    override var event: Any?) : CalendarEvent
