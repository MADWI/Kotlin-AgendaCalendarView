package com.ognev.kotlin.agendacalendarview.sample

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.ognev.kotlin.agendacalendarview.CalendarController
import com.ognev.kotlin.agendacalendarview.models.CalendarEvent
import com.ognev.kotlin.agendacalendarview.models.DayItem
import com.ognev.kotlin.agendacalendarview.models.IDayItem
import kotlinx.android.synthetic.main.activity_main.*
import java.util.Calendar
import java.util.Locale

class MainActivity : AppCompatActivity(), CalendarController {

    private val locale = Locale.ENGLISH
    private val maxDate: Calendar by lazy {
        Calendar.getInstance().apply {
            add(Calendar.YEAR, 1)
        }
    }
    private val minDate: Calendar by lazy {
        Calendar.getInstance().apply {
            add(Calendar.MONTH, -10)
            add(Calendar.YEAR, -1)
            set(Calendar.DAY_OF_MONTH, 1)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setupCalendar()
    }

    private fun setupCalendar() {
        val eventRenderer = SampleEventAgendaRenderer(applicationContext)
        agenda_calendar_view.apply {
            init(minDate, maxDate, eventRenderer, getSampleEvents())
            setCallbacks(this@MainActivity)
        }
    }

    private fun getSampleEvents(): MutableList<CalendarEvent> {
        val monthDaysNumber = Calendar.getInstance().getMaximum(Calendar.DAY_OF_MONTH)
        val events: MutableList<CalendarEvent> = ArrayList()
        for (monthDayNumber in 1..monthDaysNumber) {
            val calendarEvent = getCalendarEvent(monthDayNumber)
            events.add(calendarEvent)
        }
        return events
    }

    private fun getCalendarEvent(monthDay: Int): MyCalendarEvent {
        val day = Calendar.getInstance(locale)
        day.timeInMillis = System.currentTimeMillis()
        day.set(Calendar.DAY_OF_MONTH, monthDay)
        val event = SampleEvent(name = "Awesome $monthDay", description = "Event $monthDay")
        val dayItem = DayItem.buildDayItemFromCal(day, this, locale)
        return MyCalendarEvent(day, day, dayItem, event).also {
            it.setEventInstanceDay(day)
        }
    }

    override fun getEmptyEventLayout() = R.layout.view_agenda_empty_event

    override fun getEventLayout() = R.layout.view_agenda_event

    override fun onDaySelected(dayItem: IDayItem) {
    }

    override fun onScrollToDate(calendar: Calendar) {
    }
}
