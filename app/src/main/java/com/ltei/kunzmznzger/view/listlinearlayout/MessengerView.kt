package com.ltei.kunzmznzger.view.listlinearlayout

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.LinearLayout
import com.ltei.kunzmznzger.R
import com.ltei.kunzmznzger.models.Message
import kotlinx.android.synthetic.main.layout_messenger_message.view.*

class MessengerView: ListLinearLayout {

    init {
        setViewCreator({
            item, idx ->
            val item = item as Message
            val layout = LinearLayout(context)
            View.inflate(context, R.layout.layout_messenger_message, layout)

            layout.text_user.text = item.user!!.username
            layout.text_content.text = item.content
            layout.text_datetime.text = item.createdAt.toString()

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


