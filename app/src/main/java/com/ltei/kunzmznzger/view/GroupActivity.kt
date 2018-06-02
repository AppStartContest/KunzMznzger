package com.ltei.kunzmznzger.view

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.PopupMenu
import android.widget.Toast
import com.ltei.kunzmznzger.R
import kotlinx.android.synthetic.main.activity_group.*


class GroupActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_group)

        button_menu.setOnClickListener({
            val dropDown = PopupMenu(applicationContext, button_menu)
            dropDown.menuInflater.inflate(R.menu.menu_group_items, dropDown.menu)
            dropDown.setOnMenuItemClickListener({
                Toast.makeText(applicationContext, "Clicked "+it.title, Toast.LENGTH_SHORT).show()
                true
            })
            dropDown.show()
        })

    }

}