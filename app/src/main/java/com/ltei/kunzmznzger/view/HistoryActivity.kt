package com.ltei.kunzmznzger.view

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.PopupMenu
import android.widget.Toast
import com.ltei.kunzmznzger.R
import com.ltei.kunzmznzger.models.Expense
import kotlinx.android.synthetic.main.activity_history.*


class HistoryActivity : AppCompatActivity() {

    companion object {
        const val EXTRAS_LIST = "GROUP_ACTIVITY_EXTRAS_LIST"
        const val EXTRAS_SORT = "GROUP_ACTIVITY_EXTRAS_SORT"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_history)

        val list = intent.getSerializableExtra(EXTRAS_LIST) as ArrayList<Expense>
        val sort = intent.getSerializableExtra(EXTRAS_SORT) as ArrayList<HistorySort>

        history_list_view.adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, list)

        history_button_sort.setOnClickListener({
            val dropDown = PopupMenu(applicationContext, history_button_sort)
            dropDown.menuInflater.inflate(R.menu.menu_history_sorter_items, dropDown.menu)
            dropDown.setOnMenuItemClickListener({
                Toast.makeText(applicationContext, "Clicked "+it.title, Toast.LENGTH_SHORT).show()
                true
            })
            dropDown.show()
        })

        /*history_button_search.setOnClickListener({
            showSearchText()
        })

        history_edittext_search.setOnEditorActionListener({ v, actionId, event ->
            var result = false
            if (actionId == EditorInfo.IME_ACTION_SEARCH ||
                    actionId == EditorInfo.IME_ACTION_DONE ||
                    event != null &&
                    event.action == KeyEvent.ACTION_DOWN &&
                    event.keyCode == KeyEvent.KEYCODE_ENTER) {
                if (event == null || !event.isShiftPressed) {
                    hideSearchText()
                    result = true
                }
            }
            result
        })
        history_edittext_search.setOnFocusChangeListener({ v, hasFocus ->
            if (!hasFocus) {
                hideSearchText()
            }
        })*/

        history_list_view.onItemClickListener = AdapterView.OnItemClickListener({
            _, _, position, _ ->
            Toast.makeText(this, "Clicked item : $position", Toast.LENGTH_SHORT).show()
        })
    }


    /*override fun onBackPressed() {
        super.onBackPressed()
        hideSearchText()
    }*/



    /*private fun showSearchText() {
        history_edittext_search.visibility = View.VISIBLE
        history_edittext_search.requestFocus()
        (getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager).showSoftInput(history_edittext_search, InputMethodManager.SHOW_IMPLICIT)
    }
    private fun hideSearchText() {
        history_edittext_search.visibility = View.GONE
        (getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager).hideSoftInputFromWindow(history_edittext_search.windowToken, 0)
        Toast.makeText(applicationContext, "Hide search text", Toast.LENGTH_SHORT).show()
    }*/

}

