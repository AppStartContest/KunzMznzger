package com.ltei.kunzmznzger.view

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.Window
import android.view.WindowManager
import android.widget.Toast
import com.ltei.kunzmznzger.R
import kotlinx.android.synthetic.main.dialog_enter_text.*


class DialogEnterText(context: Context, val title: String): Dialog(context) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        setContentView(R.layout.dialog_enter_text)

        val layoutParams = WindowManager.LayoutParams()
        layoutParams.copyFrom(window.attributes)
        layoutParams.width = WindowManager.LayoutParams.MATCH_PARENT
        window.attributes = layoutParams

        dialog_enter_text_title.text = title

        dialog_enter_text_button.setOnClickListener({
            if (edittext.text.toString() != "") {
                this@DialogEnterText.cancel()
            } else {
                Toast.makeText(this.context, "You have to enter a something!", Toast.LENGTH_SHORT).show()
            }
        })

    }

}