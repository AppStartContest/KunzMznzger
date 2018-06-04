package com.ltei.kunzmznzger.models

import com.ltei.kunzmznzger.libs.models.Model
import com.ltei.kunzmznzger.libs.models.ModelManager
import com.ltei.kunzmznzger.libs.models.exceptions.ModelException
import com.ltei.kunzmznzger.models.dao.UserDAO
import org.json.simple.JSONObject
import java.util.concurrent.CompletableFuture

class User : Model<User>() {

    var name: String? = null
    var username: String? = null

    var rooms: ArrayList<Room> = ArrayList()
    var messages : ArrayList<Message> = ArrayList()
    var expenses : ArrayList<Expense> = ArrayList()

    override fun recopy(model: User) {
        val copy = User()
        copy.id = model.id
        copy.name = model.name
        copy.username = model.username
        copy.rooms = model.rooms
        copy.messages = model.messages
        copy.expenses = model.expenses
    }

    override fun getManagerInstance(): ModelManager<User> {
        return UserDAO()
    }

    override fun toString(): String {
        return "User(id=${getId()} name=$name, username=$username, rooms=$rooms, messages=$messages, expenses=$expenses)"
    }

    fun addRoom(room: Room){
        this.rooms.add(room)
    }

    fun addExpense(expense: Expense){
        this.expenses.add(expense)
    }

    fun addMessage(message: Message){
        this.messages.add(message)
    }

    override fun toJson(): JSONObject {
        val json = JSONObject()
        json["name"] = this.name
        json["username"] = this.username

        return json
    }

    /**
     * Update the model or create it if it doesn't exist
     * @return the updated/created model
     * @throws ModelException If you're trying to create a user. To do that, see UserDao#createAccount()
     */
    override fun save(): CompletableFuture<User> {
        if(this.id < 1){
            throw ModelException("You can't create a user this way. See UserDao#createAccount()")
        }

        return super.save()
    }
}