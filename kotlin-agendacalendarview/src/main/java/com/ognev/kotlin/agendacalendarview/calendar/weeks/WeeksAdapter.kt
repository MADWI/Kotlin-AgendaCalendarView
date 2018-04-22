package com.ognev.kotlin.agendacalendarview.calendar.weeks

import android.support.v7.widget.RecyclerView
import android.view.ViewGroup
import com.ognev.kotlin.agendacalendarview.models.ViewAttributes
import com.ognev.kotlin.agendacalendarview.models.WeekItem

class WeeksAdapter(val viewAttributes: ViewAttributes)
    : RecyclerView.Adapter<WeeksAdapter.WeekViewHolder>() {

    private val weeks = mutableListOf<WeekItem>()

    fun updateWeeksItems(weekItems: List<WeekItem>) {
        weeks.clear()
        weeks.addAll(weekItems)
        notifyDataSetChanged()
    }

    override fun getItemCount() = weeks.size

    override
    fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        WeekViewHolder(WeekView(parent.context))

    override
    fun onBindViewHolder(weekViewHolder: WeekViewHolder, position: Int) {
        val weekItem = weeks[position]
        weekViewHolder.bind(weekItem)
    }

    inner class WeekViewHolder(private val weekView: WeekView) : RecyclerView.ViewHolder(weekView) {

        fun bind(weekItem: WeekItem) = weekView.bindWeek(weekItem, viewAttributes)
    }
}
