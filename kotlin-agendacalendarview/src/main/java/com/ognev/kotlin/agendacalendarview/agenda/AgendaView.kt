package com.ognev.kotlin.agendacalendarview.agenda

import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import com.ognev.kotlin.agendacalendarview.models.CalendarEvent
import com.ognev.kotlin.agendacalendarview.utils.AgendaListViewTouched
import com.ognev.kotlin.agendacalendarview.utils.BusProvider
import com.ognev.kotlin.agendacalendarview.utils.DayClicked
import com.ognev.kotlin.agendacalendarview.utils.Event
import org.joda.time.LocalDate
import rx.Subscription
import se.emilsjolander.stickylistheaders.StickyListHeadersListView

class AgendaView(context: Context, attrs: AttributeSet) : StickyListHeadersListView(context, attrs) {

    lateinit var events: List<CalendarEvent>
    private var subscription: Subscription? = null

    override fun onFinishInflate() {
        super.onFinishInflate()
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
        post { setSelection(selection) }
    }
}
