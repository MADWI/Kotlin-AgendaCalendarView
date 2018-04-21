package com.ognev.kotlin.agendacalendarview.models

import org.joda.time.LocalDate

open class EmptyCalendarEvent(override val date: LocalDate) : CalendarEvent {

    override lateinit var dayReference: DayItem
    override var event: Any? = null
    override fun hasEvent() = false
}
