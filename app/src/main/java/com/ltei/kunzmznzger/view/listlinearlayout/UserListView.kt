package com.ltei.kunzmznzger.view.listlinearlayout

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.LinearLayout
import com.ltei.kunzmznzger.R
import com.ltei.kunzmznzger.models.User
import kotlinx.android.synthetic.main.layout_room_list_item.view.*

class UserListView: ListLinearLayout {

    init {
        setViewCreator({
            item, idx ->
            val layout = LinearLayout(context)
            View.inflate(context, R.layout.layout_user_list_item, layout)

            layout.list_item.text = (item as User).name

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