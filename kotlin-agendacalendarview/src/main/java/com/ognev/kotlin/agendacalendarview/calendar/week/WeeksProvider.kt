package com.ognev.kotlin.agendacalendarview.calendar.week

import com.ognev.kotlin.agendacalendarview.calendar.day.DayItem
import org.joda.time.LocalDate
import org.joda.time.Weeks

class WeeksProvider {

    fun getWeeksBetweenDates(minDate: LocalDate, maxDate: LocalDate): List<WeekItem> {
        val startDate = minDate.withDayOfWeek(1)
        val endDate = maxDate.withDayOfWeek(7)
        val weeks = mutableListOf<WeekItem>()
        val weeksBetween = Weeks.weeksBetween(startDate, endDate).weeks
        for (i in 0..weeksBetween) {
            val weekFirstDate = startDate.plusWeeks(i)
            val dayItems = getDayItems(weekFirstDate)
            val weekItem = WeekItem(dayItems)
            weeks.add(weekItem)
        }
        return weeks
    }

    private fun getDayItems(weekFirstDay: LocalDate): List<DayItem> {
        val dayItems = mutableListOf<DayItem>()
        for (i in 0 until 7) {
            val dayItem =
                DayItem(weekFirstDay.plusDays(i))
            dayItems.add(dayItem)
        }
        return dayItems
    }
}
