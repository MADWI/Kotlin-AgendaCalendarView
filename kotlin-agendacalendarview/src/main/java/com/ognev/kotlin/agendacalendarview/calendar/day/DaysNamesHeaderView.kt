package com.ognev.kotlin.agendacalendarview.calendar.day

import android.content.Context
import android.support.annotation.ColorInt
import android.util.AttributeSet
import android.widget.LinearLayout
import android.widget.TextView
import com.ognev.kotlin.agendacalendarview.R
import com.ognev.kotlin.agendacalendarview.utils.inflateWithAttach
import kotlinx.android.synthetic.main.days_names_header.view.*
import org.joda.time.LocalDate
import org.joda.time.format.DateTimeFormat
import org.joda.time.format.DateTimeFormatter

class DaysNamesHeaderView(context: Context, attrs: AttributeSet) : LinearLayout(context, attrs) {

    private val today = LocalDate.now()
    private val dayNamePattern: String = context.getString(R.string.day_name_pattern)
    private val dayNameFormatter: DateTimeFormatter = DateTimeFormat.forPattern(dayNamePattern)

    init {
        inflateWithAttach(R.layout.days_names_header, true)
        applyForChildren { dayView, index -> dayView.text = getNameForDay(index) }
    }

    fun setTextColor(@ColorInt color: Int) = applyForChildren { dayView, _ -> dayView.setTextColor(color) }

    private fun applyForChildren(action: (TextView, Int) -> Unit) {
        for (i in 0 until daysNamesContainer.childCount) {
            val dayView = daysNamesContainer.getChildAt(i) as TextView
            action.invoke(dayView, i)
        }
    }

    private fun getNameForDay(index: Int): String {
        val day = today.withDayOfWeek(index + 1)
        return dayNameFormatter.print(day).toUpperCase()
    }
}
