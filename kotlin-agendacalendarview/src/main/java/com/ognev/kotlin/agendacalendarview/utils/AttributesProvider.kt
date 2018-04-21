package com.ognev.kotlin.agendacalendarview.utils

import android.content.Context
import android.support.annotation.ColorRes
import android.support.v4.content.ContextCompat
import android.util.AttributeSet
import com.ognev.kotlin.agendacalendarview.R.color.*
import com.ognev.kotlin.agendacalendarview.R.styleable.*
import com.ognev.kotlin.agendacalendarview.models.AgendaCalendarViewAttributes

class AttributesProvider {

    fun getAttributes(context: Context, attributeSet: AttributeSet): AgendaCalendarViewAttributes {
        val attrs = context.obtainStyledAttributes(attributeSet, AgendaCalendarView, 0, 0)
        return AgendaCalendarViewAttributes(
            headerColor = attrs.getColor(AgendaCalendarView_calendarHeaderColor, getColor(context, theme_primary)),
            monthTextColor = attrs.getColor(AgendaCalendarView_calendarMonthTextColor, getColor(context, theme_text_icons)),
            selectedDayTextColor = attrs.getColor(AgendaCalendarView_calendarSelectedDayTextColor, getColor(context, theme_text_icons)),
            currentDayTextColor = attrs.getColor(AgendaCalendarView_calendarCurrentDayTextColor, getColor(context, calendar_text_current_day)),
            pastDayTextColor = attrs.getColor(AgendaCalendarView_calendarPastDayTextColor, getColor(context, theme_light_primary)),
            circleBackgroundColor = attrs.getDrawable(AgendaCalendarView_circleBackgroundColor),
            cellNowadaysDayColor = attrs.getColor(AgendaCalendarView_cellNowadaysDayColor, getColor(context, white)),
            cellPastBackgroundColor = attrs.getColor(AgendaCalendarView_cellPastBackgroundColor, getColor(context, calendar_past_days_bg)),
            cellEventMarkColor = attrs.getColor(AgendaCalendarView_cellEventMarkColor, getColor(context, azure)),
            cellEventPlusShowThreshold = attrs.getColor(AgendaCalendarView_cellEventPlusShowThreshold, 4)
        ).also { attrs.recycle() }
    }

    private fun getColor(context: Context, @ColorRes id: Int) = ContextCompat.getColor(context, id)
}
