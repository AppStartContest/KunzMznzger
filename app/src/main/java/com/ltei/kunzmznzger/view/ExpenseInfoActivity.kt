package com.ltei.kunzmznzger.view

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.Toast
import com.google.android.gms.ads.AdRequest
import com.ltei.kunzmznzger.R
import com.ltei.kunzmznzger.local.LocalUserInfo
import com.ltei.kunzmznzger.models.Expense
import kotlinx.android.synthetic.main.activity_expense_info.*
import kotlinx.android.synthetic.main.dialog_enter_text.*

class ExpenseInfoActivity : AppCompatActivity() {

    companion object {
        const val EXTRAS_ROOM_IDX = "EXPENSE_INFO_ACTIVITY_EXTRAS_ROOM_IDX"
        const val EXTRAS_EXPENSE_IDX_IN_ROOM = "EXPENSE_INFO_ACTIVITY_EXTRAS_EXPENSE_IDX_IN_ROOM"
    }


    private var roomIdx: Int = -1
    private var expenseIdxInRoom: Int = -1
    private fun getExpense(): Expense {
        return LocalUserInfo.getInstance().getRooms()[roomIdx].expenses[expenseIdxInRoom]
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_expense_info)

        // Initialize activity ad
        val adRequest = AdRequest.Builder().build()
        ad_banner.loadAd(adRequest)


        roomIdx = intent.getIntExtra(EXTRAS_ROOM_IDX, -1)
        expenseIdxInRoom = intent.getIntExtra(EXTRAS_EXPENSE_IDX_IN_ROOM, -1)

        text_title.text = getExpense().description
        text_amount.text = "spent ${getExpense().value}$"
        messagesview.setArray(ArrayList(getExpense().messages))
        button_add_message.setOnClickListener { onButtonAddMessageClickListener() }
    }

    fun onButtonAddMessageClickListener() {
        val dialog = DialogEnterText(this)
        dialog.runOnCreate = Runnable {
            dialog.dialog_enter_text_title.text = "Write a message"
            dialog.dialog_enter_text_button.setOnClickListener {
                if (dialog.dialog_enter_text_edittext.text.toString() != "") {
                    LocalUserInfo.getInstance().sendMessageToExpense(dialog.dialog_enter_text_edittext.text.toString(), getExpense())
                } else {
                    Toast.makeText(this, getText(R.string.dialog_void_input_error), Toast.LENGTH_SHORT).show()
                }
            }
        }
        dialog.show()
    }


}