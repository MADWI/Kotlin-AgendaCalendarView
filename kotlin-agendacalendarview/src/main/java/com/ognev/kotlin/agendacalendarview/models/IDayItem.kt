package com.ognev.kotlin.agendacalendarview.models

import org.joda.time.LocalDate

interface IDayItem {

    var date: LocalDate

    var isSelected: Boolean

    var eventsCount: Int
}
