package com.ltei.kunzmznzger

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.Gravity
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import android.view.ViewGroup.LayoutParams.WRAP_CONTENT
import android.widget.Button
import android.widget.LinearLayout
import android.widget.Toast
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.MobileAds
import com.ltei.kunzmznzger.local.LocalUserInfo
import com.ltei.kunzmznzger.models.Expense
import com.ltei.kunzmznzger.models.Message
import com.ltei.kunzmznzger.models.Room
import com.ltei.kunzmznzger.models.User
import com.ltei.kunzmznzger.models.dao.RoomDAO
import com.ltei.kunzmznzger.models.dao.UserDAO
import com.ltei.kunzmznzger.view.*
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.dialog_enter_text.*
import org.joda.time.DateTime
import java.util.concurrent.CompletableFuture


class MainActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        Toast.makeText(this,"caca",Toast.LENGTH_LONG)
        // Initialize globals
        MobileAds.initialize(this, getString(R.string.google_ad_banner_id))
        val adRequest = AdRequest.Builder().build()
        ad_banner.loadAd(adRequest)


        main_button_earns.setOnClickListener({
            val intent = Intent(this, HistoryActivity::class.java)
            val expenses = ArrayList<Expense>()
            expenses.add(Expense())
            expenses.add(Expense())
            expenses.add(Expense())
            intent.putExtra(HistoryActivity.EXTRAS_LIST, expenses)
            startActivity(intent)
        })

        main_button_dept.setOnClickListener({
            val intent = Intent(this, HistoryActivity::class.java)
            intent.putExtra(HistoryActivity.EXTRAS_LIST, ArrayList<Expense>())
            startActivity(intent)
        })

        val listItemLayoutParams = LinearLayout.LayoutParams(MATCH_PARENT, WRAP_CONTENT)
        listItemLayoutParams.bottomMargin = 1
        //val displaymetrics = DisplayMetrics()
        //val padding = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 16f, displaymetrics).toInt()
        //val textSize = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 12f, displaymetrics)
        listlinearlayout.init(arrayListOf<Room>(),
                { item, _ ->
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
                })

        main_button_create_group.setOnClickListener({
            val dialog = DialogEnterText(this, "Enter a name for the new room")
            dialog.setOnCancelListener({
                val name = dialog.edittext.text.toString()
                val room = Room()
                room.name = name
                room.addUser(LocalUserInfo.getInstance().getUser())
                room.save().thenAccept { LocalUserInfo.getInstance().load(this ,Runnable { onResume() }) }
                gotoActivityGroup(room)
            })
            dialog.show()
        })
    }

    override fun onResume() {
        super.onResume()
        if (!LocalUserInfo.getInstance().isCreated(this)) {
            val intent = Intent(this, UserCreationActivity::class.java)
            startActivity(intent)
            finish()
        } else {
            LocalUserInfo.getInstance().load(this, Runnable{
                listlinearlayout.setArray(LocalUserInfo.getInstance().getGroups())
            })
        }
    }

    fun gotoActivityGroup(groupInfo: Room) {
        val intent = Intent(this, RoomActivity::class.java)
        intent.putExtra(RoomActivity.EXTRAS_ROOM, groupInfo)
        startActivity(intent)
    }
}
