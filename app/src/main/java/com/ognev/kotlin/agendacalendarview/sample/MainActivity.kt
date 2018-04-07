package com.ognev.kotlin.agendacalendarview.sample

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.widget.AdapterView
import android.widget.Toast
import com.ognev.kotlin.agendacalendarview.CalendarController
import com.ognev.kotlin.agendacalendarview.builder.CalendarContentManager
import com.ognev.kotlin.agendacalendarview.models.CalendarEvent
import com.ognev.kotlin.agendacalendarview.models.DayItem
import com.ognev.kotlin.agendacalendarview.models.IDayItem
import kotlinx.android.synthetic.main.activity_main.*
import java.util.Calendar
import java.util.Locale

class MainActivity : AppCompatActivity(), CalendarController {

    private var eventList: MutableList<CalendarEvent> = ArrayList()
    private lateinit var minDate: Calendar
    private lateinit var maxDate: Calendar
    private lateinit var contentManager: CalendarContentManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        minDate = Calendar.getInstance()
        maxDate = Calendar.getInstance()

        minDate.add(Calendar.MONTH, -10)
        minDate.add(Calendar.YEAR, -1)
        minDate.set(Calendar.DAY_OF_MONTH, 1)
        maxDate.add(Calendar.YEAR, 1)


        contentManager = CalendarContentManager(this, agenda_calendar_view, SampleEventAgendaAdapter(applicationContext))

        contentManager.locale = Locale.ENGLISH
        contentManager.setDateRange(minDate, maxDate)

        val maxLength = Calendar.getInstance().getMaximum(Calendar.DAY_OF_MONTH)

        for (i in 1..maxLength) {
            val day = Calendar.getInstance(Locale.ENGLISH)
            day.timeInMillis = System.currentTimeMillis()
            day.set(Calendar.DAY_OF_MONTH, i)

            eventList.add(MyCalendarEvent(day, day,
                DayItem.buildDayItemFromCal(day),
                SampleEvent(name = "Awesome $i", description = "Event $i"))
                .setEventInstanceDay(day))
        }

        contentManager.initialiseCalendar(eventList)
        agenda_calendar_view.agendaView.agendaListView.setOnItemClickListener { _: AdapterView<*>, view: View, position: Int, _: Long ->
            Toast.makeText(view.context, "item: ".plus(position), Toast.LENGTH_SHORT).show()
        }
    }

    override fun getEmptyEventLayout() = R.layout.view_agenda_empty_event

    override fun getEventLayout() = R.layout.view_agenda_event

    override fun onDaySelected(dayItem: IDayItem) {
    }

    override fun onScrollToDate(calendar: Calendar) {
    }
}
