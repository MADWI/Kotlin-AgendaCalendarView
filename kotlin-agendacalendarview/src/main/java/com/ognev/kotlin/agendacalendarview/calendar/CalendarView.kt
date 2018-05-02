package com.ognev.kotlin.agendacalendarview.calendar

import android.content.Context
import android.util.AttributeSet
import android.widget.LinearLayout
import com.ognev.kotlin.agendacalendarview.R
import com.ognev.kotlin.agendacalendarview.attributes.ViewAttributes
import com.ognev.kotlin.agendacalendarview.calendar.week.WeekItem
import com.ognev.kotlin.agendacalendarview.calendar.week.WeeksAdapter
import com.ognev.kotlin.agendacalendarview.utils.inflateWithAttach
import kotlinx.android.synthetic.main.calendar.view.*

/**
 * The calendar view is a freely scrolling view that allows the user to browse between days of the
 * year.
 */
class CalendarView(context: Context, attrs: AttributeSet) : LinearLayout(context, attrs) {

    init {
        inflateWithAttach(R.layout.calendar, true)
        //TODO move to xml
        orientation = VERTICAL
    }

    fun init(weeks: List<WeekItem>, viewAttributes: ViewAttributes) {
        daysNamesHeaderView.setBackgroundColor(viewAttributes.daysNamesHeaderColor)
        daysNamesHeaderView.setTextColor(viewAttributes.daysNamesTextColor)
        setupAdapter(weeks, viewAttributes)
    }

    fun scrollToWeekIndex(weekIndex: Int) = weeksView.scrollToPosition(weekIndex)

    private fun setupAdapter(weeks: List<WeekItem>, viewAttributes: ViewAttributes) {
        weeksView.adapter = WeeksAdapter(weeks, viewAttributes)
    }
}
