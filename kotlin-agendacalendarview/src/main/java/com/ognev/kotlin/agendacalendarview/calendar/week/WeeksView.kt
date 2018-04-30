package com.ognev.kotlin.agendacalendarview.calendar.week

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.util.AttributeSet
import android.view.Gravity
import com.github.rubensousa.gravitysnaphelper.GravitySnapHelper
import com.ognev.kotlin.agendacalendarview.bus.BusProvider
import com.ognev.kotlin.agendacalendarview.bus.CalendarScrolled

class WeeksView(context: Context, attrs: AttributeSet) : RecyclerView(context, attrs) {

    private val scrollListener = object : OnScrollListener() {

        override
        fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
            super.onScrollStateChanged(recyclerView, newState)
            when (newState) {
                SCROLL_STATE_DRAGGING -> { BusProvider.instance.send(CalendarScrolled()) }
            }
        }
    }

    init {
        itemAnimator = null
        setHasFixedSize(true)
        GravitySnapHelper(Gravity.TOP).attachToRecyclerView(this)
        addOnScrollListener(scrollListener)
    }
}
