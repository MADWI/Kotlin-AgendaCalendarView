package com.ognev.kotlin.agendacalendarview.agenda

import android.content.Context
import android.util.AttributeSet
import android.view.View
import se.emilsjolander.stickylistheaders.StickyListHeadersListView

class AgendaView(context: Context, attrs: AttributeSet) : StickyListHeadersListView(context, attrs),
    StickyListHeadersListView.OnStickyHeaderChangedListener {

    lateinit var onHeaderChangedListener: (position: Int) -> Unit

    init {
        setOnStickyHeaderChangedListener(this)
    }

    override
    fun onStickyHeaderChanged(stickyListHeadersListView: StickyListHeadersListView, header: View, position: Int, headerId: Long) =
        onHeaderChangedListener.invoke(position)
}
