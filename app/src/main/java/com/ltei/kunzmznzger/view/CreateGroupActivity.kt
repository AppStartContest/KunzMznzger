/*package com.ltei.kunzmznzger.view

import android.content.DialogInterface
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_create_group.*
import kotlinx.android.synthetic.main.dialog_enter_text.*

class CreateGroupActivity : AppCompatActivity() {

    val members_pseudo = ArrayList<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_group)


        create_group_listview_members.adapter = ArrayAdapter(this.applicationContext, android.R.layout.simple_list_item_1, members_pseudo)
        create_group_listview_members.onItemClickListener = AdapterView.OnItemClickListener({
            _, _, position, _ ->
            val listener = DialogInterface.OnClickListener({
                _, which ->
                if (which == DialogInterface.BUTTON_POSITIVE) {
                    members_pseudo.removeAt(position)
                    create_group_listview_members.adapter = ArrayAdapter(this.applicationContext, android.R.layout.simple_list_item_1, members_pseudo)
                }
            })
            val builder = AlertDialog.Builder(this)
            builder.setMessage("Delete this member?")
            builder.setPositiveButton("Yes", listener)
            builder.setNegativeButton("No", listener)
            builder.show()
        })

        create_group_button_add_members.setOnClickListener({
            val dialog = DialogEnterText(this, "Enter a pseudo", members_pseudo)
            dialog.setOnCancelListener({
                members_pseudo.add(dialog.dialog_enter_text_edittext.text.toString())
                create_group_listview_members.adapter = ArrayAdapter(this.applicationContext, android.R.layout.simple_list_item_1, members_pseudo)
            })
            dialog.show()
        })

        create_group_button_create.setOnClickListener({
            if (create_group_edittext_name.text.toString() != "") {
                //TODO Create group
            } else {
                Toast.makeText(this@CreateGroupActivity, "You have to enter a name!", Toast.LENGTH_SHORT).show()
            }
        })

    }

}*/