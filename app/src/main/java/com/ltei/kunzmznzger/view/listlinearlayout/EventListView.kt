package com.ltei.kunzmznzger.view.listlinearlayout

import android.content.Context
import android.content.Intent
import android.util.AttributeSet
import android.view.View
import android.widget.LinearLayout
import com.ltei.kunzmznzger.R
import com.ltei.kunzmznzger.models.Event
import com.ltei.kunzmznzger.view.EventActivity
import kotlinx.android.synthetic.main.layout_room_list_item.view.*
import java.util.*

class EventListView: ListLinearLayout {


    init {
        setViewCreator({
            item, idx ->
            val layout = LinearLayout(context)
            View.inflate(context, R.layout.layout_event_list_item, layout)

            layout.list_item.text = (item as Event).name
            layout.setOnClickListener {
                val intent = Intent(context, EventActivity::class.java)
                intent.putExtra(EventActivity.EXTRAS_ROOM_IDX, this.roomIdx)
                intent.putExtra(EventActivity.EXTRAS_EVENT_IDX_IN_ROOM, idx)
                context.startActivity(intent)
            }

            layout
        })
    }

    private var roomIdx: Int = -1

    constructor(context: Context): super(context) {
        this.orientation = LinearLayout.VERTICAL
    }
    constructor(context: Context, attributeSet: AttributeSet): super(context, attributeSet) {
        this.orientation = LinearLayout.VERTICAL
    }

    fun init(array: ArrayList<*>, roomIdx: Int) {
        setArray(array)
        this.roomIdx = roomIdx
    }

}