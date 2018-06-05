package com.ltei.kunzmznzger.view.listlinearlayout

import android.content.Context
import android.content.Intent
import android.util.AttributeSet
import android.view.Gravity
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import com.ltei.kunzmznzger.R
import com.ltei.kunzmznzger.models.Event
import com.ltei.kunzmznzger.view.EventActivity
import java.util.*

class EventListView: ListLinearLayout {


    init {
        setViewCreator({
            item, idx ->
            val listItemLayoutParams = LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
            listItemLayoutParams.bottomMargin = 1
            val view = TextView(context)
            view.gravity = Gravity.CENTER
            view.background = context.getDrawable(R.color.colorListItemBackground)
            view.setPadding(16, 16, 16, 16)
            view.layoutParams = listItemLayoutParams
            view.text = (item as Event).name
            view.textSize = 12f
            view.setOnClickListener {
                val intent = Intent(context, EventActivity::class.java)
                intent.putExtra(EventActivity.EXTRAS_ROOM_IDX, this.roomIdx)
                intent.putExtra(EventActivity.EXTRAS_EVENT_IDX_IN_ROOM, idx)
                context.startActivity(intent)
            }
            view
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