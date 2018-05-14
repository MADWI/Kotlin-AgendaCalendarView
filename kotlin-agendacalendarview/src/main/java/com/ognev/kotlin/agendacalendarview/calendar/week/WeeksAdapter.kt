package com.ognev.kotlin.agendacalendarview.calendar.week

import android.databinding.DataBindingUtil
import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import com.ognev.kotlin.agendacalendarview.R
import com.ognev.kotlin.agendacalendarview.attributes.ViewAttributes
import com.ognev.kotlin.agendacalendarview.calendar.day.CalendarClick
import com.ognev.kotlin.agendacalendarview.calendar.day.DayItem
import com.ognev.kotlin.agendacalendarview.databinding.DayCellBinding
import com.ognev.kotlin.agendacalendarview.utils.inflateWithAttach

class WeeksAdapter(private val weekItems: List<WeekItem>, private val viewAttributes: ViewAttributes)
    : RecyclerView.Adapter<WeeksAdapter.WeekViewHolder>() {

    override fun getItemCount() = weekItems.size

    override
    fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        WeekViewHolder(parent.inflateWithAttach(R.layout.week, false))

    override
    fun onBindViewHolder(weekViewHolder: WeekViewHolder, position: Int) =
        weekViewHolder.bind(weekItems[position])

    inner class WeekViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bind(weekItem: WeekItem) {
            (itemView as ViewGroup).applyForChildren { child: View, index: Int ->
                setupChild(child, weekItem.days[index])
            }
        }

        private fun setupChild(child: View, dayItem: DayItem) {
            DataBindingUtil.bind<DayCellBinding>(child)?.apply {
                day = dayItem
                attributes = viewAttributes
                executePendingBindings()
            }
            child.setOnClickListener { dayItem.selectedBy = CalendarClick() }
        }
    }

    private fun ViewGroup.applyForChildren(action: (child: View, index: Int) -> Unit) {
        for (i in 0 until childCount) {
            action(getChildAt(i), i)
        }
    }
}

