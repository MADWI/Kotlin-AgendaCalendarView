package com.ognev.kotlin.agendacalendarview.models

import org.joda.time.LocalDate

interface CalendarEvent {

    var event: Any?

    val date: LocalDate

    var dayReference: IDayItem

    var weekReference: IWeekItem

    //TODO refactor
    fun hasEvent(): Boolean
}
