package com.ltei.kunzmznzger.models.dao

import com.ltei.kunzmznzger.libs.models.ModelManager
import com.ltei.kunzmznzger.models.Expense

class ExpenseDAO : ModelManager<Expense>() {
    override fun getNamespace(): String {
        return "expenses"
    }

    override fun getModelInstanceClass(): Class<Expense> {
        return Expense::class.java
    }
}