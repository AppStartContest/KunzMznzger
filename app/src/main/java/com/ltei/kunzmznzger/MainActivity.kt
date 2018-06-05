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
import com.ltei.kunzmznzger.models.generateDummy
import com.ltei.kunzmznzger.view.*
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.dialog_enter_text.*


class MainActivity : AppCompatActivity() {


    private var roomListItemViewCreator = ListLinearLayout.ViewCreator {
        item, _ ->
        val listItemLayoutParams = LinearLayout.LayoutParams(MATCH_PARENT, WRAP_CONTENT)
        listItemLayoutParams.bottomMargin = 1
        val view = Button(this)
        view.gravity = Gravity.CENTER
        // /view.layout = getDrawable(android.R.layout.simple_list_item_1)
        view.background = getDrawable(R.color.colorListItemBackground)
        view.setPadding(16, 16, 16, 16)
        view.layoutParams = listItemLayoutParams
        view.text = item.toString()
        view.textSize = 12f
        view.setOnClickListener({ gotoActivityGroup(item as Room) })
        view
    }

    private var buttonCreateRoomClickListener = View.OnClickListener {
        val dialog = DialogEnterText(this, "Enter a name for the new room")
        dialog.setOnDismissListener({
            LocalUserInfo.getInstance().createRoom(dialog.dialog_enter_text_edittext.text.toString()).thenAccept {
                //Toast.makeText(this, it.rooms.size, Toast.LENGTH_SHORT).show()
                gotoActivityGroup(it.rooms.last())
            }
        })
        dialog.show()
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        Toast.makeText(this,"caca",Toast.LENGTH_LONG)

        val room = generateDummy()
        val depts = room.computeDepts()

        // Initialize globals
        MobileAds.initialize(this, getString(R.string.google_ad_banner_id))
        val adRequest = AdRequest.Builder().build()
        ad_banner.loadAd(adRequest)


        main_button_earns.setOnClickListener({ onButtonEarnsClicked() })
        main_button_dept.setOnClickListener({ onButtonDeptsClicked() })

        main_button_create_group.setOnClickListener(buttonCreateRoomClickListener)

        // TEST
        button_TEST.setOnClickListener {
            val intent = Intent(this, EventCreationActivity::class.java)
            startActivity(intent)
        }
    }


    override fun onResume() {
        super.onResume()
        if (!LocalUserInfo.getInstance().isCreated(this)) {
            val intent = Intent(this, UserCreationActivity::class.java)
            startActivity(intent)
            finish()
        } else {
            LocalUserInfo.getInstance().load(this, Runnable {
                listlinearlayout.init(LocalUserInfo.getInstance().getRooms(), roomListItemViewCreator)
            })
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
    fun gotoActivityGroup(groupInfo: Room) {
        val intent = Intent(this, RoomActivity::class.java)
        intent.putExtra(RoomActivity.EXTRAS_ROOM, groupInfo)
        startActivity(intent)
    }
}
