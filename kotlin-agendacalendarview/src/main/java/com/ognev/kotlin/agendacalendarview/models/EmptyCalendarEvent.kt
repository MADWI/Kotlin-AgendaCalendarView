package com.ognev.kotlin.agendacalendarview.models

import java.util.Calendar

open class EmptyCalendarEvent : CalendarEvent {

    override lateinit var startTime: Calendar
    override lateinit var endTime: Calendar
    override lateinit var instanceDay: Calendar
    override lateinit var dayReference: IDayItem
    override lateinit var weekReference: IWeekItem
    override var event: Any? = null

    override fun hasEvent() = false

    override fun setEventInstanceDay(instanceDay: Calendar): EmptyCalendarEvent {
        this.instanceDay = instanceDay
        this.instanceDay.set(Calendar.HOUR, 0)
        this.instanceDay.set(Calendar.MINUTE, 0)
        this.instanceDay.set(Calendar.SECOND, 0)
        this.instanceDay.set(Calendar.MILLISECOND, 0)
        this.instanceDay.set(Calendar.AM_PM, 0)
        return this
    }
}
