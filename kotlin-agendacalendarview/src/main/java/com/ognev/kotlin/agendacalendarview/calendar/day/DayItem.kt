package com.ognev.kotlin.agendacalendarview.calendar.day

import android.databinding.BaseObservable
import android.databinding.Bindable
import com.android.databinding.library.baseAdapters.BR
import org.joda.time.LocalDate

data class DayItem(val date: LocalDate, var eventsCount: Int = 0) : BaseObservable() {

    var isSelected: Boolean = false
        private set
        @Bindable get() = selectedBy !is None

    var selectedBy: Selection = None()
        @Bindable get() = field
        set(value) {
            field = value
            notifyPropertyChanged(BR.selectedBy)
            notifyPropertyChanged(BR.selected)
        }
}

sealed class Selection

class None : Selection()

class CalendarClick : Selection()

class AgendaScroll : Selection()
