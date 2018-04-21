package com.ognev.kotlin.agendacalendarview.sample

import com.ognev.kotlin.agendacalendarview.models.CalendarEvent
import com.ognev.kotlin.agendacalendarview.models.DayItem
import org.joda.time.LocalDate

/**
 * Sample Calendar Event
 */
class MyCalendarEvent(override val date: LocalDate, override var dayReference: DayItem, event: SampleEvent?) : CalendarEvent {

    override var event: Any? = event

    override fun hasEvent() = event != null
}
