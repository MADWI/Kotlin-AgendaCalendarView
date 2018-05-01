package com.ognev.kotlin.agendacalendarview.calendar.week

import android.databinding.Observable
import com.ognev.kotlin.agendacalendarview.calendar.day.DayItem

class WeeksViewModel(weekItems: List<WeekItem>, onDaySelected: (DayItem) -> Unit) {

    init {
        weekItems.flatMap { it.days }.forEach {
            setupPropertyChangeCallback(it, onDaySelected)
        }
    }

    private fun setupPropertyChangeCallback(dayItem: DayItem, onDaySelected: (DayItem) -> Unit) =
        dayItem.addOnPropertyChangedCallback {
            if (it.isSelected) {
                onDaySelected(it)
            }
        }

    private inline fun <reified T : Observable> T.addOnPropertyChangedCallback(crossinline callback: (T) -> Unit) =
        addOnPropertyChangedCallback(object : Observable.OnPropertyChangedCallback() {
            override fun onPropertyChanged(sender: Observable, propertyId: Int) = callback(sender as T)
        })
}
