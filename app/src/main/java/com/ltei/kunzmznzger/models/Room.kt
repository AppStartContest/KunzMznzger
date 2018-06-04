package com.ltei.kunzmznzger.models

import com.ltei.kunzmznzger.libs.models.Model
import com.ltei.kunzmznzger.libs.models.ModelManager
import com.ltei.kunzmznzger.models.dao.RoomDAO
import org.joda.time.DateTime
import org.json.simple.JSONObject
import java.io.Serializable

class Room : Model<Room>(), Serializable {

    var name: String? = null
    var createdAt: DateTime? = null
    var updatedAt: DateTime? = null

    var users: ArrayList<User> = ArrayList()
    var events: ArrayList<Event> = ArrayList()
    var messages: ArrayList<Message> = ArrayList()
    var expenses: ArrayList<Expense> = ArrayList()

    override fun recopy(model: Room) {
        var copy = Room()
        copy.name = model.name
        copy.createdAt = model.createdAt
        copy.updatedAt = model.updatedAt
        copy.users = model.users
        copy.events = model.events
    }

    override fun getManagerInstance(): ModelManager<Room> {
        return RoomDAO()
    }

    override fun toString(): String {
        return "Room(id=${getId()} name=$name, createdAt=$createdAt, updatedAt=$updatedAt, users=$users, events=$events)"
    }

    fun addUser(user: User) {
        this.users.add(user)
    }

    fun addEvent(event: Event) {
        this.events.add(event)
    }

    fun addMessage(message: Message) {
        this.messages.add(message)
    }

    fun addExpense(expense: Expense) {
        this.expenses.add(expense)
    }

    override fun toJson(): JSONObject {
        val json = JSONObject()
        json["name"] = this.name

        return json
    }

    override fun copyRelation(relation: String, room: Room): Room {
        when (relation) {
            "users" -> this.users = room.users
            "events" -> this.events = room.events
        }
        return this
    }

    // --- Helpers

    /**
     * Note: The room should have its expenses already loaded
     */
    fun calcUserExpenseStatus(user: User): Double {
        // Expenses in this room of this user
        val filtered = this.expenses.filter { it.user!!.id == user.id }

        val spentByLoggedUser = filtered.stream().mapToDouble { it.value!! }.sum()
        val roomAvg = this.calcExpenseAverage()

        return spentByLoggedUser - roomAvg
    }

    /**
     * Note: The room should have its expenses already loaded
     */
    fun calcExpenseAverage(): Double {
        val total = this.expenses.stream().mapToDouble { it.value!! }.sum()
        return total / this.expenses.size
    }
}