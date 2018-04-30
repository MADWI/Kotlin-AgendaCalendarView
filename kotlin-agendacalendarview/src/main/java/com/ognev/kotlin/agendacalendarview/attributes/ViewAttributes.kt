package com.ognev.kotlin.agendacalendarview.attributes

import android.graphics.drawable.Drawable
import android.support.annotation.ColorInt
import android.support.annotation.DrawableRes

data class ViewAttributes(
    @ColorInt val calendarColor: Int, @ColorInt val cellDayTextColor: Int,
    @ColorInt val cellEventMarkColor: Int, @ColorInt val cellEventPlusShowThreshold: Int,
    @ColorInt val daysNamesTextColor: Int, @ColorInt val daysNamesHeaderColor: Int,
    @ColorInt val monthTextColor: Int, @ColorInt val currentDayTextColor: Int,
    @ColorInt val selectedDayTextColor: Int, @DrawableRes val selectedDayBackground: Drawable?
)
