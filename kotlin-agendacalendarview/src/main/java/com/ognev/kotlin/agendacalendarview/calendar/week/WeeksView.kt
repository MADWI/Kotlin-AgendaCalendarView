package com.ognev.kotlin.agendacalendarview.calendar.week

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.util.AttributeSet
import android.view.Gravity
import com.github.rubensousa.gravitysnaphelper.GravitySnapHelper

class WeeksView(context: Context, attrs: AttributeSet) : RecyclerView(context, attrs) {

    //TODO remove view
    init {
        itemAnimator = null
        setHasFixedSize(true)
        GravitySnapHelper(Gravity.TOP).attachToRecyclerView(this)
    }
}
