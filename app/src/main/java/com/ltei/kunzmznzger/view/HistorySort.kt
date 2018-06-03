package com.ltei.kunzmznzger.view

import com.ltei.kunzmznzger.models.Expense
import java.io.Serializable

abstract class HistorySort: Serializable {

    companion object {

        fun biggestAmount(): HistorySort {
            return BiggestAmount()
        }
        fun lowestAmount(): HistorySort {
            return LowestAmount()
        }
        fun mostRecentFirst(): HistorySort {
            return MostRecentFirst()
        }
        fun mostRecentLast(): HistorySort {
            return MostRecentLast()
        }
        //TODO Implement everything
        class BiggestAmount(): HistorySort() {
            override fun toString(): String { return "Biggest amount" }
            override fun sort(expenses: ArrayList<Expense>) { throw IllegalStateException("Not implemented!") }
        }
        class LowestAmount(): HistorySort() {
            override fun toString(): String { return "Lowest amount" }
            override fun sort(expenses: ArrayList<Expense>) { throw IllegalStateException("Not implemented!") }
        }
        class MostRecentFirst(): HistorySort() {
            override fun toString(): String { return "Most recent first" }
            override fun sort(expenses: ArrayList<Expense>) { throw IllegalStateException("Not implemented!") }
        }
        class MostRecentLast(): HistorySort() {
            override fun toString(): String { return "Most recent last" }
            override fun sort(expenses: ArrayList<Expense>) { throw IllegalStateException("Not implemented!") }
        }

    }

    abstract fun sort(expenses: ArrayList<Expense>)
}