package com.ognev.kotlin.agendacalendarview.ui

import android.content.Context
import android.graphics.drawable.Animatable
import android.graphics.drawable.Drawable
import android.support.design.widget.FloatingActionButton
import android.util.AttributeSet
import com.ognev.kotlin.agendacalendarview.R

internal class ExpandButton(
    context: Context,
    attributeSet: AttributeSet
) : FloatingActionButton(context, attributeSet) {

    private val forwardAnimation: Drawable = context.getDrawable(R.drawable.expand_to_collapse)
    private val reverseAnimation: Drawable = context.getDrawable(R.drawable.collapse_to_expand)
    var isNextAnimationIsForward = true
        private set

    init {
        setImageDrawable(forwardAnimation)
    }

    fun startAnimation() {
        setImageDrawable(getNextAnimation())
        isNextAnimationIsForward = isNextAnimationIsForward.not()
        (drawable as Animatable).start()
    }

    private fun getNextAnimation() = if (isNextAnimationIsForward) forwardAnimation else reverseAnimation
}
