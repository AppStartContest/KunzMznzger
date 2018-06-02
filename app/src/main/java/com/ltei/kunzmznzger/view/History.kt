package com.ltei.kunzmznzger.view

import android.content.Context
import android.util.AttributeSet
import android.widget.AdapterView.OnItemClickListener
import android.widget.ArrayAdapter
import android.widget.ListView
import android.widget.Toast
import com.ltei.kunzmznzger.models.Expense

class History: ListView {

    constructor(context: Context): super(context)
    constructor(context: Context, attrs: AttributeSet): super(context, attrs)

    fun init(expenses: List<Expense>) {
        this.adapter = ArrayAdapter(this.context, android.R.layout.simple_list_item_1, expenses)
        this.onItemClickListener = OnItemClickListener(
                { _, _, position, _ ->
                    Toast.makeText(this.context, "History : clicked item : $position", Toast.LENGTH_SHORT).show()
                }
        )
    }

}