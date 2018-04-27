package com.ognev.kotlin.agendacalendarview.render

import android.support.annotation.LayoutRes
import android.view.View
import com.ognev.kotlin.agendacalendarview.event.CalendarEvent
import org.joda.time.LocalDate

interface CalendarEventRenderer<in T : CalendarEvent> {

    fun setupEventItemView(eventItemView: View, event: T, position: Int)

    fun setupHeaderItemView(headerItemView: View, day: LocalDate)

    @LayoutRes
    fun getEventLayout(isEmptyEvent: Boolean): Int

    @LayoutRes
    fun getHeaderLayout(): Int
}
