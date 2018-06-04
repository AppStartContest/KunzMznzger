package com.ltei.kunzmznzger.view

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.PopupMenu
import android.widget.Toast
import com.ltei.kunzmznzger.R
import com.ltei.kunzmznzger.local.LocalUserInfo
import com.ltei.kunzmznzger.models.Room
import kotlinx.android.synthetic.main.activity_room.*
import kotlinx.android.synthetic.main.dialog_enter_text.*


class RoomActivity : AppCompatActivity() {

    companion object {
        const val EXTRAS_ROOM = "GROUP_ACTIVITY_EXTRAS_GROUP"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_room)

        val room = intent.getSerializableExtra(EXTRAS_ROOM) as Room

        button_menu.setOnClickListener({
            val dropDown = PopupMenu(applicationContext, button_menu)
            dropDown.menuInflater.inflate(R.menu.menu_group_items, dropDown.menu)
            dropDown.setOnMenuItemClickListener({
                when (it.itemId) {
                    R.id.menu_group_items_add_member -> { onButtonAddMemberPressed() }
                    R.id.menu_group_items_add_message -> { onButtonAddMessagePressed(room) }
                    R.id.menu_group_items_add_event -> { onButtonAddEventPressed() }
                    R.id.menu_group_items_history -> { onButtonHistoryPressed() }
                    R.id.menu_group_items_graph -> { onButtonGraphPressed() }
                    else -> throw IllegalStateException()
                }
                Toast.makeText(applicationContext, "Clicked "+it.title, Toast.LENGTH_SHORT).show()
                true
            })
            dropDown.show()
        })

    }


    fun onButtonAddMemberPressed() {

    }

    fun onButtonAddMessagePressed(room: Room) {
        val dialog = DialogEnterText(this, "Write a message")
        dialog.dialog_enter_text_button.setOnClickListener({
            if (dialog.edittext.text.toString() != "") {
                LocalUserInfo.globalInstance.sendMessageToRoom(edittext.text.toString(), room).thenRun {
                    dialog.cancel()
                }
            } else {
                Toast.makeText(this, getText(R.string.dialog_void_input_error), Toast.LENGTH_SHORT).show()
            }
        })
    }

    fun onButtonAddEventPressed() {
        /*val dialog = DialogEnterText(this, "Enter the title")
        dialog.dialog_enter_text_button.setOnClickListener({
            if (dialog.edittext.text.toString() != "") {
                val event = Event()
                event.name = dialog.edittext.text.toString()
                LocalUserInfo.globalInstance.createEvent(room).thenRun {
                    dialog.cancel()
                }
            } else {
                Toast.makeText(this, getText(R.string.dialog_void_input_error), Toast.LENGTH_SHORT).show()
            }
        })*/
    }

    fun onButtonHistoryPressed() {
        val intent = Intent(this, HistoryActivity::class.java)
        intent.putExtra(HistoryActivity.EXTRAS_LIST, LocalUserInfo.globalInstance.getUser().expenses)
        startActivity(intent)
    }

    fun onButtonGraphPressed() {

    }

}