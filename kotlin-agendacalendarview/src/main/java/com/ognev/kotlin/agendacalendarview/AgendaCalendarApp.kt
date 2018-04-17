package com.ognev.kotlin.agendacalendarview

import android.app.Application
import net.danlew.android.joda.JodaTimeAndroid

open class AgendaCalendarApp : Application() {

    override fun onCreate() {
        super.onCreate()
        JodaTimeAndroid.init(this)
    }
}
