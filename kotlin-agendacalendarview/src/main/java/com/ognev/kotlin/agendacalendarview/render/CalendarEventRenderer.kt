package com.ognev.kotlin.agendacalendarview.render

import android.support.annotation.LayoutRes
import android.view.View
import com.ognev.kotlin.agendacalendarview.models.CalendarEvent
import java.util.Calendar

interface CalendarEventRenderer<in T : CalendarEvent> {

    fun setupEventItemView(eventItemView: View, event: T, position: Int)

    fun setupHeaderItemView(headerItemView: View, day: Calendar)

    @LayoutRes
    fun getEventLayout(isEmptyEvent: Boolean): Int

    @LayoutRes
    fun getHeaderLayout(): Int
}
