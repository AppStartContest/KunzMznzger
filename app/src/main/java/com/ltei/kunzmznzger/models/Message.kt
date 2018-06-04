package com.ltei.kunzmznzger.models

import com.ltei.kunzmznzger.libs.models.Model
import com.ltei.kunzmznzger.libs.models.ModelManager
import com.ltei.kunzmznzger.models.dao.MessageDAO
import org.joda.time.DateTime
import org.json.simple.JSONObject
import java.io.Serializable

class Message : Model<Message>(), Serializable {
    var content: String? = null

    var createdAt: DateTime? = null
    var updatedAt: DateTime? = null
    var deletedAt: DateTime? = null

    var user: User? = null

    var room: Room? = null
    var expense: Expense? = null
    var event: Event? = null

    override fun recopy(model: Message) {
        var copy = Message()
        copy.user = model.user
        copy.room = model.room
        copy.expense = model.expense
        copy.createdAt = model.createdAt
        copy.updatedAt = model.updatedAt
        copy.deletedAt = model.deletedAt
        copy.content = model.content
    }

    override fun getManagerInstance(): ModelManager<Message> {
        return MessageDAO()
    }

    override fun toString(): String {
        return "Message(id=${getId()} content=$content, createdAt=$createdAt, updatedAt=$updatedAt, user=$user, room=$room, expense=$expense"

    }

    override fun toJson(): JSONObject {
        val json = super.toJson()
        json["content"] = this.content
        this.putFkIfRelationDefined(json, "user_id", this.user)
        this.putFkIfRelationDefined(json, "room_id", this.room)
        this.putFkIfRelationDefined(json, "expense_id", this.expense)
        this.putFkIfRelationDefined(json, "event_id", this.event)

        return json
    }

    override fun copyRelation(relation: String, message: Message): Message {
        when(relation){
            "room" -> this.room = message.room
            "expense" -> this.expense = message.expense
            "event" -> this.event = message.event
        }
        return this
    }

}