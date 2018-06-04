package com.ltei.kunzmznzger.models.contracts

import com.ltei.kunzmznzger.libs.Helpers
import com.ltei.kunzmznzger.libs.models.Model

open interface Messageable{
    fun messageForeignKey(): String {
        val className = this::class.java.simpleName
        val result = Helpers.snakeCase("${className}Id")
        return result
    }

    fun getId(): Int
}