package com.ltei.kunzmznzger.models

import com.ltei.kunzmznzger.libs.models.Model
import com.ltei.kunzmznzger.libs.models.ModelManager
import com.ltei.kunzmznzger.models.dao.ExpenseDAO
import org.joda.time.DateTime
import org.json.simple.JSONObject

class Expense : Model<Expense>(){

    var name : String? = null
    var value : Float? = null
    var description : String? = null

    var createdAt : DateTime? = null
    var updatedAt : DateTime? = null
    var deletedAt : DateTime? = null

    var messages : MutableList<Message> = arrayListOf()
    var user : User? = null
    var room : Room? = null

    fun addMessage(message: Message){
        messages.add(message)
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

        return json
    }
}