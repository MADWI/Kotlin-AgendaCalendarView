package com.ognev.kotlin.agendacalendarview.models

import android.graphics.drawable.Drawable

data class AgendaCalendarViewAttributes(val headerColor: Int, val monthTextColor: Int,
    val selectedDayTextColor: Int, val currentDayTextColor: Int,
    val pastDayTextColor: Int, val circleBackgroundColor: Drawable?,
    val cellNowadaysDayColor: Int, val cellPastBackgroundColor: Int,
    val cellEventMarkColor: Int, val cellEventPlusShowThreshold: Int)
