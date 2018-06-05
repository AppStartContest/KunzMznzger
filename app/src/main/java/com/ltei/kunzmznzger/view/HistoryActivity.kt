package com.ltei.kunzmznzger.view

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.Gravity
import android.view.MenuItem
import android.view.ViewGroup
import android.widget.*
import com.google.android.gms.ads.AdRequest
import com.ltei.kunzmznzger.R
import com.ltei.kunzmznzger.local.LocalUserInfo
import com.ltei.kunzmznzger.models.Expense
import kotlinx.android.synthetic.main.activity_history.*


class HistoryActivity : AppCompatActivity() {


    companion object {
        const val EXTRAS_ROOM_IDX = "GROUP_ACTIVITY_EXTRAS_ROOM_IDX"
    }


    private var roomIdx: Int = -1
    private fun getList(): ArrayList<Expense> {
        return LocalUserInfo.getInstance().getRooms()[roomIdx].expenses
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_history)

        // Initialize activity ad
        val adRequest = AdRequest.Builder().build()
        ad_banner.loadAd(adRequest)


        roomIdx = intent.getIntExtra(EXTRAS_ROOM_IDX, -1)

        history_button_sort.setOnClickListener({ onSortButtonClickListener() })

        listlinearlayout.init(getList(), {
            item, idx ->
            val listItemLayoutParams = LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
            listItemLayoutParams.bottomMargin = 1
            val view = TextView(this)
            view.gravity = Gravity.CENTER
            view.background = this.getDrawable(R.color.colorListItemBackground)
            view.setPadding(16, 16, 16, 16)
            view.layoutParams = listItemLayoutParams
            view.text = (item as Expense).description
            view.textSize = 12f
            view.setOnClickListener {
                onHistoryItemClickListener(idx)
            }
            view
        })
    }

    override fun onResume() {
        super.onResume()
        listlinearlayout.notifyArrayChange()
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
            R.id.menu_item_sorter_biggestfirst -> listlinearlayout.setArray(HistorySort.biggestAmount().sorted(getList()))
            R.id.menu_item_sorter_smallestfirst -> listlinearlayout.setArray(HistorySort.lowestAmount().sorted(getList()))
            R.id.menu_item_sorter_recentfirst -> listlinearlayout.setArray(HistorySort.mostRecentFirst().sorted(getList()))
            R.id.menu_item_sorter_recentlast -> listlinearlayout.setArray(HistorySort.mostRecentLast().sorted(getList()))
            else -> throw IllegalStateException()
        }
        listlinearlayout.notifyArrayChange()
    }
    private fun onHistoryItemClickListener(position: Int) {
        val intent = Intent(this, ExpenseInfoActivity::class.java)
        intent.putExtra(ExpenseInfoActivity.EXTRAS_ROOM_IDX, roomIdx)
        intent.putExtra(ExpenseInfoActivity.EXTRAS_EXPENSE_IDX_IN_ROOM, position)
        startActivity(intent)
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

