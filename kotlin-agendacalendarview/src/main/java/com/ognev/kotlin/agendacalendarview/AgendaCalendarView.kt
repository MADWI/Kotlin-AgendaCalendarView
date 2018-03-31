package com.ognev.kotlin.agendacalendarview

import android.animation.ObjectAnimator
import android.content.Context
import android.support.annotation.ColorRes
import android.support.annotation.NonNull
import android.support.v4.content.ContextCompat
import android.support.v4.widget.SwipeRefreshLayout
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.FrameLayout
import com.ognev.kotlin.agendacalendarview.agenda.AgendaAdapter
import com.ognev.kotlin.agendacalendarview.agenda.AgendaView
import com.ognev.kotlin.agendacalendarview.calendar.CalendarView
import com.ognev.kotlin.agendacalendarview.models.AgendaCalendarViewAttributes
import com.ognev.kotlin.agendacalendarview.models.CalendarEvent
import com.ognev.kotlin.agendacalendarview.models.IDayItem
import com.ognev.kotlin.agendacalendarview.models.IWeekItem
import com.ognev.kotlin.agendacalendarview.render.DefaultEventAdapter
import com.ognev.kotlin.agendacalendarview.render.EventAdapter
import com.ognev.kotlin.agendacalendarview.utils.BusProvider
import com.ognev.kotlin.agendacalendarview.utils.Events
import se.emilsjolander.stickylistheaders.StickyListHeadersListView

/**
 * View holding the agenda and calendar view together.
 */
class AgendaCalendarView(context: Context, attrs: AttributeSet) : FrameLayout(context, attrs), StickyListHeadersListView.OnStickyHeaderChangedListener {

    lateinit var agendaView: AgendaView
        private set

    private lateinit var calendarView: CalendarView
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
        val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        inflater.inflate(R.layout.view_agendacalendar, this, true)
        alpha = 0f
    }

    override fun onFinishInflate() {
        super.onFinishInflate()
        calendarView = findViewById(R.id.calendar_view) as CalendarView
        agendaView = findViewById(R.id.agenda_view) as AgendaView
        calendarView.findViewById(R.id.cal_day_names).setBackgroundColor(viewAttributes.headerColor)

        BusProvider.instance.toObserverable()
            .subscribe({ event ->
                if (event is Events.DayClickedEvent) {
                    calendarController!!.onDaySelected((event).day)
                } else if (event is Events.EventsFetched) {
                    ObjectAnimator.ofFloat(this, "alpha", alpha, 1f)
                        .setDuration(500)
                        .start()
                }
            })
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

    fun init(weeks: MutableList<IWeekItem>, days: MutableList<IDayItem>,
        events: MutableList<CalendarEvent>, sampleAgendaAdapter: DefaultEventAdapter) {

        CalendarManager.getInstance(context).loadCal(weeks, days, events)
        calendarView.init(CalendarManager.getInstance(context), viewAttributes)

        // Load agenda events and scroll to current day
        val agendaAdapter = AgendaAdapter()
        agendaView.agendaListView.adapter = agendaAdapter
        agendaView.agendaListView.setOnStickyHeaderChangedListener(this)

        // notify that actually everything is loaded
        BusProvider.instance.send(Events.EventsFetched())
        // add default event renderer
        addEventRenderer(sampleAgendaAdapter)
    }

    private fun addEventRenderer(@NonNull eventAdapter: EventAdapter<*>) {
        val agendaAdapter = agendaView.agendaListView.adapter as AgendaAdapter
        agendaAdapter.addEventRenderer(eventAdapter as EventAdapter<CalendarEvent>)
    }

    fun showProgress() {
        (findViewById(R.id.refresh_layout) as SwipeRefreshLayout).isRefreshing = true
    }

    fun hideProgress() {
        (findViewById(R.id.refresh_layout) as SwipeRefreshLayout).isRefreshing = false
    }

    fun isCalendarLoading(): Boolean {
        return (findViewById(R.id.refresh_layout) as SwipeRefreshLayout).isRefreshing
    }

    private fun View.getColor(@ColorRes id: Int) = ContextCompat.getColor(context, id)
}
