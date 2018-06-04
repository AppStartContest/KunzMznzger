package com.ltei.kunzmznzger.local

import android.content.Context
import com.ltei.kunzmznzger.R
import com.ltei.kunzmznzger.libs.api.UrlParametersMap
import com.ltei.kunzmznzger.libs.models.Model
import com.ltei.kunzmznzger.libs.models.exceptions.ModelException
import com.ltei.kunzmznzger.models.*
import com.ltei.kunzmznzger.models.dao.UserDAO
import java.util.concurrent.CompletableFuture

class LocalUserInfo {

    companion object {

        var globalInstance: LocalUserInfo = LocalUserInfo()

        fun getInstance(): LocalUserInfo {
            return globalInstance
        }

    }


    private var user: User? = null
    private val groups: ArrayList<Room> = ArrayList()


    fun isCreated(context: Context): Boolean {
        val key = context.getString(R.string.preference_file_id)
        return context.getSharedPreferences(context.getString(R.string.preference_file_id), Context.MODE_PRIVATE)
                .getInt(key, -1) != -1
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

    fun load(context: Context) {
        val key =  context.getString(R.string.preference_item_user_id)
        val id = context.getSharedPreferences(context.getString(R.string.preference_file_id), Context.MODE_PRIVATE)
                .getInt(key, Int.MAX_VALUE)

        UserDAO().find(id , UrlParametersMap().withAllRelations()).thenAccept { this.user = it }
    }

    fun load(context: Context , runnable: Runnable) {
        val key =  context.getString(R.string.preference_item_user_id)
        val id = context.getSharedPreferences(context.getString(R.string.preference_file_id), Context.MODE_PRIVATE)
                .getInt(key, Int.MAX_VALUE)

        UserDAO().find(id , UrlParametersMap().withAllRelations()).thenAccept { this.user = it }
                .thenRun(runnable)
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
        return user!!.rooms as ArrayList<Room>
    }

    fun getRooms(): ArrayList<Room> {
        return getGroups()
    }

    fun createRoom(name: String): CompletableFuture<User> {
        val room = Room()
        room.name = name
        return room.save().thenCompose { room: Room ->
            this.user!!.attach(room)
        }
    }

    fun createGroup(name: String): CompletableFuture<User> {
        return createRoom(name)
    }

    /**
     * @param message
     * @param room an existing room
     * @throws ModelException if the room is not valid (if it doesn't correspond to db entry)
     */
    fun sendMessageToRoom(message: Message, room: Room): CompletableFuture<Message> {
        throwIfInvalidModel(room)
        message.room = room
        return this.saveMessage(message)
    }

    /**
     * @param message
     * @param expense an existing expense
     * @throws ModelException if the expense is not valid (if it doesn't correspond to db entry)
     */
    fun sendMessageToExpense(message: Message, expense: Expense): CompletableFuture<Message> {
        throwIfInvalidModel(expense)
        message.expense = expense
        return this.saveMessage(message)
    }

    /**
     * @param message
     * @param event an existing event
     * @throws ModelException if the event is not valid (if it doesn't correspond to db entry)
     */
    fun sendMessageToEvent(message: Message, event: Event): CompletableFuture<Message> {
        throwIfInvalidModel(event)
        message.event = event
        return this.saveMessage(message)
    }

    private fun saveMessage(message: Message): CompletableFuture<Message> {
        message.user = this.user!!
        return message.save()
    }

    /**
     * @param event
     * @param room an existing room
     * @throws ModelException if the room is not valid (if it doesn't correspond to db entry)
     */
    fun createEvent(event: Event, room: Room): CompletableFuture<Event>? {
        throwIfInvalidModel(room)
        event.room = room
        return event.save()
    }

    private fun throwIfInvalidModel(model: Model<*>) {
        if (!model.isValid())
            throw ModelException("The model should exist in the database (have an ID > 0)")
    }

//    fun addExpenseToRoom(e)

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