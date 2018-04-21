package com.ognev.kotlin.agendacalendarview.utils

import com.ognev.kotlin.agendacalendarview.models.CalendarEvent
import com.ognev.kotlin.agendacalendarview.models.DayItem
import com.ognev.kotlin.agendacalendarview.models.EmptyCalendarEvent
import com.ognev.kotlin.agendacalendarview.models.WeekItem
import org.joda.time.LocalDate

class EventsProvider {

    fun getAgendaEvents(events: List<CalendarEvent>, weeks: List<WeekItem>) =
        mutableListOf<CalendarEvent>().apply {
            for (week in weeks) {
                for (dayItem in week.dayItems) {
                    events.filter { dayItem.date.compareTo(it.date) == 0 }
                        .withEmpty { this.add(getEmptyCalendarEvent(dayItem)) }
                        .forEach { this.add(getCalendarEvent(it, dayItem)) }
                }
            }
        }

    private fun getEmptyCalendarEvent(dayItem: DayItem) =
        EmptyCalendarEvent(LocalDate(dayItem.date)).apply {
            dayReference = dayItem
        }

    private fun getCalendarEvent(event: CalendarEvent, dayItem: DayItem): CalendarEvent {
        event.dayReference = dayItem
        if (event.hasEvent()) {
            dayItem.eventsCount += 1
        }
        return event
    }

    private inline fun <E : Any, T : Collection<E>> T.withEmpty(action: () -> Unit): T {
        if (this.isEmpty()) {
            action()
        }
        return this
    }
}
