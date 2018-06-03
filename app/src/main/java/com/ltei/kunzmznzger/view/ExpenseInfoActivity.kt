package com.ltei.kunzmznzger.view

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.Button
import android.widget.LinearLayout
import com.ltei.kunzmznzger.R
import com.ltei.kunzmznzger.models.Expense
import com.ltei.kunzmznzger.models.Message
import com.ltei.kunzmznzger.models.Room
import com.ltei.kunzmznzger.models.User
import kotlinx.android.synthetic.main.activity_expense_info.*
import kotlinx.android.synthetic.main.activity_group.*

class ExpenseInfoActivity : AppCompatActivity() {

    companion object {
        const val EXTRAS_EXPENSE = "EXPENSE_INFO_ACTIVITY_EXTRAS_EXPENSE"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_expense_info)

        val expense = intent.getSerializableExtra(EXTRAS_EXPENSE) as Expense
        text_title.text = expense.description
        text_amount.text = "spent ${expense.value}$ for"
        //TODO listlinearlayout_receivers.init(expense.getReceivers(), { item, _ -> createReceiverItemView(item as Expense) })
        listlinearlayout_messages.init(ArrayList(expense.messages), { item, _ -> createMessageItemView(item as Message) })
        button_add_message.setOnClickListener { onButtonAddMessageClickListener(expense) }
    }

    fun onButtonAddMessageClickListener(expense: Expense) {
        val dialog = DialogEnterText(this, "Write a message")
        dialog.setOnDismissListener({
            //TODO expense.postMessage(dialog.edittext.text.toString())
        })
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