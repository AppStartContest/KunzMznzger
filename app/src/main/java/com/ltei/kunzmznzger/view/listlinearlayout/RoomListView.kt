package com.ltei.kunzmznzger.view.listlinearlayout

import android.content.Context
import android.content.Intent
import android.util.AttributeSet
import android.view.View
import android.widget.LinearLayout
import com.ltei.kunzmznzger.R
import com.ltei.kunzmznzger.models.Room
import com.ltei.kunzmznzger.view.RoomActivity
import kotlinx.android.synthetic.main.layout_room_list_item.view.*

class RoomListView: ListLinearLayout {


    init {
        setViewCreator({
            item, idx ->
            val layout = LinearLayout(context)
            View.inflate(context, R.layout.layout_room_list_item, layout)

            layout.list_item.text = (item as Room).name
            layout.setOnClickListener {
                val intent = Intent(context, RoomActivity::class.java)
                intent.putExtra(RoomActivity.EXTRAS_ROOM_IDX, idx)
                context.startActivity(intent)
            }

            layout
        })
    }
    constructor(context: Context): super(context) {
        this.orientation = LinearLayout.VERTICAL
    }
    constructor(context: Context, attributeSet: AttributeSet): super(context, attributeSet) {
        this.orientation = LinearLayout.VERTICAL
    }

}