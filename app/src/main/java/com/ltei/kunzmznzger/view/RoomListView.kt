package com.ltei.kunzmznzger.view

import android.content.Context
import android.content.Intent
import android.util.AttributeSet
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import com.ltei.kunzmznzger.R
import com.ltei.kunzmznzger.models.Room
import com.ltei.kunzmznzger.models.User

class RoomListView: ListLinearLayout {


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
            view.text = (item as Room).name
            view.textSize = 12f
            view.setOnClickListener {
                val intent = Intent(context, RoomActivity::class.java)
                intent.putExtra(RoomActivity.EXTRAS_ROOM_IDX, idx)
                context.startActivity(intent)
            }
            view
        })
    }
    constructor(context: Context): super(context) {
        this.orientation = LinearLayout.VERTICAL
    }
    constructor(context: Context, attributeSet: AttributeSet): super(context, attributeSet) {
        this.orientation = LinearLayout.VERTICAL
    }

}