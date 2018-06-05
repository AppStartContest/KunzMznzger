package com.ltei.kunzmznzger.view

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.Toast
import com.google.android.gms.ads.AdRequest
import com.ltei.kunzmznzger.R
import com.ltei.kunzmznzger.local.LocalUserInfo
import com.ltei.kunzmznzger.models.Event
import com.ltei.kunzmznzger.models.Room
import kotlinx.android.synthetic.main.activity_event.*
import kotlinx.android.synthetic.main.dialog_enter_text.*
import java.text.SimpleDateFormat

class EventActivity : AppCompatActivity() {


    companion object {
        const val EXTRAS_ROOM_IDX = "EVENT_CREATION_ACTIVITY_EXTRAS_ROOM_IDX"
        const val EXTRAS_EVENT_IDX_IN_ROOM = "EVENT_CREATION_ACTIVITY_EXTRAS_EVENT_IDX_IN_ROOM"
    }


    var roomIdx: Int = -1
    var eventIdxInRoom: Int = -1
    private fun getRoom(): Room { return LocalUserInfo.getInstance().getRooms()[roomIdx] }
    private fun getEvent(): Event { return getRoom().events[eventIdxInRoom] }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_event)

        // Initialize activity ad
        val adRequest = AdRequest.Builder().build()
        ad_banner.loadAd(adRequest)



        roomIdx = intent.getIntExtra(EXTRAS_ROOM_IDX, -1)
        eventIdxInRoom = intent.getIntExtra(EXTRAS_EVENT_IDX_IN_ROOM, -1)

        val room = getRoom()
        val event = getEvent()
        text_title.text = event.name
        text_description.text = event.description
        val sdf = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
        text_datetime.text = sdf.format(event.date!!.toDateTime().toDate())
        messengerview.setArray(event.messages)
        button_add_message.setOnClickListener { onButtonAddMessagePressed(event) }
    }

    fun onButtonAddMessagePressed(event: Event) {
        val dialog = DialogEnterText(this)
        dialog.runOnCreate = Runnable {
            dialog.dialog_enter_text_title.text = "Write a message"
            dialog.dialog_enter_text_button.setOnClickListener({
                if (dialog.dialog_enter_text_edittext.text.toString() != "") {
                    LocalUserInfo.globalInstance.sendMessageToEvent(dialog.dialog_enter_text_edittext.text.toString(), event).thenRun {
                        LocalUserInfo.getInstance().load(this).thenRun {
                            this.runOnUiThread {
                                dialog.cancel()
                                messengerview.setArray(getEvent().messages)
                            }
                        }
                    }
                } else {
                    Toast.makeText(this, getText(R.string.dialog_void_input_error), Toast.LENGTH_SHORT).show()
                }
            })
        }
        dialog.show()
    }

}