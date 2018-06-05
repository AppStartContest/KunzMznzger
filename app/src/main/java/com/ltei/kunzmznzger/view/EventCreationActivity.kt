package com.ltei.kunzmznzger.view

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.widget.Toast
import com.ltei.kunzmznzger.R
import com.ltei.kunzmznzger.libs.time.Date
import com.ltei.kunzmznzger.libs.time.Time
import com.ltei.kunzmznzger.local.LocalUserInfo
import com.ltei.kunzmznzger.models.Room
import kotlinx.android.synthetic.main.activity_event_creation.*
import org.joda.time.DateTime

class EventCreationActivity: AppCompatActivity() {

    companion object {
        const val EXTRAS_ROOM_IDX = "EVENT_CREATION_ACTIVITY_EXTRAS_ROOM_IDX"
    }


    var room: Room? = null
    var viewIdx = 0

    private var buttonNextClickListener1 = View.OnClickListener{
        if (edittext_name.text.toString() == "") {
            Toast.makeText(this, getString(R.string.dialog_void_input_error), Toast.LENGTH_SHORT).show()
        } else {
            viewIdx = 1
            onViewIdxChange()
        }
    }
    private var buttonNextClickListener2 = View.OnClickListener {
        viewIdx = 2
        onViewIdxChange()
    }
    private var buttonConfirmClickListener = View.OnClickListener {
        val datetime = DateTime(
                datepicker.year,
                datepicker.month,
                datepicker.dayOfMonth,
                timepicker.hour,
                timepicker.minute,
                0, 0)

        LocalUserInfo.getInstance().createEvent(
                edittext_name.text.toString(),
                edittext_description.text.toString(),
                Date(datetime),
                Time(datetime),
                room!!).thenRun {
            finish()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_event_creation)

        room = LocalUserInfo.getInstance().getRooms()[intent.getIntExtra(EXTRAS_ROOM_IDX, -1)]
        onViewIdxChange()
    }


    private fun onViewIdxChange() {
        when (viewIdx) {
            0 -> { hideView1(); hideView2(); showView0() }
            1 -> { hideView0(); hideView2(); showView1() }
            2 -> { hideView0(); hideView1(); showView2() }
            else -> throw IllegalStateException()
        }
    }

    private fun showView0() {
        text_name.visibility = View.VISIBLE
        edittext_name.visibility = View.VISIBLE
        text_description.visibility = View.VISIBLE
        edittext_description.visibility = View.VISIBLE
        button_next.visibility = View.VISIBLE
        button_next.setOnClickListener(buttonNextClickListener1)
    }
    private fun hideView0() {
        text_name.visibility = View.GONE
        edittext_name.visibility = View.GONE
        text_description.visibility = View.GONE
        edittext_description.visibility = View.GONE
        button_next.visibility = View.GONE
    }

    private fun showView1() {
        datepicker.visibility = View.VISIBLE
        button_next.visibility = View.VISIBLE
        button_next.setOnClickListener(buttonNextClickListener2)
    }
    private fun hideView1() {
        datepicker.visibility = View.GONE
        button_next.visibility = View.GONE
    }

    private fun showView2() {
        timepicker.visibility = View.VISIBLE
        button_confirm.visibility = View.VISIBLE
        button_confirm.setOnClickListener(buttonConfirmClickListener)
    }
    private fun hideView2() {
        timepicker.visibility = View.GONE
        button_confirm.visibility = View.GONE
    }


    override fun onBackPressed() {
        when (viewIdx) {
            0 -> super.onBackPressed()
            1 -> {
                Toast.makeText(this, "Hello1", Toast.LENGTH_SHORT).show()
                viewIdx = 0; onViewIdxChange() }
            2 -> {
                Toast.makeText(this, "Hello1", Toast.LENGTH_SHORT).show()
                viewIdx = 1; onViewIdxChange() }
            else -> throw IllegalStateException()
        }
    }



}