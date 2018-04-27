package com.ognev.kotlin.agendacalendarview.sample.event

import com.ognev.kotlin.agendacalendarview.calendar.day.DayItem
import com.ognev.kotlin.agendacalendarview.event.CalendarEvent
import org.joda.time.LocalDate

class EventsRepository {

    fun fetchEvents(): MutableList<CalendarEvent> {
        val monthDaysNumber = LocalDate.now().dayOfMonth().maximumValue
        val events: MutableList<CalendarEvent> = ArrayList()
        for (monthDayNumber in 1..monthDaysNumber) {
            val calendarEvent = getCalendarEvent(monthDayNumber)
            events.add(calendarEvent)
            events.add(getCalendarEvent(monthDayNumber))
        }
        return events
    }

    private fun getCalendarEvent(monthDay: Int): MyCalendarEvent {
        val event = SampleEvent(name = "Awesome $monthDay", description = "Event $monthDay")
        val date = LocalDate().withDayOfMonth(monthDay)
        return MyCalendarEvent(DayItem(date), event)
    }
}
