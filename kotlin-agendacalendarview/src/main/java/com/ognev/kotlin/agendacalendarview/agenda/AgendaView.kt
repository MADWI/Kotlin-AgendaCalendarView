package com.ognev.kotlin.agendacalendarview.agenda

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.MotionEvent
import android.widget.FrameLayout
import com.ognev.kotlin.agendacalendarview.R
import com.ognev.kotlin.agendacalendarview.models.CalendarEvent
import com.ognev.kotlin.agendacalendarview.utils.AgendaListViewTouched
import com.ognev.kotlin.agendacalendarview.utils.BusProvider
import com.ognev.kotlin.agendacalendarview.utils.DayClicked
import com.ognev.kotlin.agendacalendarview.utils.Event
import org.joda.time.LocalDate
import rx.Subscription

class AgendaView(context: Context, attrs: AttributeSet) : FrameLayout(context, attrs) {

    lateinit var agendaListView: AgendaListView
        private set
    private var subscription: Subscription? = null
    lateinit var events: List<CalendarEvent>

    init {
        LayoutInflater.from(context).inflate(R.layout.view_agenda, this, true)
    }

    override fun onFinishInflate() {
        super.onFinishInflate()
        agendaListView = findViewById(R.id.agenda_listview)
        subscription = BusProvider.instance.toObservable()
            .subscribe { event -> handleEvent(event) }
    }

    override
    fun dispatchTouchEvent(event: MotionEvent): Boolean {
        if (event.action == MotionEvent.ACTION_MOVE) {
            BusProvider.instance.send(AgendaListViewTouched())
        }
        return super.dispatchTouchEvent(event)
    }

    fun dispose() {
        subscription?.unsubscribe()
    }

    private fun handleEvent(event: Event) {
        when (event) {
            is DayClicked -> scrollToDate(event.day.date)
        }
    }

    private fun scrollToDate(date: LocalDate) {
        val selection = events.indexOfFirst { date.compareTo(it.date) == 0 }
        post { agendaListView.setSelection(selection) }
    }
}
