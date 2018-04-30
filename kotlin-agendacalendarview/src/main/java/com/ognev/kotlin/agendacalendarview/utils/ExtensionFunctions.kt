package com.ognev.kotlin.agendacalendarview.utils

import android.support.annotation.LayoutRes
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

internal fun ViewGroup.inflateWithAttach(@LayoutRes layoutRes: Int, attachToRoot: Boolean): View =
    LayoutInflater.from(this.context).inflate(layoutRes, this, attachToRoot)
