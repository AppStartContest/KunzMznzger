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
import kotlinx.android.synthetic.main.dialog_create_expense.*
import kotlinx.android.synthetic.main.dialog_enter_text.*




class RoomActivity : AppCompatActivity() {

    companion object {
        const val EXTRAS_ROOM_IDX = "GROUP_ACTIVITY_EXTRAS_GROUP"
    }



    private var room: Room? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_room)

        val roomIdx = intent.getIntExtra(EXTRAS_ROOM_IDX, -1)
        room = LocalUserInfo.getInstance().getRooms()[roomIdx]
        text_title.text = room.toString()

        button_menu.setOnClickListener({
            val dropDown = PopupMenu(applicationContext, button_menu)
            dropDown.menuInflater.inflate(R.menu.menu_group_items, dropDown.menu)
            dropDown.setOnMenuItemClickListener({
                when (it.itemId) {
                    R.id.menu_group_items_add_member -> { onButtonAddMemberPressed() }
                    R.id.menu_group_items_add_message -> { onButtonAddMessagePressed(room!!) }
                    R.id.menu_group_items_add_event -> { onButtonAddEventPressed() }
                    R.id.menu_group_items_history -> { onButtonHistoryPressed() }
                    R.id.menu_group_items_graph -> { onButtonGraphPressed() }
                    else -> throw IllegalStateException()
                }
                true
            })
            dropDown.show()
        })

        button_add_expense.setOnClickListener { onButtonCreateExpensePressed() }

    }



    fun onButtonCreateExpensePressed() {
        val dialog = DialogCreateExpense(this)
        dialog.runOnCreate = Runnable {
            dialog.button_create.setOnClickListener {
                if (dialog.edittext_title.text.toString() == "" || dialog.edittext_amount.text.toString() == "") {
                    Toast.makeText(dialog.context, "You have to fill title and amount!", Toast.LENGTH_SHORT).show()
                } else {
                    LocalUserInfo.getInstance().createExpense(
                            dialog.edittext_title.text.toString(),
                            dialog.edittext_amount.text.toString().toDouble(),
                            dialog.edittext_description.text.toString(),
                            room!!
                    ).thenRun { dialog.dismiss() }
                }
            }
        }
        dialog.show()
    }

    fun onButtonAddMemberPressed() {

    }

    fun onButtonAddMessagePressed(room: Room) {
        val dialog = DialogEnterText(this)
        dialog.runOnCreate = Runnable {
            dialog.dialog_enter_text_title.text = "Write a message"
            dialog.dialog_enter_text_button.setOnClickListener({
                if (dialog.dialog_enter_text_edittext.text.toString() != "") {
                    LocalUserInfo.globalInstance.sendMessageToRoom(dialog.dialog_enter_text_edittext.text.toString(), room).thenRun {
                        dialog.cancel()
                    }
                } else {
                    Toast.makeText(this, getText(R.string.dialog_void_input_error), Toast.LENGTH_SHORT).show()
                }
            })
        }
        dialog.show()
    }

    fun onButtonAddEventPressed() {
        val intent = Intent(this, EventCreationActivity::class.java)
        intent.putExtra(EventCreationActivity.EXTRAS_ROOM, room)
        startActivity(intent)
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