package com.ltei.kunzmznzger.models

import com.ltei.kunzmznzger.libs.models.Model
import com.ltei.kunzmznzger.libs.models.ModelManager
import com.ltei.kunzmznzger.models.dao.ExpenseDAO
import org.joda.time.DateTime
import org.json.simple.JSONObject
import java.io.Serializable

class Expense : Model<Expense>(), Serializable {

    var name: String? = null
    var value: Double? = null
    var description: String? = null

    var createdAt: DateTime? = null
    var updatedAt: DateTime? = null
    var deletedAt: DateTime? = null

    var messages: ArrayList<Message> = ArrayList()
    var users: ArrayList<User> = ArrayList()
    var user: User? = null
    var room: Room? = null

    fun addMessage(message: Message) {
        messages.add(message)
    }

    fun addUser(user: User) {
        users.add(user)
    }

    override fun recopy(model: Expense) {
        var copy = Expense()
        copy.user = model.user
        copy.room = model.room
        copy.value = model.value
        copy.createdAt = model.createdAt
        copy.updatedAt = model.updatedAt
        copy.deletedAt = model.deletedAt
        copy.messages = model.messages
        copy.description = model.description
    }

    override fun getManagerInstance(): ModelManager<Expense> {
        return ExpenseDAO()
    }

    override fun toString(): String {
        return "Expense(id=${getId()} value=$value, createdAt=$createdAt, updatedAt=$updatedAt, user=$user, room=$room, message=$messages"

    }

    override fun toJson(): JSONObject {
        val json = super.toJson()
        this.putFkIfRelationDefined(json, "room_id", this.room)
        this.putFkIfRelationDefined(json, "user_id", this.user)
        json["value"] = this.value
        json["description"] = this.description

        return json
    }

    override fun copyRelation(relation: String, expense: Expense): Expense {
        when (relation) {
            "messages" -> this.messages = expense.messages
            "users" -> this.users = expense.users
            "user" -> this.user = expense.user
            "room" -> this.room = expense.room
        }
        return this
    }
}