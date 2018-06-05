package com.ltei.kunzmznzger.view

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.Gravity
import android.view.MenuItem
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.PopupMenu
import android.widget.TextView
import com.ltei.kunzmznzger.R
import com.ltei.kunzmznzger.graph.Graph
import com.ltei.kunzmznzger.local.LocalUserInfo
import com.ltei.kunzmznzger.models.Expense
import com.ltei.kunzmznzger.models.Room
import kotlinx.android.synthetic.main.activity_graph.*
import kotlinx.android.synthetic.main.activity_history.*


class GraphActivity : AppCompatActivity() {


    companion object {
        const val EXTRAS_ROOM_IDX = "GROUP_ACTIVITY_EXTRAS_GROUP"

    }
    var roomIdx: Int = -1
    var room: Room? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_graph)

        roomIdx = intent.getIntExtra(GraphActivity.EXTRAS_ROOM_IDX, -1)
        room = LocalUserInfo.getInstance().getRooms()[roomIdx]

        Graph.plot_expense_detail(chart, room!!.expenses)

    }

    override fun onResume() {
        super.onResume()

    }


}

