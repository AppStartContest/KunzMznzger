package com.ltei.kunzmznzger.view

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.ltei.kunzmznzger.R
import com.ltei.kunzmznzger.models.Expense
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



        /*expense_info_listview_messages.adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, expense.getMessages())

        expense_info_button_add_message.setOnClickListener({
            //TODO Add message to expense
            //TODO Reload expense
            expense_info_listview_messages.adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, expense.getMessages())
        })*/

    }

}