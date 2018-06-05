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
        class BiggestAmount(): HistorySort() {
            override fun toString(): String { return "Biggest amount" }
            override fun sorted(expenses: ArrayList<Expense>): ArrayList<Expense> {
                val comparator = Comparator<Expense>({
                    it1, it2 ->
                    if (it1.value!! == it2.value!!) {
                        0
                    } else if (it1.value!! < it2.value!!) {
                        -1
                    } else {
                        1
                    }
                })
                return ArrayList(expenses.sortedWith(comparator))
            }
        }
        class LowestAmount(): HistorySort() {
            override fun toString(): String { return "Lowest amount" }
            override fun sorted(expenses: ArrayList<Expense>): ArrayList<Expense> {
                val comparator = Comparator<Expense>({
                    it1, it2 ->
                    if (it1.value!! == it2.value!!) {
                        0
                    } else if (it1.value!! < it2.value!!) {
                        -1
                    } else {
                        1
                    }
                })
                return ArrayList(expenses.sortedWith(comparator))
            }
        }
        class MostRecentFirst(): HistorySort() {
            override fun toString(): String { return "Most recent first" }
            override fun sorted(expenses: ArrayList<Expense>): ArrayList<Expense> {
                val comparator = Comparator<Expense>({
                    it1, it2 ->
                    if (it1.createdAt!!.isEqual(it2.createdAt!!)) {
                        0
                    } else if (it1.createdAt!!.isAfter(it2.createdAt!!)) {
                        -1
                    } else {
                        1
                    }
                })
                return ArrayList(expenses.sortedWith(comparator))
            }
        }
        class MostRecentLast(): HistorySort() {
            override fun toString(): String { return "Most recent last" }
            override fun sorted(expenses: ArrayList<Expense>): ArrayList<Expense> {
                val comparator = Comparator<Expense>({
                    it1, it2 ->
                    if (it1.createdAt!!.isEqual(it2.createdAt!!)) {
                        0
                    } else if (it1.createdAt!!.isAfter(it2.createdAt!!)) {
                        1
                    } else {
                        -1
                    }
                })
                return ArrayList(expenses.sortedWith(comparator))
            }
        }

    }

    abstract fun sorted(expenses: ArrayList<Expense>): ArrayList<Expense>
}