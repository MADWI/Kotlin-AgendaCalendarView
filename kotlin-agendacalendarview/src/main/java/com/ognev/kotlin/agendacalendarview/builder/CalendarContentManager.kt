package com.ognev.kotlin.agendacalendarview.builder

import com.ognev.kotlin.agendacalendarview.AgendaCalendarView
import com.ognev.kotlin.agendacalendarview.CalendarController
import com.ognev.kotlin.agendacalendarview.CalendarManager
import com.ognev.kotlin.agendacalendarview.models.CalendarEvent
import com.ognev.kotlin.agendacalendarview.render.DefaultEventAdapter
import java.util.Calendar
import java.util.Locale

/**
 * Created by ognev on 7/5/17.
 */
class CalendarContentManager(private val calendarController: CalendarController,
    private val agendaCalendarView: AgendaCalendarView,
    private val sampleAgendaAdapter: DefaultEventAdapter) {

    private var minDate: Calendar? = null
    private var maxDate: Calendar? = null
    var locale: Locale = Locale.ENGLISH
    private lateinit var calendarManager: CalendarManager

    fun setDateRange(minDate: Calendar, maxDate: Calendar) {
        this.minDate = minDate
        this.maxDate = maxDate
        initManager()
    }

    private fun initManager() {
        calendarManager = CalendarManager.getInstance(agendaCalendarView.context)
        calendarManager.locale = locale
        calendarManager.buildCal(minDate, maxDate)
    }

    fun initialiseCalendar(eventList: MutableList<CalendarEvent>) {
        calendarManager.loadInitialEvents(eventList)
        val readyEvents = calendarManager.events
        val readyDays = calendarManager.days
        val readyWeeks = calendarManager.weeks
        agendaCalendarView.init(readyWeeks, readyDays, readyEvents, sampleAgendaAdapter)
        agendaCalendarView.setCallbacks(calendarController)
    }
}
