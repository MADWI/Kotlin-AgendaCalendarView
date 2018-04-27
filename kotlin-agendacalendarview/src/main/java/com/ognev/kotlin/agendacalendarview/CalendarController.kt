package com.ognev.kotlin.agendacalendarview

import com.ognev.kotlin.agendacalendarview.calendar.day.DayItem
import org.joda.time.LocalDate

interface CalendarController {

    fun getEmptyEventLayout(): Int

    fun getEventLayout() : Int

    fun onDaySelected(dayItem: DayItem)

    fun onScrollToDate(date: LocalDate)
}
