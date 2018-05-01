package com.ognev.kotlin.agendacalendarview.ui

import android.content.Context
import android.graphics.drawable.Animatable
import android.graphics.drawable.Drawable
import android.support.design.widget.FloatingActionButton
import android.util.AttributeSet
import com.ognev.kotlin.agendacalendarview.R

class ReverseAnimatedButton(context: Context, attributeSet: AttributeSet) : FloatingActionButton(context, attributeSet) {

    private val forwardAnimation: Drawable?
    private val reverseAnimation: Drawable?
    private var shouldAnimateForward = true

    init {
        val attributes = context.obtainStyledAttributes(attributeSet, R.styleable.ReverseAnimatedButton, 0, 0)
        forwardAnimation = attributes.getDrawable(R.styleable.ReverseAnimatedButton_animation)
        reverseAnimation = attributes.getDrawable(R.styleable.ReverseAnimatedButton_reverseAnimation)
        attributes.recycle()
        setImageDrawable(forwardAnimation)
    }

    fun startAnimation() {
        setImageDrawable(getAnimationToRun())
        shouldAnimateForward = shouldAnimateForward.not()
        startDrawable()
    }

    private fun getAnimationToRun() = if (shouldAnimateForward) forwardAnimation else reverseAnimation

    private fun startDrawable() {
        if (drawable != null) {
            (drawable as Animatable).start()
        }
    }

    fun willAnimateForward() = shouldAnimateForward
}
