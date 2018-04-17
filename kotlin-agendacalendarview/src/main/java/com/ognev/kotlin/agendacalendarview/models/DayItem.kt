package com.ognev.kotlin.agendacalendarview.models

import org.joda.time.LocalDate

/**
 * Day model class.
 */
class DayItem(override var date: LocalDate) : IDayItem {

    override var eventsCount: Int = 0
    override var isSelected: Boolean = false
}
