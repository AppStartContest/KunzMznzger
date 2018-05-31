package com.ltei.kunzmznzger.models.dao

import com.ltei.kunzmznzger.libs.models.ModelManager
import com.ltei.kunzmznzger.models.User

class UserDAO : ModelManager<User>() {
    override fun getNamespace(): String {
        return "users"
    }

    override fun getModelInstanceClass(): Class<User> {
        return User::class.java
    }
}