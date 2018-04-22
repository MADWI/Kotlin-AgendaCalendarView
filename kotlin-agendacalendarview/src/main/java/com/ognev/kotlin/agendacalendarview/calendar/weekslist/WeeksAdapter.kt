package com.ognev.kotlin.agendacalendarview.calendar.weekslist

import android.content.Context
import android.graphics.PorterDuff
import android.graphics.Typeface
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import com.ognev.kotlin.agendacalendarview.R
import com.ognev.kotlin.agendacalendarview.models.DayItem
import com.ognev.kotlin.agendacalendarview.models.ViewAttributes
import com.ognev.kotlin.agendacalendarview.models.WeekItem
import com.ognev.kotlin.agendacalendarview.utils.BusProvider
import com.ognev.kotlin.agendacalendarview.utils.DayClicked
import net.danlew.android.joda.DateUtils
import org.joda.time.LocalDate
import org.joda.time.format.DateTimeFormat

class WeeksAdapter(private val mContext: Context, val viewAttributes: ViewAttributes)
    : RecyclerView.Adapter<WeeksAdapter.WeekViewHolder>() {

    private val weeks = mutableListOf<WeekItem>()

    fun updateWeeksItems(weekItems: List<WeekItem>) {
        weeks.clear()
        weeks.addAll(weekItems)
        notifyDataSetChanged()
    }

    override fun getItemCount() = weeks.size

    override
    fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WeekViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.list_item_week, parent, false)
        return WeekViewHolder(view)
    }

    override
    fun onBindViewHolder(weekViewHolder: WeekViewHolder, position: Int) {
        val weekItem = weeks[position]
        weekViewHolder.bindWeek(weekItem)
    }

    inner class WeekViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        /**
         * List of layout containers for each day
         */
        private var cells = mutableListOf<LinearLayout>()
        private val monthFormatter = DateTimeFormat.forPattern("MMM")

        init {
            val daysContainer = itemView.findViewById<LinearLayout>(R.id.week_days_container)
            setUpChildren(daysContainer)
        }

        private fun setUpChildren(daysContainer: LinearLayout) {
            for (i in 0 until daysContainer.childCount) {
                cells.add(daysContainer.getChildAt(i) as LinearLayout)
            }
        }

        fun bindWeek(weekItem: WeekItem) {
            val dayItems = weekItem.dayItems

            for (c in 0 until dayItems.size) {
                val dayItem = dayItems[c]
                val cellItem = cells[c]
                val txtDay = cellItem.findViewById(R.id.view_day_day_label) as TextView
                val txtMonth = cellItem.findViewById(R.id.view_day_month_label) as TextView
                val circleView = cellItem.findViewById<View>(R.id.view_day_circle_selected)
                val eventsDotsContainer = cellItem.findViewById(R.id.events_dots_container) as LinearLayout
                cellItem.setOnClickListener { BusProvider.instance.send(DayClicked(dayItem)) }

                circleView.setBackgroundDrawable(viewAttributes.circleBackgroundColor)
                txtMonth.visibility = View.GONE
                txtDay.setTextColor(viewAttributes.pastDayTextColor)
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

                // Check if this day is in the past
                if (dayItem.date.isAfter(LocalDate.now()) && !DateUtils.isToday(dayItem.date)) {
                    txtDay.setTextColor(viewAttributes.pastDayTextColor)
                    txtMonth.setTextColor(viewAttributes.monthTextColor)
                    cellItem.setBackgroundColor(viewAttributes.cellPastBackgroundColor)
                } else {
                    cellItem.setBackgroundColor(viewAttributes.cellNowadaysDayColor)
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

                addEventsMarks(eventsDotsContainer, dayItem)
            }
        }

        private fun addEventsMarks(eventsMarksContainer: LinearLayout, dayItem: DayItem) {
            if (eventsMarksContainer.childCount == dayItem.eventsCount) {
                return
            }
            eventsMarksContainer.removeAllViews()
            repeat(dayItem.eventsCount) {
                if (it >= viewAttributes.cellEventPlusShowThreshold) {
                    val plusView = LayoutInflater.from(mContext).inflate(R.layout.event_plus, eventsMarksContainer, false) as ImageView
                    plusView.setColorFilter(viewAttributes.cellEventMarkColor)
                    eventsMarksContainer.addView(plusView)
                    return
                }
                val dotView = LayoutInflater.from(mContext).inflate(R.layout.event_dot, eventsMarksContainer, false)
                dotView.background.setColorFilter(viewAttributes.cellEventMarkColor, PorterDuff.Mode.SRC_ATOP)
                eventsMarksContainer.addView(dotView)
            }
        }
    }
}
