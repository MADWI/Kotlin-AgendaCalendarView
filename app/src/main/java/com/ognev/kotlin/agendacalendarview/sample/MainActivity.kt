package com.ognev.kotlin.agendacalendarview.sample

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.ognev.kotlin.agendacalendarview.sample.event.EventsRepository
import com.ognev.kotlin.agendacalendarview.sample.event.SampleEventAgendaRenderer
import kotlinx.android.synthetic.main.activity_main.*
import org.joda.time.LocalDate
import org.joda.time.format.DateTimeFormat

class MainActivity : AppCompatActivity() {

    private val minDate = LocalDate.now().withDayOfMonth(1)
    private val maxDate = minDate.plusMonths(5)
    private val eventsRepository = EventsRepository()
    private val dateFormatter = DateTimeFormat.forPattern("MMMM yyyy")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setupCalendar()
    }

    private fun setupCalendar() {
        val eventRenderer = SampleEventAgendaRenderer(applicationContext)
        val events = eventsRepository.fetchEvents()
        agendaCalendarView.apply {
            init(minDate, maxDate, eventRenderer, events)
            setDayChangedListener { setToolbarDate(it.date) }
        }
    }

    private fun setToolbarDate(date: LocalDate) {
        supportActionBar?.title = dateFormatter.print(date)
    }

    override fun onDestroy() {
        super.onDestroy()
        agendaCalendarView.dispose()
    }
}
