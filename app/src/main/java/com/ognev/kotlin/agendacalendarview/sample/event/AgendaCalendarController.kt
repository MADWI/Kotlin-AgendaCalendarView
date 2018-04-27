package com.ognev.kotlin.agendacalendarview.sample.event

import com.ognev.kotlin.agendacalendarview.CalendarController
import com.ognev.kotlin.agendacalendarview.calendar.day.DayItem
import com.ognev.kotlin.agendacalendarview.sample.R
import org.joda.time.LocalDate

class AgendaCalendarController: CalendarController {

    override fun getEmptyEventLayout() = R.layout.view_agenda_empty_event

    override fun getEventLayout() = R.layout.view_agenda_event

    override fun onDaySelected(dayItem: DayItem) {
    }

    override fun onScrollToDate(date: LocalDate) {
    }
}
