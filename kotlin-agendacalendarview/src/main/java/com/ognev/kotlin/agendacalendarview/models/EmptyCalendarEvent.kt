package com.ognev.kotlin.agendacalendarview.models

import com.ognev.kotlin.agendacalendarview.calendar.day.DayItem
import org.joda.time.LocalDate

open class EmptyCalendarEvent(override val date: LocalDate) : CalendarEvent {

    override lateinit var day: DayItem
    override var event: Any? = null
}
