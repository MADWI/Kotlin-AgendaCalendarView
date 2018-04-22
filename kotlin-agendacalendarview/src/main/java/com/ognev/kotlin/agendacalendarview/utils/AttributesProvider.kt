package com.ognev.kotlin.agendacalendarview.utils

import android.content.Context
import android.support.annotation.ColorRes
import android.support.v4.content.ContextCompat
import android.util.AttributeSet
import com.ognev.kotlin.agendacalendarview.R.color.*
import com.ognev.kotlin.agendacalendarview.R.styleable.*
import com.ognev.kotlin.agendacalendarview.models.ViewAttributes

class AttributesProvider {

    fun getAttributes(context: Context, attributeSet: AttributeSet): ViewAttributes {
        val attrs = context.obtainStyledAttributes(attributeSet, AgendaCalendarView, 0, 0)
        return ViewAttributes(
            daysNamesHeaderColor = attrs.getColor(AgendaCalendarView_daysNamesHeaderColor, getColor(context, android.R.color.white)),
            daysNamesTextColor = attrs.getColor(AgendaCalendarView_daysNamesTextColor, getColor(context, android.R.color.black)),
            monthTextColor = attrs.getColor(AgendaCalendarView_calendarMonthTextColor, getColor(context, theme_text_icons)),
            selectedDayTextColor = attrs.getColor(AgendaCalendarView_calendarSelectedDayTextColor, getColor(context, theme_text_icons)),
            currentDayTextColor = attrs.getColor(AgendaCalendarView_calendarCurrentDayTextColor, getColor(context, calendar_text_current_day)),
            circleBackgroundColor = attrs.getDrawable(AgendaCalendarView_circleBackgroundColor),
            cellDayBackgroundColor = attrs.getColor(AgendaCalendarView_cellDayBackgroundColor, getColor(context, android.R.color.white)),
            cellDayTextColor = attrs.getColor(AgendaCalendarView_cellDayTextColor, getColor(context, android.R.color.white)),
            cellEventMarkColor = attrs.getColor(AgendaCalendarView_cellEventMarkColor, getColor(context, azure)),
            cellEventPlusShowThreshold = attrs.getColor(AgendaCalendarView_cellEventPlusShowThreshold, 4)
        ).also { attrs.recycle() }
    }

    private fun getColor(context: Context, @ColorRes id: Int) = ContextCompat.getColor(context, id)
}
