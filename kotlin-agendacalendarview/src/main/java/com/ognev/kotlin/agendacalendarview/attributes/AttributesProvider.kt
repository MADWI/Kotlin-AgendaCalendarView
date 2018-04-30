package com.ognev.kotlin.agendacalendarview.attributes

import android.content.Context
import android.support.annotation.ColorRes
import android.support.v4.content.ContextCompat
import android.util.AttributeSet
import com.ognev.kotlin.agendacalendarview.R.color.azure
import com.ognev.kotlin.agendacalendarview.R.color.calendar_text_current_day
import com.ognev.kotlin.agendacalendarview.R.color.theme_text_icons
import com.ognev.kotlin.agendacalendarview.R.styleable.AgendaCalendarView
import com.ognev.kotlin.agendacalendarview.R.styleable.AgendaCalendarView_calendarColor
import com.ognev.kotlin.agendacalendarview.R.styleable.AgendaCalendarView_calendarCurrentDayTextColor
import com.ognev.kotlin.agendacalendarview.R.styleable.AgendaCalendarView_calendarMonthTextColor
import com.ognev.kotlin.agendacalendarview.R.styleable.AgendaCalendarView_calendarSelectedDayTextColor
import com.ognev.kotlin.agendacalendarview.R.styleable.AgendaCalendarView_cellDayTextColor
import com.ognev.kotlin.agendacalendarview.R.styleable.AgendaCalendarView_cellEventMarkColor
import com.ognev.kotlin.agendacalendarview.R.styleable.AgendaCalendarView_cellEventPlusShowThreshold
import com.ognev.kotlin.agendacalendarview.R.styleable.AgendaCalendarView_daysNamesHeaderColor
import com.ognev.kotlin.agendacalendarview.R.styleable.AgendaCalendarView_daysNamesTextColor
import com.ognev.kotlin.agendacalendarview.R.styleable.AgendaCalendarView_selectedDayBackground

class AttributesProvider {

    fun getAttributes(context: Context, attributeSet: AttributeSet): ViewAttributes {
        val attrs = context.obtainStyledAttributes(attributeSet, AgendaCalendarView, 0, 0)
        return ViewAttributes(
            calendarColor = attrs.getColor(AgendaCalendarView_calendarColor, getColor(context, android.R.color.white)),
            cellDayTextColor = attrs.getColor(AgendaCalendarView_cellDayTextColor, getColor(context, android.R.color.white)),
            cellEventMarkColor = attrs.getColor(AgendaCalendarView_cellEventMarkColor, getColor(context, azure)),
            cellEventPlusShowThreshold = attrs.getColor(AgendaCalendarView_cellEventPlusShowThreshold, 4),
            daysNamesTextColor = attrs.getColor(AgendaCalendarView_daysNamesTextColor, getColor(context, android.R.color.black)),
            daysNamesHeaderColor = attrs.getColor(AgendaCalendarView_daysNamesHeaderColor, getColor(context, android.R.color.white)),
            monthTextColor = attrs.getColor(AgendaCalendarView_calendarMonthTextColor, getColor(context, theme_text_icons)),
            currentDayTextColor = attrs.getColor(AgendaCalendarView_calendarCurrentDayTextColor, getColor(context, calendar_text_current_day)),
            selectedDayTextColor = attrs.getColor(AgendaCalendarView_calendarSelectedDayTextColor, getColor(context, theme_text_icons)),
            selectedDayBackground = attrs.getDrawable(AgendaCalendarView_selectedDayBackground)
        ).also { attrs.recycle() }
    }

    private fun getColor(context: Context, @ColorRes id: Int) = ContextCompat.getColor(context, id)
}
