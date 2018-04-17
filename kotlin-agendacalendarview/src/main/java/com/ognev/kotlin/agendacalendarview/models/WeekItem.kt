package com.ognev.kotlin.agendacalendarview.models

import org.joda.time.LocalDate

data class WeekItem(val date: LocalDate, val dayItems: List<DayItem>)
