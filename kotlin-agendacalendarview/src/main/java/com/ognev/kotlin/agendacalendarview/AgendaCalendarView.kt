package com.ognev.kotlin.agendacalendarview

import android.content.Context
import android.support.annotation.ColorRes
import android.support.v4.content.ContextCompat
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.FrameLayout
import com.ognev.kotlin.agendacalendarview.agenda.AgendaAdapter
import com.ognev.kotlin.agendacalendarview.agenda.AgendaView
import com.ognev.kotlin.agendacalendarview.calendar.CalendarView
import com.ognev.kotlin.agendacalendarview.models.AgendaCalendarViewAttributes
import com.ognev.kotlin.agendacalendarview.models.CalendarEvent
import com.ognev.kotlin.agendacalendarview.render.CalendarEventRenderer
import com.ognev.kotlin.agendacalendarview.utils.BusProvider
import com.ognev.kotlin.agendacalendarview.utils.DayClickedEvent
import com.ognev.kotlin.agendacalendarview.utils.FetchedEvent
import rx.Subscription
import se.emilsjolander.stickylistheaders.StickyListHeadersListView
import java.util.Calendar

/**
 * View holding the agenda and calendar view together.
 */
class AgendaCalendarView(context: Context, attrs: AttributeSet) : FrameLayout(context, attrs),
    StickyListHeadersListView.OnStickyHeaderChangedListener {

    private lateinit var agendaView: AgendaView
    private lateinit var calendarView: CalendarView
    private var subscription: Subscription? = null
    private var calendarController: CalendarController? = null
    private val viewAttributes: AgendaCalendarViewAttributes

    init {
        val a = context.obtainStyledAttributes(attrs, R.styleable.ColorOptionsView, 0, 0)
        viewAttributes = AgendaCalendarViewAttributes(
            headerColor = a.getColor(R.styleable.ColorOptionsView_calendarHeaderColor, getColor(R.color.theme_primary)),
            monthTextColor = a.getColor(R.styleable.ColorOptionsView_calendarMonthTextColor, getColor(R.color.theme_text_icons)),
            selectedDayTextColor = a.getColor(R.styleable.ColorOptionsView_calendarSelectedDayTextColor, getColor(R.color.theme_text_icons)),
            currentDayTextColor = a.getColor(R.styleable.ColorOptionsView_calendarCurrentDayTextColor, getColor(R.color.calendar_text_current_day)),
            pastDayTextColor = a.getColor(R.styleable.ColorOptionsView_calendarPastDayTextColor, getColor(R.color.theme_light_primary)),
            circleBackgroundColor = a.getDrawable(R.styleable.ColorOptionsView_circleBackgroundColor),
            cellNowadaysDayColor = a.getColor(R.styleable.ColorOptionsView_cellNowadaysDayColor, getColor(R.color.white)),
            cellPastBackgroundColor = a.getColor(R.styleable.ColorOptionsView_cellPastBackgroundColor, getColor(R.color.calendar_past_days_bg)),
            cellEventMarkColor = a.getColor(R.styleable.ColorOptionsView_cellEventMarkColor, getColor(R.color.azure)),
            cellEventPlusShowThreshold = a.getColor(R.styleable.ColorOptionsView_cellEventPlusShowThreshold, 4)
        )
        LayoutInflater.from(context).inflate(R.layout.view_agendacalendar, this, true)
    }

    override fun onFinishInflate() {
        super.onFinishInflate()
        calendarView = findViewById(R.id.calendar_view)
        agendaView = findViewById(R.id.agenda_view)
        calendarView.findViewById<View>(R.id.cal_day_names).setBackgroundColor(viewAttributes.headerColor)

        subscription = BusProvider.instance.toObservable()
            .subscribe { event ->
                if (event is DayClickedEvent) {
                    calendarController!!.onDaySelected(event.day)
                }
            }
    }

    override
    fun onStickyHeaderChanged(stickyListHeadersListView: StickyListHeadersListView, header: View, position: Int, headerId: Long) {
        if (CalendarManager.instance!!.events.size > 0) {
            val event = CalendarManager.instance!!.events[position]
            calendarView.scrollToDate(event)
            calendarController!!.onScrollToDate(event.instanceDay)
        }
    }

    fun setCallbacks(calendarController: CalendarController) {
        this.calendarController = calendarController
    }

    fun init(minDate: Calendar, maxDate: Calendar, eventRenderer: CalendarEventRenderer<CalendarEvent>, events: List<CalendarEvent>) {
        CalendarManager.getInstance(context).apply {
            buildCal(minDate, maxDate)
            fillCalendarEventsWithEmptyEvents(events)
        }

        // Load agenda events and scroll to current day
        val agendaAdapter = AgendaAdapter()
        agendaView.agendaListView.adapter = agendaAdapter
        agendaView.agendaListView.setOnStickyHeaderChangedListener(this)

        agendaAdapter.setEvents(CalendarManager.getInstance(context).events)
        agendaAdapter.setEventAdapter(eventRenderer)

        calendarView.init(CalendarManager.getInstance(context), viewAttributes)

        // notify that actually everything is loaded
        BusProvider.instance.send(FetchedEvent())
    }

    private fun View.getColor(@ColorRes id: Int) = ContextCompat.getColor(context, id)

    fun dispose() {
        subscription?.unsubscribe()
        calendarView.dispose()
        agendaView.dispose()
    }
}
