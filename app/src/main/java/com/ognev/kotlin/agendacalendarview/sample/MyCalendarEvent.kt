package com.ognev.kotlin.agendacalendarview.sample

import com.ognev.kotlin.agendacalendarview.models.CalendarEvent
import com.ognev.kotlin.agendacalendarview.models.IDayItem
import com.ognev.kotlin.agendacalendarview.models.IWeekItem
import java.util.Calendar

/**
 * Sample Calendar Event
 */
class MyCalendarEvent(override var startTime: Calendar, override var endTime: Calendar,
    override var dayReference: IDayItem, event: SampleEvent?) : CalendarEvent {

    override var event: Any? = event
    override lateinit var instanceDay: Calendar
    override lateinit var weekReference: IWeekItem

    override fun setEventInstanceDay(instanceDay: Calendar): MyCalendarEvent {
        this.instanceDay = instanceDay
        this.instanceDay.set(Calendar.HOUR, 0)
        this.instanceDay.set(Calendar.MINUTE, 0)
        this.instanceDay.set(Calendar.SECOND, 0)
        this.instanceDay.set(Calendar.MILLISECOND, 0)
        this.instanceDay.set(Calendar.AM_PM, 0)
        return this
    }

    override fun hasEvent() = event != null
}
