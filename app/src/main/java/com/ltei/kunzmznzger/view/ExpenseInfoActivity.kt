package com.ltei.kunzmznzger.view

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout
import android.widget.Toast
import com.ltei.kunzmznzger.R
import com.ltei.kunzmznzger.local.LocalUserInfo
import com.ltei.kunzmznzger.models.Expense
import com.ltei.kunzmznzger.models.Message
import com.ltei.kunzmznzger.models.User
import kotlinx.android.synthetic.main.activity_expense_info.*
import kotlinx.android.synthetic.main.dialog_enter_text.*

class ExpenseInfoActivity : AppCompatActivity() {

    companion object {
        const val EXTRAS_EXPENSE = "EXPENSE_INFO_ACTIVITY_EXTRAS_EXPENSE"
    }


    private var expense: Expense? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_expense_info)

        expense = intent.getSerializableExtra(EXTRAS_EXPENSE) as Expense
        text_title.text = expense!!.name
        text_amount.text = "spent ${expense!!.value}$"
        text_description.text = expense!!.description
        messagesview.setArray(ArrayList(expense!!.messages))
        button_add_message.setOnClickListener { onButtonAddMessageClickListener() }
    }

    fun onButtonAddMessageClickListener() {
        val dialog = DialogEnterText(this)
        dialog.runOnCreate = Runnable {
            dialog.dialog_enter_text_title.text = "Write a message"
            dialog.dialog_enter_text_button.setOnClickListener {
                if (dialog.dialog_enter_text_edittext.text.toString() != "") {
                    LocalUserInfo.getInstance().sendMessageToExpense(dialog.dialog_enter_text_edittext.text.toString(), expense!!)
                } else {
                    Toast.makeText(this, getText(R.string.dialog_void_input_error), Toast.LENGTH_SHORT).show()
                }
            }
        }
        dialog.show()
    }


}