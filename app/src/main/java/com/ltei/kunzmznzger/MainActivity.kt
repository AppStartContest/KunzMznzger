package com.ltei.kunzmznzger

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.widget.Toast
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.MobileAds
import com.ltei.kunzmznzger.local.LocalUserInfo
import com.ltei.kunzmznzger.view.DialogEnterText
import com.ltei.kunzmznzger.view.RoomActivity
import com.ltei.kunzmznzger.view.UserCreationActivity
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.dialog_enter_text.*


class MainActivity : AppCompatActivity() {



    private var buttonCreateRoomClickListener = View.OnClickListener {
        val dialog = DialogEnterText(this)
        dialog.runOnCreate = Runnable {
            dialog.dialog_enter_text_title.text = "Enter a name for the new room"
            dialog.dialog_enter_text_button.setOnClickListener {
                if (dialog.dialog_enter_text_edittext.text.toString() != "") {
                    LocalUserInfo.getInstance().createRoom(dialog.dialog_enter_text_edittext.text.toString()).thenAccept {
                        LocalUserInfo.getInstance().load(this).thenRun {
                            dialog.dismiss()
                            gotoActivityGroup(LocalUserInfo.getInstance().getRooms().size -1)
                        }
                    }
                } else {
                    Toast.makeText(this, getText(R.string.dialog_void_input_error), Toast.LENGTH_SHORT).show()
                }
            }
        }
        dialog.show()
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Initialize admob
        MobileAds.initialize(this, getString(R.string.google_ad_banner_id))

        // Initialize activity ad
        val adRequest = AdRequest.Builder().build()
        ad_banner.loadAd(adRequest)

        main_button_create_group.setOnClickListener(buttonCreateRoomClickListener)

    }


    override fun onPause() {
        super.onPause()
    }
    override fun onResume() {
        super.onResume()
        if (!LocalUserInfo.getInstance().isCreated(this)) {
            val intent = Intent(this, UserCreationActivity::class.java)
            startActivity(intent)
            finish()
        } else {
            LocalUserInfo.getInstance().load(this).thenRun {
                this.runOnUiThread {
                    text_title.text = "Welcome, ${LocalUserInfo.getInstance().getUser().username}"
                    roomlistview.setArray(LocalUserInfo.getInstance().getRooms())
                }
            }
        }
    }

    fun gotoActivityGroup(roomIdx: Int) {
        val intent = Intent(this, RoomActivity::class.java)
        intent.putExtra(RoomActivity.EXTRAS_ROOM_IDX, roomIdx)
        startActivity(intent)
    }
}
