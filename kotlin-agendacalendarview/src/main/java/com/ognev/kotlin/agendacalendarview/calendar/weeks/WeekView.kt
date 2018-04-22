package com.ognev.kotlin.agendacalendarview.calendar.weeks

import android.content.Context
import android.graphics.Typeface
import android.view.View
import android.view.ViewGroup
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import android.view.ViewGroup.LayoutParams.WRAP_CONTENT
import android.widget.LinearLayout
import android.widget.TextView
import com.ognev.kotlin.agendacalendarview.R
import com.ognev.kotlin.agendacalendarview.calendar.CalendarEventMarkPainter
import com.ognev.kotlin.agendacalendarview.models.ViewAttributes
import com.ognev.kotlin.agendacalendarview.models.WeekItem
import com.ognev.kotlin.agendacalendarview.utils.BusProvider
import com.ognev.kotlin.agendacalendarview.utils.DayClicked
import net.danlew.android.joda.DateUtils
import org.joda.time.format.DateTimeFormat

class WeekView(context: Context) : LinearLayout(context) {

    private val monthFormatter = DateTimeFormat.forPattern("MMM")
    private val calendarEventMarkPainter = CalendarEventMarkPainter()

    init {
        inflate(context, R.layout.week, this)
        layoutParams = ViewGroup.LayoutParams(MATCH_PARENT, WRAP_CONTENT)
    }

    //TODO method was only moved and will be refactored in other PR
    fun bindWeek(weekItem: WeekItem, viewAttributes: ViewAttributes) {
        val dayItems = weekItem.dayItems

        for (c in 0 until dayItems.size) {
            val dayItem = dayItems[c]
            val cellItem = getChildAt(c)
            val txtDay = cellItem.findViewById(R.id.view_day_day_label) as TextView
            val txtMonth = cellItem.findViewById(R.id.view_day_month_label) as TextView
            val circleView = cellItem.findViewById<View>(R.id.view_day_circle_selected)
            val eventsContainer = cellItem.findViewById(R.id.events_marks_container) as LinearLayout
            cellItem.setOnClickListener { BusProvider.instance.send(DayClicked(dayItem)) }

            txtDay.setTextColor(viewAttributes.cellDayTextColor)
            cellItem.setBackgroundColor(viewAttributes.cellDayBackgroundColor)
            circleView.setBackgroundDrawable(viewAttributes.circleBackgroundColor)
            txtMonth.visibility = View.GONE
            txtMonth.setTextColor(viewAttributes.monthTextColor)
            circleView.visibility = View.GONE

            txtDay.setTypeface(null, Typeface.NORMAL)
            txtMonth.setTypeface(null, Typeface.NORMAL)

            // Display the day
            txtDay.text = dayItem.date.dayOfMonth.toString()

            // Highlight first day of the month
            val isFirstDayOfMonth = dayItem.date.dayOfMonth == 1
            if (isFirstDayOfMonth && !dayItem.isSelected) {
                txtMonth.visibility = View.VISIBLE
                txtMonth.text = monthFormatter.print(dayItem.date)
                txtDay.setTypeface(null, Typeface.BOLD)
                txtMonth.setTypeface(null, Typeface.BOLD)
            }

            // Highlight the cell if this day is today
            if (DateUtils.isToday(dayItem.date) && !dayItem.isSelected) {
                txtDay.setTextColor(viewAttributes.currentDayTextColor)
            }

            // Show a circle if the day is selected
            if (dayItem.isSelected) {
                txtDay.setTextColor(viewAttributes.selectedDayTextColor)
                circleView.visibility = View.VISIBLE
            }

            val markColor = viewAttributes.cellEventMarkColor
            val eventPlusShowThreshold = viewAttributes.cellEventPlusShowThreshold
            calendarEventMarkPainter.addMarks(eventsContainer, dayItem.eventsCount, eventPlusShowThreshold, markColor)
        }
    }
}
