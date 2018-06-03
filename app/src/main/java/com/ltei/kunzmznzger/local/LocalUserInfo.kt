package com.ltei.kunzmznzger.local

import android.content.Context
import com.ltei.kunzmznzger.R
import com.ltei.kunzmznzger.models.Room
import com.ltei.kunzmznzger.models.User
import com.ltei.kunzmznzger.models.dao.UserDAO

class LocalUserInfo {

    companion object {

        private var globalInstance: LocalUserInfo = LocalUserInfo()

        fun getInstance(): LocalUserInfo {
            return globalInstance
        }

    }


    private var user: User? = null
    private val groups: ArrayList<Room>  = ArrayList()


    fun isCreated(context: Context): Boolean {
        val key =  context.getString(R.string.preference_item_user_id)
        return context.getSharedPreferences(context.getString(R.string.preference_file_id), Context.MODE_PRIVATE)
                .getInt(key, Int.MAX_VALUE) != Int.MAX_VALUE
    }

    fun create(context: Context, username: String, name: String, password: String) {
        UserDAO().register(username, password).thenCompose { UserDAO().auth(username, password) }
                .thenAccept {
                    this.user = it
                    val preferences = context.getSharedPreferences(context.getString(R.string.preference_file_id), Context.MODE_PRIVATE)
                    preferences.edit().putInt(context.getString(R.string.preference_item_user_id), user!!.id).apply()
                }
    }
    fun create(context: Context, username: String, name: String, password: String, runnable: Runnable) {
        UserDAO().register(username, password).thenCompose { UserDAO().auth(username, password) }
                .thenAccept {
                    this.user = it
                    val preferences = context.getSharedPreferences(context.getString(R.string.preference_file_id), Context.MODE_PRIVATE)
                    preferences.edit().putInt(context.getString(R.string.preference_item_user_id), user!!.id).apply()
                }.thenRun(runnable)
    }
    fun load() {
        //TODO
    }
    fun load(runnable: Runnable) {
        //TODO
    }

    /*fun getHistory(): Array<LocalExpenseInfo> {
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

    fun getUser(): User {
        return user!!
    }
    fun getGroups(): ArrayList<Room> {
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