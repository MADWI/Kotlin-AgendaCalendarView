package com.ognev.kotlin.agendacalendarview.calendar.week

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.util.AttributeSet
import android.view.View
import com.ognev.kotlin.agendacalendarview.utils.BusProvider
import com.ognev.kotlin.agendacalendarview.utils.CalendarScrolled

class WeeksView(context: Context, attrs: AttributeSet) : RecyclerView(context, attrs) {

    private var userScrolling = false
    private var scrolling = false
    private val centerView: View
        get() = getChildClosestToPosition(measuredHeight / 2)!!

    init {
        itemAnimator = null
        setHasFixedSize(true)
    }

    override fun onFinishInflate() {
        super.onFinishInflate()
        addOnScrollListener(scrollListener)
    }

    private val scrollListener = object : OnScrollListener() {

        override
        fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
            super.onScrollStateChanged(recyclerView, newState)
            when (newState) {
                SCROLL_STATE_IDLE -> {
                    if (userScrolling) {
                        scrollToView(centerView)
                    }
                    userScrolling = false
                    scrolling = false
                }
                // If scroll is caused by a touch (scroll touch, not any touch)
                SCROLL_STATE_DRAGGING -> {
                    BusProvider.instance.send(CalendarScrolled())
                    // If scroll was initiated already, this is not a user scrolling, but probably a tap, else set userScrolling
                    if (!scrolling) {
                        userScrolling = true
                    }
                }
                SCROLL_STATE_SETTLING -> {
                    // The user's finger is not touching the list anymore, no need
                    // for any alpha animation then
                    scrolling = true
                }
            }
        }
    }

    private fun getChildClosestToPosition(y: Int): View? {
        if (childCount <= 0) {
            return null
        }

        val itemHeight = getChildAt(0).measuredHeight
        var closestY = 9999
        var closestChild: View? = null

        for (i in 0 until childCount) {
            val child = getChildAt(i)
            val childCenterY = child.y + itemHeight / 2
            val yDistance = childCenterY - y

            // If child center is closer than previous closest, set it as closest
            if (Math.abs(yDistance) < Math.abs(closestY)) {
                closestY = yDistance.toInt()
                closestChild = child
            }
        }

        return closestChild
    }

    private fun scrollToView(child: View?) {
        if (child == null) {
            return
        }
        stopScroll()
        val scrollDistance = getScrollDistance(child)
        if (scrollDistance != 0) {
            smoothScrollBy(0, scrollDistance)
        }
    }

    private fun getScrollDistance(child: View): Int {
        val itemHeight = getChildAt(0).measuredHeight
        val centerY = measuredHeight / 2
        val childCenterY = child.y + itemHeight / 2
        return childCenterY.toInt() - centerY
    }
}
