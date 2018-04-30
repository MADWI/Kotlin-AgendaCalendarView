package com.ognev.kotlin.agendacalendarview.sample.event

import com.ognev.kotlin.agendacalendarview.calendar.day.DayItem
import com.ognev.kotlin.agendacalendarview.event.CalendarEvent
import org.joda.time.LocalDate

class EventsRepository {

    fun fetchEvents(): MutableList<CalendarEvent> {
        val monthDaysNumber = LocalDate.now().dayOfMonth().maximumValue
        val events = mutableListOf<CalendarEvent>()
        for (monthDayNumber in 1..monthDaysNumber) {
            val date = LocalDate().withDayOfMonth(monthDayNumber)
            if (date.isWeekend()) continue
            events.add(getEventForDate(date))
            events.add(getEventForDate(date))
        }
        return events
    }

    private fun getEventForDate(date: LocalDate): MyCalendarEvent {
        val event = SampleEvent("Awesome ${date.dayOfMonth}", "Event ${date.dayOfMonth}")
        return MyCalendarEvent(DayItem(date), event)
    }

    private fun LocalDate.isWeekend() = dayOfWeek == 6 || dayOfWeek == 7
}
