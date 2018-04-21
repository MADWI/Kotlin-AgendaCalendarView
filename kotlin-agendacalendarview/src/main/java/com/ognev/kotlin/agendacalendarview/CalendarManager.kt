package com.ognev.kotlin.agendacalendarview

import com.ognev.kotlin.agendacalendarview.models.CalendarEvent
import com.ognev.kotlin.agendacalendarview.models.DayItem
import com.ognev.kotlin.agendacalendarview.models.EmptyCalendarEvent
import com.ognev.kotlin.agendacalendarview.models.WeekItem
import org.joda.time.LocalDate
import java.util.ArrayList

/**
 * This class manages information about the calendar. (Events, weather info...)
 * Holds reference to the days list of the calendar.
 * As the app is using several views, we want to keep everything in one place.
 */
class CalendarManager {

    /**
     * List of events instances
     */
    var events: MutableList<CalendarEvent> = ArrayList()
        private set

    fun fillCalendarEventsWithEmptyEvents(events: List<CalendarEvent>, weeks: List<WeekItem>) {
        for (weekItem in weeks) {
            for (dayItem in weekItem.dayItems) {
                events.filter { dayItem.date.compareTo(it.date) == 0 }
                    .withEmpty { this.events.add(getEmptyCalendarEvent(dayItem, weekItem)) }
                    .forEach { this.events.add(getCalendarEvent(it, dayItem, weekItem)) }
            }
        }
    }

    private fun getEmptyCalendarEvent(dayItem: DayItem, weekItem: WeekItem) =
        EmptyCalendarEvent(LocalDate(dayItem.date)).apply {
            dayReference = dayItem
            weekReference = weekItem
        }

    private fun getCalendarEvent(event: CalendarEvent, dayItem: DayItem, weekItem: WeekItem): CalendarEvent {
        event.dayReference = dayItem
        event.weekReference = weekItem
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

    companion object {
        val instance: CalendarManager = CalendarManager()
    }
}
