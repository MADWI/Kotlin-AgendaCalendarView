package com.ognev.kotlin.agendacalendarview.sample

import android.app.Application
//import com.squareup.leakcanary.LeakCanary

open class App : Application() {

    override fun onCreate() {
        super.onCreate()
//        if (!LeakCanary.isInAnalyzerProcess(this)) {
//            LeakCanary.install(this)
//        }
    }
}
