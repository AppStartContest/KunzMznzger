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
import com.ltei.kunzmznzger.models.Expense
import com.ltei.kunzmznzger.models.Message
import com.ltei.kunzmznzger.models.User
import kotlinx.android.synthetic.main.activity_expense_info.*
import kotlinx.android.synthetic.main.dialog_enter_text.*

class ExpenseInfoActivity : AppCompatActivity() {

    companion object {
        const val EXTRAS_EXPENSE = "EXPENSE_INFO_ACTIVITY_EXTRAS_EXPENSE"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_expense_info)

        val expense = intent.getSerializableExtra(EXTRAS_EXPENSE) as Expense
        val receivers = expense.users
        for (i in 0..receivers.size) {
            if (receivers[i].id == expense.user!!.id) {
                receivers.removeAt(i)
                break
            }
        }

        text_title.text = expense.description
        text_amount.text = "spent ${expense.value}$ for"
        listlinearlayout_receivers.init(receivers, { item, _ -> createReceiverItemView(item as User) })
        listlinearlayout_messages.init(ArrayList(expense.messages), { item, _ -> createMessageItemView(item as Message) })
        button_add_message.setOnClickListener { onButtonAddMessageClickListener(expense) }
    }

    fun onButtonAddMessageClickListener(expense: Expense) {
        val dialog = DialogEnterText(this)
        dialog.runOnCreate = Runnable {
            dialog.dialog_enter_text_title.text = "Write a message"
            dialog.dialog_enter_text_button.setOnClickListener {
                if (dialog.dialog_enter_text_edittext.text.toString() != "") {
                } else {
                    Toast.makeText(this, getText(R.string.dialog_void_input_error), Toast.LENGTH_SHORT).show()
                }
            }
        }
        dialog.show()
    }

    fun createReceiverItemView(item: User): View {
        val layoutParams = LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        layoutParams.bottomMargin = 1

        val view = Button(this)
        view.gravity = Gravity.CENTER
        // /view.layout = getDrawable(android.R.layout.simple_list_item_1)
        view.background = getDrawable(R.color.colorListItemBackground)
        view.setPadding(16, 16, 16, 16)
        view.layoutParams = layoutParams
        view.text = "$item.name (${item.username})"
        view.textSize = 12f
        return view
    }
    fun createMessageItemView(item: Message): View {
        val layoutParams = LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        layoutParams.bottomMargin = 1

        val view = Button(this)
        view.gravity = Gravity.CENTER
        // /view.layout = getDrawable(android.R.layout.simple_list_item_1)
        view.background = getDrawable(R.color.colorListItemBackground)
        view.setPadding(16, 16, 16, 16)
        view.layoutParams = layoutParams
        view.text = item.content
        view.textSize = 12f
        return view
    }


}