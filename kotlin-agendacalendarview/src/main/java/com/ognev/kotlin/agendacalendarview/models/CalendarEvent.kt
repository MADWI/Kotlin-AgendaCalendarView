package com.ognev.kotlin.agendacalendarview.models

import java.util.Calendar

interface CalendarEvent {

    var event: Any?

    var startTime: Calendar

    var endTime: Calendar

    val instanceDay: Calendar

    var dayReference: IDayItem

    var weekReference: IWeekItem

    fun hasEvent(): Boolean

    fun setEventInstanceDay(instanceDay: Calendar) : CalendarEvent
}
