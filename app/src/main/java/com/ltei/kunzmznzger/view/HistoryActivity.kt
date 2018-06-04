package com.ltei.kunzmznzger.view

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.MenuItem
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.PopupMenu
import com.ltei.kunzmznzger.R
import com.ltei.kunzmznzger.models.Expense
import kotlinx.android.synthetic.main.activity_history.*


class HistoryActivity : AppCompatActivity() {


    companion object {
        const val EXTRAS_LIST = "GROUP_ACTIVITY_EXTRAS_LIST"
        private var list: ArrayList<Expense>? = null
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_history)

        list = intent.getSerializableExtra(EXTRAS_LIST) as ArrayList<Expense>
        history_button_sort.setOnClickListener({ onSortButtonClickListener() })
        history_list_view.onItemClickListener = AdapterView.OnItemClickListener({ _, _, position, _ -> onHistoryItemClickListener(position) })
    }

    override fun onResume() {
        super.onResume()
        onListChanged()
    }


    private fun onSortButtonClickListener() {
        val dropDown = PopupMenu(applicationContext, history_button_sort)
        dropDown.menuInflater.inflate(R.menu.menu_history_sorter_items, dropDown.menu)
        dropDown.setOnMenuItemClickListener({
            onSortItemClickListener(it)
            true
        })
        dropDown.show()
    }
    private fun onSortItemClickListener(it: MenuItem) {
        when (it.itemId) {
            R.id.menu_item_sorter_biggestfirst -> HistorySort.biggestAmount().sort(list!!)
            R.id.menu_item_sorter_smallestfirst -> HistorySort.lowestAmount().sort(list!!)
            R.id.menu_item_sorter_recentfirst -> HistorySort.mostRecentFirst().sort(list!!)
            R.id.menu_item_sorter_recentlast -> HistorySort.mostRecentLast().sort(list!!)
            else -> throw IllegalStateException()
        }
        history_list_view.adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, list)
    }
    private fun onHistoryItemClickListener(position: Int) {
        val intent = Intent(this, ExpenseInfoActivity::class.java)
        intent.putExtra(ExpenseInfoActivity.EXTRAS_EXPENSE, list!![position])
        startActivity(intent)
    }

    private fun onListChanged() {
        history_list_view.adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, list)
    }



    //Toast.makeText(this, "Clicked item : $position", Toast.LENGTH_SHORT).show()
    //const val EXTRAS_SORT = "GROUP_ACTIVITY_EXTRAS_SORT"
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
    /*override fun onBackPressed() {
        super.onBackPressed()
        hideSearchText()
    }

    private fun showSearchText() {
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

