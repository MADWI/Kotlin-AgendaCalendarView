package com.ognev.kotlin.agendacalendarview.calendar.week

import android.databinding.Observable
import com.ognev.kotlin.agendacalendarview.calendar.day.AgendaScroll
import com.ognev.kotlin.agendacalendarview.calendar.day.CalendarClick
import com.ognev.kotlin.agendacalendarview.calendar.day.DayItem
import com.ognev.kotlin.agendacalendarview.calendar.day.None
import org.joda.time.LocalDate

class WeeksViewModel(private val weekItems: List<WeekItem>, private val onWeekIndexChanged: (Int) -> Unit,
    private val onSelectedDayChanged: (DayItem) -> Unit) {

    private var selectedDay: DayItem? = null

    init {
        weekItems.flatMap { it.days }
            .forEach { setupPropertyChangeCallback(it) }
    }

    private fun setupPropertyChangeCallback(dayItem: DayItem) =
        dayItem.addOnPropertyChangedCallback { setupSelection(it) }

    private fun unSelectPreviousDay() {
        selectedDay?.apply {
            if (isSelected) {
                selectedBy = None()
            }
        }
    }

    private fun setupSelection(dayItem: DayItem) {
        if (dayItem == selectedDay) {
            return
        }
        unSelectPreviousDay()
        if (dayItem.selectedBy !is None) {
            selectedDay = dayItem
            when (dayItem.selectedBy) {
                is CalendarClick -> onSelectedDayChanged(dayItem)
                is AgendaScroll -> onWeekIndexChanged(getWeekIndex(dayItem))
            }
        }
    }

    private fun getWeekIndex(dayItem: DayItem) =
        weekItems.indexOfFirst { it.days[0].date.isSameWeek(dayItem.date) }

    private inline fun <reified T : Observable> T.addOnPropertyChangedCallback(crossinline callback: (T) -> Unit) =
        addOnPropertyChangedCallback(object : Observable.OnPropertyChangedCallback() {
            override fun onPropertyChanged(sender: Observable, propertyId: Int) = callback(sender as T)
        })

    private fun LocalDate.isSameWeek(date: LocalDate): Boolean {
        return this.weekOfWeekyear == date.weekOfWeekyear
    }
}
