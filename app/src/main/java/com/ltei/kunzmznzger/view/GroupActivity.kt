package com.ltei.kunzmznzger.view

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.PopupMenu
import android.widget.Toast
import com.ltei.kunzmznzger.R
import com.ltei.kunzmznzger.local.LocalGroupInfo
import kotlinx.android.synthetic.main.activity_group.*


class GroupActivity : AppCompatActivity() {

    companion object {
        const val EXTRAS_GROUP = "GROUP_ACTIVITY_EXTRAS_GROUP"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_group)

        var group = intent.getSerializableExtra(EXTRAS_GROUP) as LocalGroupInfo

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