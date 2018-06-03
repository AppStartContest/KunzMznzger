package com.ltei.kunzmznzger.local

import android.content.Context
import com.ltei.kunzmznzger.R
import com.ltei.kunzmznzger.libs.time.Date
import com.ltei.kunzmznzger.models.Room
import com.ltei.kunzmznzger.models.dao.RoomDAO
import com.ltei.kunzmznzger.models.dao.UserDAO

class LocalUserInfo {
    
    companion object {
        
        private var globalInstance: LocalUserInfo? = null
        
        fun getInstance(): LocalUserInfo {
            if (globalInstance == null) {
                globalInstance = LocalUserInfo()
            }
            return globalInstance!!
        }
        
    }
    
    
    private var id: Int? = null
    private var username: String? = null
    private var name: String? = null
    private var password: String? = null
    private val groups: ArrayList<LocalGroupInfo>  = ArrayList()
    

    fun isCreated(context: Context): Boolean {
        val key =  context.getString(R.string.preference_item_user_pseudo)
        return context.getSharedPreferences(context.getString(R.string.preference_file_id), Context.MODE_PRIVATE)
                .getString(key, null) != null
    }
    fun create(username: String, name: String, password: String) {
        UserDAO().register(username, password).thenCompose { UserDAO().auth(username, password) }
                .thenAccept {
                    this.id = it!!.id
                    this.username = username
                    this.name = name
                    this.password = password
                }
    }
    fun create(username: String, name: String, password: String, runnable: Runnable) {
        UserDAO().register(username, password).thenCompose { UserDAO().auth(username, password) }
                .thenAccept {
                    this.id = it!!.id
                    this.username = username
                    this.name = name
                    this.password = password
                }.thenRun(runnable)
    }
    /*fun load() {
        //TODO
    }
    fun load(runnable: Runnable) {
        //TODO
    }
    fun getHistory(): Array<LocalExpenseInfo> {
        //TODO
    }
    fun getTotalDebts(): Int {
        //TODO
    }
    fun getTotalEarnings(): Int {
        //TODO
    }
    fun getTotalDebtsAfter(date: Date): Int {
        //TODO
    }
    fun getTotalEarningsAfter(date: Date): Int {
        //TODO
    }
    fun getTotalDebtsBefore(date: Date): Int {
        //TODO
    }
    fun getTotalEarningsBefore(date: Date): Int {
        //TODO
    }*/
    fun getGroups(): ArrayList<LocalGroupInfo> {
        return groups
    }


    /*fun isUserCreated(): Boolean {
        return getUserName() != null
    }
    fun getUserName(): String? {
        val key = getString(R.string.preference_item_user_pseudo)
        return this.getSharedPreferences(getString(R.string.preference_file_id), Context.MODE_PRIVATE)
                .getString(key, null)
    }
    fun getUserPassword(): String? {
        val key = getString(R.string.preference_item_user_password)
        return this.getSharedPreferences(getString(R.string.preference_file_id), Context.MODE_PRIVATE)
                .getString(key, null)
    }
    fun getUserId(): Int? {
        val key = getString(R.string.preference_item_user_id)
        val result = this.getSharedPreferences(getString(R.string.preference_file_id), Context.MODE_PRIVATE)
                .getInt(key, -1)
        return if (result == -1) { null } else { result }
    }

    fun createUser(name: String, password: String) {
        val newUser = User()
        newUser.name = name
        newUser.save().thenAccept {
            Toast.makeText(this@MainActivity, "User created!", Toast.LENGTH_SHORT).show()
            this.getSharedPreferences(getString(R.string.preference_file_id), Context.MODE_PRIVATE).edit()
                    .putString(getString(R.string.preference_item_user_pseudo), name)
                    .putString(getString(R.string.preference_item_user_password), password)
                    .putInt(getString(R.string.preference_item_user_id), it.id)
                    .apply()
        }
    }
    fun reloadUser(id: Int) {
        UserDAO().find(id).thenAccept { MAIN_USER = it }
    }*/
    
    
}