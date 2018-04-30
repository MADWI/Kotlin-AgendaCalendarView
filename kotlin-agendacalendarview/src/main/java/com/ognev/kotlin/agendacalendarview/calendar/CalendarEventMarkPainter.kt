package com.ognev.kotlin.agendacalendarview.calendar

import android.graphics.PorterDuff
import android.support.annotation.ColorInt
import android.view.ViewGroup
import android.widget.ImageView
import com.ognev.kotlin.agendacalendarview.R
import com.ognev.kotlin.agendacalendarview.utils.inflateWithAttach

class CalendarEventMarkPainter {

    fun addMarks(containerView: ViewGroup, eventsCount: Int, eventPlusShowThreshold: Int, @ColorInt markColor: Int) {
        if (containerView.childCount == eventsCount) {
            return
        }
        containerView.removeAllViews()
        repeat(eventsCount) {
            if (it >= eventPlusShowThreshold) {
                addEventPlusIcon(containerView, markColor)
                return
            }
            addEventDotIcon(containerView, markColor)
        }
    }

    private fun addEventDotIcon(viewGroup: ViewGroup, @ColorInt iconColor: Int) {
        val dotView = viewGroup.inflateWithAttach(R.layout.event_dot, false)
        dotView.background.setColorFilter(iconColor, PorterDuff.Mode.SRC_ATOP)
        viewGroup.addView(dotView)
    }

    private fun addEventPlusIcon(viewGroup: ViewGroup, @ColorInt iconColor: Int) {
        val plusView = viewGroup.inflateWithAttach(R.layout.event_plus, false) as ImageView
        plusView.setColorFilter(iconColor)
        viewGroup.addView(plusView)
    }
}
