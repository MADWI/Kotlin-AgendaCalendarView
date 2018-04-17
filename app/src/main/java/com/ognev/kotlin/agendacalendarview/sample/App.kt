package com.ognev.kotlin.agendacalendarview.sample

import com.ognev.kotlin.agendacalendarview.AgendaCalendarApp
import com.squareup.leakcanary.LeakCanary

class App : AgendaCalendarApp() {

    override fun onCreate() {
        super.onCreate()
        setupLeakCanary()
    }

    private fun setupLeakCanary() {
        if (!LeakCanary.isInAnalyzerProcess(this)) {
            LeakCanary.install(this)
        }
    }
}
