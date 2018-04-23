package com.ognev.kotlin.agendacalendarview.utils

import android.databinding.BindingAdapter
import android.graphics.Typeface
import android.support.annotation.ColorInt
import android.widget.LinearLayout
import android.widget.TextView
import com.ognev.kotlin.agendacalendarview.calendar.CalendarEventMarkPainter
import org.joda.time.LocalDate
import org.joda.time.format.DateTimeFormat

private val monthFormatter = DateTimeFormat.forPattern("MMM")
private val calendarEventMarkPainter = CalendarEventMarkPainter()

@BindingAdapter("monthNameOfDate")
fun setMonthNameOfDate(textView: TextView, date: LocalDate) {
    textView.text = monthFormatter.print(date)
    textView.setTypeface(null, Typeface.BOLD)
}

@BindingAdapter("typeface")
fun setTypeface(textView: TextView, typeface: Int) {
    textView.setTypeface(null, typeface)
}

@BindingAdapter(value = ["eventsMarksNumber", "markColor", "showPlusThreshold"], requireAll = true)
fun setEventMarks(layout: LinearLayout, eventsNumber: Int, @ColorInt markColor: Int, showPlusThreshold: Int) {
    calendarEventMarkPainter.addMarks(layout, eventsNumber, showPlusThreshold, markColor)
}
