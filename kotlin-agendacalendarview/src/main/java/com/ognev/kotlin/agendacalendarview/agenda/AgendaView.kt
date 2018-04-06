package com.ognev.kotlin.agendacalendarview.agenda

import android.animation.Animator
import android.animation.ObjectAnimator
import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver
import android.widget.FrameLayout
import com.ognev.kotlin.agendacalendarview.CalendarManager
import com.ognev.kotlin.agendacalendarview.R
import com.ognev.kotlin.agendacalendarview.utils.AgendaListViewTouchedEvent
import com.ognev.kotlin.agendacalendarview.utils.BusProvider
import com.ognev.kotlin.agendacalendarview.utils.CalendarScrolledEvent
import com.ognev.kotlin.agendacalendarview.utils.DayClickedEvent
import com.ognev.kotlin.agendacalendarview.utils.FetchedEvent

class AgendaView : FrameLayout {

    lateinit var agendaListView: AgendaListView
        private set
    private lateinit var shadowView: View

    constructor(context: Context) : super(context)

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        inflater.inflate(R.layout.view_agenda, this, true)
        findViewById<View>(R.id.refresh_layout).isEnabled = false
    }

    override fun onFinishInflate() {
        super.onFinishInflate()
        agendaListView = findViewById(R.id.agenda_listview)
        shadowView = findViewById(R.id.view_shadow)

        BusProvider.instance.toObservable()
            .subscribe { event ->
                when (event) {
                    is DayClickedEvent -> agendaListView.scrollToCurrentDate(event.calendar)
                    is CalendarScrolledEvent -> {
                        val offset = (3 * resources.getDimension(R.dimen.day_cell_height))
                        translateList(offset.toInt())
                    }
                    is FetchedEvent -> onEventsFetched()
                }
            }
    }

    private fun onEventsFetched() {
        (agendaListView.adapter as AgendaAdapter).updateEvents()
        viewTreeObserver.addOnGlobalLayoutListener(
            object : ViewTreeObserver.OnGlobalLayoutListener {
                override
                fun onGlobalLayout() {
                    if (width != 0 && height != 0) {
                        // display only two visible rows on the calendar view
                        val layoutParams = layoutParams as ViewGroup.MarginLayoutParams
                        val height = height
                        val margin = (context.resources.getDimension(R.dimen.calendar_header_height) + 2 * context.resources.getDimension(R.dimen.day_cell_height))
                        layoutParams.height = height
                        layoutParams.setMargins(0, margin.toInt(), 0, 0)
                        setLayoutParams(layoutParams)
                        //todo
                        if (!CalendarManager.instance!!.events.isEmpty())
                            agendaListView.scrollToCurrentDate(CalendarManager.instance!!.today)

                        viewTreeObserver.removeGlobalOnLayoutListener(this)
                    }
                }
            }
        )
    }

    override
    fun dispatchTouchEvent(event: MotionEvent): Boolean {
        if (event.action == MotionEvent.ACTION_DOWN) {
            // if the user touches the listView, we put it back to the top
            translateList(0)
        }
        return super.dispatchTouchEvent(event)
    }

    private fun translateList(targetY: Int) {
        if (targetY != translationY.toInt()) {
            val mover = ObjectAnimator.ofFloat(this, "translationY", targetY.toFloat())
            mover.duration = 150
            mover.addListener(object : Animator.AnimatorListener {
                override
                fun onAnimationStart(animation: Animator) {
                    shadowView.visibility = GONE
                }

                override
                fun onAnimationEnd(animation: Animator) {
                    if (targetY == 0) {
                        BusProvider.instance.send(AgendaListViewTouchedEvent())
                    }
                    shadowView.visibility = VISIBLE
                }

                override
                fun onAnimationCancel(animation: Animator) {
                }

                override
                fun onAnimationRepeat(animation: Animator) {
                }
            })
            mover.start()
        }
    }
}
