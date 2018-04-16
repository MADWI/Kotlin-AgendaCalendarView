package com.ognev.kotlin.agendacalendarview.utils

import rx.subjects.PublishSubject
import rx.subjects.SerializedSubject

class BusProvider {

    private val bus = SerializedSubject<Event, Event>(PublishSubject.create())

    fun send(event: Event) = bus.onNext(event)

    fun toObservable() = bus

    companion object {
        val instance: BusProvider = BusProvider()
    }
}
