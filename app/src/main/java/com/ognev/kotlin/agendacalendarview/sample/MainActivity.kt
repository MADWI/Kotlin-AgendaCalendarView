package com.ognev.kotlin.agendacalendarview.sample

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.ognev.kotlin.agendacalendarview.CalendarController
import com.ognev.kotlin.agendacalendarview.calendar.day.DayItem
import kotlinx.android.synthetic.main.activity_main.*
import org.joda.time.LocalDate

class MainActivity : AppCompatActivity(), CalendarController {

    private val minDate = LocalDate.now().withDayOfMonth(1)
    private val maxDate = minDate.plusMonths(5)
    private val eventsRepository = EventsRepository()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setupCalendar()
    }

    private fun setupCalendar() {
        val eventRenderer = SampleEventAgendaRenderer(applicationContext)
        val events = eventsRepository.fetchEvents()
        agenda_calendar_view.apply {
            init(minDate, maxDate, eventRenderer, events)
            setCallbacks(this@MainActivity)
        }
    }

    override fun getEmptyEventLayout() = R.layout.view_agenda_empty_event

    override fun getEventLayout() = R.layout.view_agenda_event

    override fun onDaySelected(dayItem: DayItem) {
    }

    override fun onScrollToDate(date: LocalDate) {
    }

    override fun onDestroy() {
        super.onDestroy()
        agenda_calendar_view.dispose()
    }
}
