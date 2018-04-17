package com.ognev.kotlin.agendacalendarview

import com.ognev.kotlin.agendacalendarview.models.IDayItem
import org.joda.time.LocalDate

interface CalendarController {

    fun getEmptyEventLayout(): Int

    fun getEventLayout() : Int

    fun onDaySelected(dayItem: IDayItem)

    fun onScrollToDate(date: LocalDate)
}
