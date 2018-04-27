package com.ognev.kotlin.agendacalendarview.calendar.week

import android.databinding.DataBindingUtil
import android.support.annotation.LayoutRes
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.ognev.kotlin.agendacalendarview.R
import com.ognev.kotlin.agendacalendarview.bus.BusProvider
import com.ognev.kotlin.agendacalendarview.bus.DayClicked
import com.ognev.kotlin.agendacalendarview.calendar.day.DayItem
import com.ognev.kotlin.agendacalendarview.databinding.DayCellBinding
import com.ognev.kotlin.agendacalendarview.models.ViewAttributes

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
        WeekViewHolder(parent.inflate(R.layout.week))

    override
    fun onBindViewHolder(weekViewHolder: WeekViewHolder, position: Int) =
        weekViewHolder.bind(weeks[position])

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
            }
            child.setOnClickListener { BusProvider.instance.send(DayClicked(dayItem)) }
        }
    }

    private fun ViewGroup.applyForChildren(action: (child: View, index: Int) -> Unit) {
        for (i in 0 until childCount) {
            action(getChildAt(i), i)
        }
    }

    private fun ViewGroup.inflate(@LayoutRes layoutRes: Int) =
        LayoutInflater.from(this.context).inflate(layoutRes, this, false)
}

