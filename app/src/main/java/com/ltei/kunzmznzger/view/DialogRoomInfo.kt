package com.ltei.kunzmznzger.view

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.Window
import com.ltei.kunzmznzger.R

class DialogRoomInfo(context: Context): Dialog(context) {


    var runOnCreate: Runnable? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        setContentView(R.layout.dialog_room_info)

        if (runOnCreate != null) {
            runOnCreate!!.run()
        }
    }

}