package com.ognev.kotlin.agendacalendarview.agenda

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.MotionEvent
import android.widget.FrameLayout
import com.ognev.kotlin.agendacalendarview.R
import com.ognev.kotlin.agendacalendarview.utils.AgendaListViewTouched
import com.ognev.kotlin.agendacalendarview.utils.BusProvider
import com.ognev.kotlin.agendacalendarview.utils.DayClicked
import com.ognev.kotlin.agendacalendarview.utils.Event
import rx.Subscription

class AgendaView : FrameLayout {

    lateinit var agendaListView: AgendaListView
        private set
    private var subscription: Subscription? = null

    constructor(context: Context) : super(context)

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        inflater.inflate(R.layout.view_agenda, this, true)
    }

    override fun onFinishInflate() {
        super.onFinishInflate()
        agendaListView = findViewById(R.id.agenda_listview)

        subscription = BusProvider.instance.toObservable()
            .subscribe { event -> handleEvent(event) }
    }

    private fun handleEvent(event: Event) {
        when (event) {
            is DayClicked -> agendaListView.scrollToCurrentDate(event.calendar)
        }
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
}
