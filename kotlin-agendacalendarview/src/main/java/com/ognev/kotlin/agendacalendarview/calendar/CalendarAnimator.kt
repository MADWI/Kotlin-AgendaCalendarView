package com.ognev.kotlin.agendacalendarview.calendar

import android.animation.ValueAnimator
import android.view.View
import android.view.ViewGroup
import com.ognev.kotlin.agendacalendarview.R
import java.lang.ref.WeakReference

class CalendarAnimator(view: View) {

    private val viewReference = WeakReference<View>(view)
    private val resources = view.context.resources
    private val rowHeight = resources.getDimension(R.dimen.day_cell_height)
    private val headerHeight = resources.getDimension(R.dimen.calendar_header_height)
    private val expandedCalendarHeight: Int by lazy {
        val rows = resources.getInteger(R.integer.calendar_expanded_rows)
        (headerHeight + rowHeight * rows).toInt()
    }
    private val collapsedCalendarHeight: Int by lazy {
        val rows = resources.getInteger(R.integer.calendar_collapsed_rows)
        (headerHeight + rowHeight * rows).toInt()
    }
    private val animationDuration =
        resources.getInteger(R.integer.height_animation_duration).toLong()

    fun expandView() = animateHeightTo(expandedCalendarHeight)

    fun collapseView() = animateHeightTo(collapsedCalendarHeight)

    fun dispose() = viewReference.clear()

    private fun animateHeightTo(endHeight: Int) {
        val view = viewReference.get() ?: return
        val animator = ValueAnimator.ofInt(view.measuredHeight, endHeight)
        animator.duration = animationDuration
        animator.addUpdateListener { setHeight(it.animatedValue as Int) }
        animator.start()
    }

    private fun setHeight(height: Int) {
        val view = viewReference.get() ?: return
        val layoutParams = view.layoutParams as ViewGroup.LayoutParams
        layoutParams.height = height
        view.layoutParams = layoutParams
    }
}
