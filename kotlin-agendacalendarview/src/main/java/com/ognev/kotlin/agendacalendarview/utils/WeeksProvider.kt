package com.ognev.kotlin.agendacalendarview.utils

import com.ognev.kotlin.agendacalendarview.models.DayItem
import com.ognev.kotlin.agendacalendarview.models.WeekItem
import org.joda.time.Days
import org.joda.time.LocalDate

class WeeksProvider {

    fun getWeeksBetweenDates(startDate: LocalDate, endDate: LocalDate): MutableList<WeekItem> {
        val weeks = mutableListOf<WeekItem>()
        val daysBetween = Days.daysBetween(startDate, endDate).days
        for (i in 0..daysBetween) {
            val weekFirstDate = startDate.plusWeeks(i)
            val dayItems = getDayItems(weekFirstDate)
            val weekItem = WeekItem(weekFirstDate, dayItems)
            weeks.add(weekItem)
        }
        return weeks
    }

    private fun getDayItems(weekFirstDay: LocalDate): List<DayItem> {
        val dayItems = mutableListOf<DayItem>()
        for (i in 0 until 7) {
            val dayItem = DayItem(weekFirstDay.plusDays(i))
            dayItems.add(dayItem)
        }
        return dayItems
    }
}
