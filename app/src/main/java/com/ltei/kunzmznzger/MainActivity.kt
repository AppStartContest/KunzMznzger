package com.ltei.kunzmznzger

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.Gravity
import android.view.View
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import android.view.ViewGroup.LayoutParams.WRAP_CONTENT
import android.widget.Button
import android.widget.LinearLayout
import android.widget.Toast
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.MobileAds
import com.ltei.kunzmznzger.local.LocalUserInfo
import com.ltei.kunzmznzger.models.Room
import com.ltei.kunzmznzger.view.*
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.dialog_enter_text.*


class MainActivity : AppCompatActivity() {


    private var roomListItemViewCreator = ListLinearLayout.ViewCreator {
        item, idx ->
        val listItemLayoutParams = LinearLayout.LayoutParams(MATCH_PARENT, WRAP_CONTENT)
        listItemLayoutParams.bottomMargin = 1
        val view = Button(this)
        view.gravity = Gravity.CENTER
        // /view.layout = getDrawable(android.R.layout.simple_list_item_1)
        view.background = getDrawable(R.color.colorListItemBackground)
        view.setPadding(16, 16, 16, 16)
        view.layoutParams = listItemLayoutParams
        view.text = item.toString() //(item as Room).name
        view.textSize = 12f
        view.setOnClickListener({ gotoActivityGroup(idx) })
        view
    }

    private var buttonCreateRoomClickListener = View.OnClickListener {
        val dialog = DialogEnterText(this)
        dialog.runOnCreate = Runnable {
            dialog.dialog_enter_text_title.text = "Enter a name for the new room"
            dialog.dialog_enter_text_button.setOnClickListener {
                if (dialog.dialog_enter_text_edittext.text.toString() != "") {
                    LocalUserInfo.getInstance().createRoom(dialog.dialog_enter_text_edittext.text.toString()).thenAccept {
                        LocalUserInfo.getInstance().load(this).thenRun {
                            dialog.cancel()
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

        // Initialize globals
        MobileAds.initialize(this, getString(R.string.google_ad_banner_id))
        val adRequest = AdRequest.Builder().build()
        ad_banner.loadAd(adRequest)


        main_button_earns.setOnClickListener({ onButtonEarnsClicked() })
        main_button_dept.setOnClickListener({ onButtonDeptsClicked() })

        main_button_create_group.setOnClickListener(buttonCreateRoomClickListener)
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
                    listlinearlayout.init(LocalUserInfo.getInstance().getRooms(), roomListItemViewCreator)
                }
            }
        }
    }


    private fun onButtonEarnsClicked() {
        val intent = Intent(this, HistoryActivity::class.java)
        intent.putExtra(HistoryActivity.EXTRAS_LIST, LocalUserInfo.getInstance().getUser().expenses)
        startActivity(intent)
    }
    private fun onButtonDeptsClicked() {
        val intent = Intent(this, HistoryActivity::class.java)
        intent.putExtra(HistoryActivity.EXTRAS_LIST, LocalUserInfo.getInstance().getUser().expenses)
        startActivity(intent)
    }
    fun gotoActivityGroup(roomIdx: Int) {
        val intent = Intent(this, RoomActivity::class.java)
        intent.putExtra(RoomActivity.EXTRAS_ROOM_IDX, roomIdx)
        startActivity(intent)
    }
}
