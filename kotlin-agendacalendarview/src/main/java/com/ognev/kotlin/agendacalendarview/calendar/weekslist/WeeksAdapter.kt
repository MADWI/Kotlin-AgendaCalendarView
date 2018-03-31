package com.ognev.kotlin.agendacalendarview.calendar.weekslist

import android.animation.Animator
import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Context
import android.graphics.PorterDuff
import android.graphics.Typeface
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import com.ognev.kotlin.agendacalendarview.CalendarManager
import com.ognev.kotlin.agendacalendarview.R
import com.ognev.kotlin.agendacalendarview.models.AgendaCalendarViewAttributes
import com.ognev.kotlin.agendacalendarview.models.IDayItem
import com.ognev.kotlin.agendacalendarview.models.IWeekItem
import com.ognev.kotlin.agendacalendarview.utils.BusProvider
import com.ognev.kotlin.agendacalendarview.utils.DateHelper
import com.ognev.kotlin.agendacalendarview.utils.Events
import java.text.SimpleDateFormat
import java.util.ArrayList
import java.util.Calendar

class WeeksAdapter(private val mContext: Context, private val mToday: Calendar,
    val viewAttributes: AgendaCalendarViewAttributes) : RecyclerView.Adapter<WeeksAdapter.WeekViewHolder>() {

    override fun getItemCount() = weeksList.size

    val weeksList: List<IWeekItem>
    var isDragging: Boolean = false
        set(dragging) {
            if (dragging != this.isDragging) {
                field = dragging
                notifyItemRangeChanged(0, weeksList.size)
            }
        }
    var isAlphaSet: Boolean = false

    init {
        weeksList = CalendarManager.instance!!.weeks
    }

    fun updateWeeksItems(weekItems: List<IWeekItem>) {
        //    this.mWeeksList.clear();
        //    this.mWeeksList.addAll(weekItems);
        notifyDataSetChanged()
    }

    override
    fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WeekViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.list_item_week, parent, false)
        return WeekViewHolder(view)
    }

    override
    fun onBindViewHolder(weekViewHolder: WeekViewHolder, position: Int) {
        val weekItem = weeksList[position]
        weekViewHolder.bindWeek(weekItem, mToday)
    }

    inner class WeekViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        /**
         * List of layout containers for each day
         */
        private var mCells: List<LinearLayout>? = null
        private val mTxtMonth: TextView
        private val mMonthBackground: FrameLayout

        init {
            mTxtMonth = itemView.findViewById(R.id.month_label) as TextView
            mMonthBackground = itemView.findViewById(R.id.month_background) as FrameLayout
            val daysContainer = itemView.findViewById(R.id.week_days_container) as LinearLayout
            setUpChildren(daysContainer)
        }

        fun bindWeek(weekItem: IWeekItem, today: Calendar) {
            setUpMonthOverlay()

            val dayItems = weekItem.dayItems

            for (c in 0 until dayItems.size) {
                val dayItem = dayItems[c]
                val cellItem = mCells!![c]
                val txtDay = cellItem.findViewById(R.id.view_day_day_label) as TextView
                val txtMonth = cellItem.findViewById(R.id.view_day_month_label) as TextView
                val circleView = cellItem.findViewById(R.id.view_day_circle_selected)
                val eventsDotsContainer = cellItem.findViewById(R.id.events_dots_container) as LinearLayout
                cellItem.setOnClickListener { BusProvider.instance.send(Events.DayClickedEvent(dayItem)) }

                circleView.setBackgroundDrawable(viewAttributes.circleBackgroundColor)
                txtMonth.visibility = View.GONE
                txtDay.setTextColor(viewAttributes.pastDayTextColor)
                txtMonth.setTextColor(viewAttributes.monthTextColor)
                circleView.visibility = View.GONE

                txtDay.setTypeface(null, Typeface.NORMAL)
                txtMonth.setTypeface(null, Typeface.NORMAL)

                // Display the day
                txtDay.text = dayItem.value.toString()

                // Highlight first day of the month
                if (dayItem.isFirstDayOfTheMonth && !dayItem.isSelected) {
                    txtMonth.visibility = View.VISIBLE
                    txtMonth.text = dayItem.month
                    txtDay.setTypeface(null, Typeface.BOLD)
                    txtMonth.setTypeface(null, Typeface.BOLD)
                }

                // Check if this day is in the past
                if (today.time.after(dayItem.date) && !DateHelper.sameDate(today, dayItem.date)) {
                    txtDay.setTextColor(viewAttributes.pastDayTextColor)
                    txtMonth.setTextColor(viewAttributes.monthTextColor)
                    cellItem.setBackgroundColor(viewAttributes.cellPastBackgroundColor)
                } else {
                    cellItem.setBackgroundColor(viewAttributes.cellNowadaysDayColor)
                }

                // Highlight the cell if this day is today
                if (dayItem.isToday && !dayItem.isSelected) {
                    txtDay.setTextColor(viewAttributes.currentDayTextColor)
                }

                // Show a circle if the day is selected
                if (dayItem.isSelected) {
                    txtDay.setTextColor(viewAttributes.selectedDayTextColor)
                    circleView.visibility = View.VISIBLE
                }

                addEventsMarks(eventsDotsContainer, dayItem)

                // Check if the month label has to be displayed
                if (dayItem.value == 15) {
                    mTxtMonth.visibility = View.VISIBLE
                    val monthDateFormat = SimpleDateFormat(mContext.getString(R.string.month_half_name_format), CalendarManager.instance!!.locale)
                    var month = monthDateFormat.format(weekItem.date).toUpperCase()
                    if (today.get(Calendar.YEAR) != weekItem.year) {
                        month += String.format(" %d", weekItem.year)
                    }
                    mTxtMonth.text = month
                }
            }
        }

        private fun setUpChildren(daysContainer: LinearLayout) {
            mCells = ArrayList()
            for (i in 0..daysContainer.childCount - 1) {
                (mCells as ArrayList<LinearLayout>).add(daysContainer.getChildAt(i) as LinearLayout)
            }
        }

        private fun setUpMonthOverlay() {
            mTxtMonth.setVisibility(View.GONE)

            if (isDragging) {
                val animatorSetFadeIn = AnimatorSet()
                animatorSetFadeIn.setDuration(FADE_DURATION)
                val animatorTxtAlphaIn = ObjectAnimator.ofFloat(mTxtMonth, "alpha", mTxtMonth.getAlpha(), 1f)
                val animatorBackgroundAlphaIn = ObjectAnimator.ofFloat(mMonthBackground, "alpha", mMonthBackground.getAlpha(), 1f)
                animatorSetFadeIn.playTogether(
                    animatorTxtAlphaIn
                    //animatorBackgroundAlphaIn
                )
                animatorSetFadeIn.addListener(object : Animator.AnimatorListener {
                    override
                    fun onAnimationStart(animation: Animator) {
                    }

                    override
                    fun onAnimationEnd(animation: Animator) {
                        isAlphaSet = true
                    }

                    override
                    fun onAnimationCancel(animation: Animator) {
                    }

                    override
                    fun onAnimationRepeat(animation: Animator) {
                    }
                })
                animatorSetFadeIn.start()
            } else {
                val animatorSetFadeOut = AnimatorSet()
                animatorSetFadeOut.setDuration(FADE_DURATION)
                val animatorTxtAlphaOut = ObjectAnimator.ofFloat(mTxtMonth, "alpha", mTxtMonth.getAlpha(), 0f)
                val animatorBackgroundAlphaOut = ObjectAnimator.ofFloat(mMonthBackground, "alpha", mMonthBackground.getAlpha(), 0f)
                animatorSetFadeOut.playTogether(
                    animatorTxtAlphaOut
                    //animatorBackgroundAlphaOut
                )
                animatorSetFadeOut.addListener(object : Animator.AnimatorListener {
                    override
                    fun onAnimationStart(animation: Animator) {
                    }

                    override
                    fun onAnimationEnd(animation: Animator) {
                        isAlphaSet = false
                    }

                    override
                    fun onAnimationCancel(animation: Animator) {
                    }

                    override
                    fun onAnimationRepeat(animation: Animator) {
                    }
                })
                animatorSetFadeOut.start()
            }

            if (isAlphaSet) {
                //mMonthBackground.setAlpha(1f);
                mTxtMonth.setAlpha(1f)
            } else {
                //mMonthBackground.setAlpha(0f);
                mTxtMonth.setAlpha(0f)
            }
        }

        private fun addEventsMarks(eventsMarksContainer: LinearLayout, dayItem: IDayItem) {
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

    companion object {
        const val FADE_DURATION: Long = 250
    }
}
