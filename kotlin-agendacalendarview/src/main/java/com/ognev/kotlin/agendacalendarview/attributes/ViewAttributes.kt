package com.ognev.kotlin.agendacalendarview.attributes

import android.graphics.drawable.Drawable
import android.support.annotation.ColorInt

data class ViewAttributes(@ColorInt val daysNamesHeaderColor: Int, @ColorInt val daysNamesTextColor: Int,
    @ColorInt val monthTextColor: Int, @ColorInt val selectedDayTextColor: Int,
    @ColorInt val currentDayTextColor: Int, @ColorInt val circleBackgroundColor: Drawable?,
    @ColorInt val cellDayBackgroundColor: Int, @ColorInt val cellDayTextColor: Int,
    @ColorInt val cellEventMarkColor: Int, @ColorInt val cellEventPlusShowThreshold: Int)
