package com.ltei.kunzmznzger.view

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.PopupMenu
import android.widget.Toast
import com.google.android.gms.ads.AdRequest
import com.ltei.kunzmznzger.R
import com.ltei.kunzmznzger.local.LocalUserInfo
import com.ltei.kunzmznzger.models.Room
import kotlinx.android.synthetic.main.activity_room.*
import kotlinx.android.synthetic.main.dialog_create_expense.*
import kotlinx.android.synthetic.main.dialog_enter_text.*
import kotlinx.android.synthetic.main.dialog_room_info.*


class RoomActivity : AppCompatActivity() {

    companion object {
        const val EXTRAS_ROOM_IDX = "GROUP_ACTIVITY_EXTRAS_GROUP"
    }



    private var roomIdx: Int? = null
    private fun getRoom(): Room { return LocalUserInfo.getInstance().getRooms()[roomIdx!!] }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_room)

        // Initialize activity ad
        val adRequest = AdRequest.Builder().build()
        ad_banner.loadAd(adRequest)


        roomIdx = intent.getIntExtra(EXTRAS_ROOM_IDX, -1)
        text_title.text = "Room : ${getRoom().name}"

        button_menu.setOnClickListener({
            val dropDown = PopupMenu(applicationContext, button_menu)
            dropDown.menuInflater.inflate(R.menu.menu_group_items, dropDown.menu)
            dropDown.setOnMenuItemClickListener({
                when (it.itemId) {
                    R.id.menu_group_items_add_member -> { onButtonAddMemberPressed() }
                    R.id.menu_group_items_add_event -> { onButtonAddEventPressed() }
                    R.id.menu_group_items_history -> { onButtonHistoryPressed() }
                    R.id.menu_group_items_graph -> { onButtonGraphPressed() }
                    R.id.menu_group_items_info -> { onButtonInfoPressed() }
                    else -> throw IllegalStateException()
                }
                true
            })
            dropDown.show()
        })

        button_add_message.setOnClickListener { onButtonAddMessagePressed() }
        button_add_expense.setOnClickListener { onButtonCreateExpensePressed() }

        userlistview.setArray(getRoom().users)
        messengerview.setArray(getRoom().messages)
        eventlistview.init(getRoom().events, roomIdx!!)
    }

    override fun onResume() {
        super.onResume()
        userlistview.setArray(getRoom().users)
        messengerview.setArray(getRoom().messages)
        eventlistview.init(getRoom().events, roomIdx!!)
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
                            getRoom()
                    ).thenRun {
                        LocalUserInfo.getInstance().load(this).thenRun {
                            dialog.dismiss()
                        }
                    }
                }
            }
        }
        dialog.show()
    }


    fun onButtonAddMemberPressed() {
        val dialog = DialogEnterText(this)
        dialog.runOnCreate = Runnable {
            dialog.dialog_enter_text_title.text = "Enter username to add:"
            dialog.dialog_enter_text_button.setOnClickListener({
                if (dialog.dialog_enter_text_edittext.text.toString() != "") {
                    LocalUserInfo.globalInstance.addUserToRoom(dialog.dialog_enter_text_edittext.text.toString(), getRoom()).thenRun {
                        this.runOnUiThread {
                            userlistview.setArray(getRoom().users)
                            dialog.dismiss()
                        }
                    }
                } else {
                    Toast.makeText(this, getText(R.string.dialog_void_input_error), Toast.LENGTH_SHORT).show()
                }
            })
        }
        dialog.show()
    }

    fun onButtonAddMessagePressed() {
        val dialog = DialogEnterText(this)
        dialog.runOnCreate = Runnable {
            dialog.dialog_enter_text_title.text = "Write a message"
            dialog.dialog_enter_text_button.setOnClickListener({
                if (dialog.dialog_enter_text_edittext.text.toString() != "") {
                    LocalUserInfo.getInstance().sendMessageToRoom(dialog.dialog_enter_text_edittext.text.toString(), getRoom()).thenRun {
                        LocalUserInfo.getInstance().load(this).thenRun {
                            dialog.dismiss()
                            this.runOnUiThread { messengerview.setArray(getRoom().messages) }
                        }
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
        intent.putExtra(EventCreationActivity.EXTRAS_ROOM_IDX, roomIdx)
        startActivity(intent)
    }

    fun onButtonHistoryPressed() {
        val intent = Intent(this, HistoryActivity::class.java)
        intent.putExtra(HistoryActivity.EXTRAS_ROOM_IDX, roomIdx)
        startActivity(intent)
    }

    fun onButtonGraphPressed() {
        if (getRoom().users.size == 1) {
            Toast.makeText(this, "There is no info as your are alone in this room", Toast.LENGTH_SHORT).show()
        } else {
            val intent = Intent(this, GraphActivity::class.java)
            intent.putExtra(GraphActivity.EXTRAS_ROOM_IDX, roomIdx)
            startActivity(intent)
        }
    }

    fun onButtonInfoPressed() {
        if (getRoom().users.size == 1) {
            Toast.makeText(this, "There is no info as your are alone in this room", Toast.LENGTH_SHORT).show()
        } else {
            val dialog = DialogRoomInfo(this)
            dialog.runOnCreate = Runnable {
                dialog.roomdebtsinfolistview.setArray(getRoom().computeDepts())
            }
            dialog.show()
        }
    }

}