package com.ognev.kotlin.agendacalendarview

import android.content.Context
import com.ognev.kotlin.agendacalendarview.models.CalendarEvent
import com.ognev.kotlin.agendacalendarview.models.DayItem
import com.ognev.kotlin.agendacalendarview.models.EmptyCalendarEvent
import com.ognev.kotlin.agendacalendarview.models.IWeekItem
import com.ognev.kotlin.agendacalendarview.models.WeekItem
import org.joda.time.LocalDate
import java.text.SimpleDateFormat
import java.util.ArrayList
import java.util.Calendar
import java.util.Locale

/**
 * This class manages information about the calendar. (Events, weather info...)
 * Holds reference to the days list of the calendar.
 * As the app is using several views, we want to keep everything in one place.
 */
class CalendarManager(context: Context, var locale: Locale = Locale.ENGLISH) {

    private val monthHalfNameFormat = SimpleDateFormat(context.getString(R.string.month_half_name_format), locale)

    /**
     * List of days used by the calendar
     */
    private var days: MutableList<DayItem> = ArrayList()
    /**
     * List of weeks used by the calendar
     */
    var weeks: MutableList<IWeekItem> = ArrayList()
        private set
    /**
     * List of events instances
     */
    var events: MutableList<CalendarEvent> = ArrayList()
        private set

    fun buildCal(minDate: Calendar?, maxDate: Calendar?) {
        if (minDate == null || maxDate == null) {
            throw IllegalArgumentException(
                "minDate and maxDate must be non-null.")
        }
        if (minDate.after(maxDate)) {
            throw IllegalArgumentException(
                "minDate must be before maxDate.")
        }

        days.clear()
        weeks.clear()
        events.clear()

        val mMinCal = Calendar.getInstance(locale)
        val mMaxCal = Calendar.getInstance(locale)
        val mWeekCounter = Calendar.getInstance(locale)

        mMinCal.time = minDate.time
        mMaxCal.time = maxDate.time

        // maxDate is exclusive, here we bump back to the previous day, as maxDate if December 1st, 2020,
        // we don't include that month in our list
        mMaxCal.add(Calendar.MINUTE, -1)

        // Now iterate we iterate between mMinCal and mMaxCal so we build our list of weeks
        mWeekCounter.time = mMinCal.time
        val maxMonth = mMaxCal.get(Calendar.MONTH)
        val maxYear = mMaxCal.get(Calendar.YEAR)

        var currentMonth = mWeekCounter.get(Calendar.MONTH)
        var currentYear = mWeekCounter.get(Calendar.YEAR)

        // Loop through the weeks
        while ((currentMonth <= maxMonth // Up to, including the month.
                || currentYear < maxYear) // Up to the year.
            && currentYear < maxYear + 1) { // But not > next yr.

            val date = mWeekCounter.time
            // Build our week list
            val currentWeekOfYear = mWeekCounter.get(Calendar.WEEK_OF_YEAR)

            val weekItem = WeekItem(currentWeekOfYear, currentYear, date, monthHalfNameFormat.format(date), currentMonth)
            val dayItems = getDayCells(mWeekCounter) // gather days for the built week
            weekItem.dayItems = (dayItems)
            weeks.add(weekItem)

            //      Log.d(LOG_TAG, String.format("Adding week: %s", weekItem));

            mWeekCounter.add(Calendar.WEEK_OF_YEAR, 1)

            currentMonth = mWeekCounter.get(Calendar.MONTH)
            currentYear = mWeekCounter.get(Calendar.YEAR)
        }
    }

    fun fillCalendarEventsWithEmptyEvents(events: List<CalendarEvent>) {
        for (weekItem in weeks) {
            for (dayItem in weekItem.dayItems) {
                events.filter { dayItem.date.compareTo(it.date) == 0 }
                    .withEmpty { this.events.add(getEmptyCalendarEvent(dayItem, weekItem)) }
                    .forEach { this.events.add(getCalendarEvent(it, dayItem, weekItem)) }
            }
        }
    }

    private fun getEmptyCalendarEvent(dayItem: DayItem, weekItem: IWeekItem) =
        EmptyCalendarEvent(LocalDate(dayItem.date)).apply {
            dayReference = dayItem
            weekReference = weekItem
        }

    private fun getCalendarEvent(event: CalendarEvent, dayItem: DayItem, weekItem: IWeekItem): CalendarEvent {
        event.dayReference = dayItem
        event.weekReference = weekItem
        if (event.hasEvent()) {
            dayItem.eventsCount += 1
        }
        return event
    }

    private fun getDayCells(startCal: Calendar): List<DayItem> {
        val cal = Calendar.getInstance(locale)
        cal.time = startCal.time
        val dayItems = ArrayList<DayItem>()

        val firstDayOfWeek = cal.get(Calendar.DAY_OF_WEEK)
        var offset = cal.firstDayOfWeek - firstDayOfWeek
        if (offset > 0) {
            offset -= 7
        }
        cal.add(Calendar.DATE, offset)

        for (c in 0..6) {
            val dayItem = DayItem(LocalDate.fromCalendarFields(cal))
            dayItems.add(dayItem)
            cal.add(Calendar.DATE, 1)
        }

        days.addAll(dayItems)
        return dayItems
    }

    private inline fun <E : Any, T : Collection<E>> T.withEmpty(action: () -> Unit): T {
        if (this.isEmpty()) {
            action()
        }
        return this
    }

    companion object {
        var instance: CalendarManager? = null
            private set

        fun getInstance(context: Context): CalendarManager {
            if (instance == null) {
                instance = CalendarManager(context.applicationContext)
            }
            return instance as CalendarManager
        }
    }
}
