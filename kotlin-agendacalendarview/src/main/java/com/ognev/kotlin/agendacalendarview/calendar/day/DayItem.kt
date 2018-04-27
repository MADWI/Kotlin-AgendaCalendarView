package com.ognev.kotlin.agendacalendarview.calendar.day

import org.joda.time.LocalDate

data class DayItem(var date: LocalDate, var eventsCount: Int = 0, var isSelected: Boolean = false)
