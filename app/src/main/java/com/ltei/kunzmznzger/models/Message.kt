package com.ltei.kunzmznzger.models

import com.ltei.kunzmznzger.libs.Helpers
import com.ltei.kunzmznzger.libs.models.Model
import com.ltei.kunzmznzger.libs.models.ModelManager
import com.ltei.kunzmznzger.models.dao.MessageDAO
import org.joda.time.DateTime
import org.json.simple.JSONObject

class Message() : Model<Message>() {
    //var id = Int
    var user : User? = null
    var room : Room? = null
    var expense : Expense? = null
    var content : String? = null

    var created_at : DateTime? = null
    var updated_at : DateTime? = null
    var deleted_at : DateTime? = null

    override fun recopy(model: Message) {
        var copy = Message()
        copy.user = model.user
        copy.room = model.room
        copy.expense = model.expense
        copy.created_at = model.created_at
        copy.updated_at = model.updated_at
        copy.deleted_at = model.deleted_at
        copy.content = model.content
    }

    override fun getManagerInstance(): ModelManager<Message> {
        return MessageDAO()
    }

    override fun toString(): String {
        return "Message(id=${getId()} content=$content, createdAt=$created_at, updatedAt=$updated_at, user=$user, room=$room, expense=$expense"

    }

    override fun toJson(): JSONObject {
        val json = super.toJson()
        json["user_id"] = this.user!!.id
        json["room_id"] = this.room!!.id
        json["expense_id"] = this.expense!!.id
        json["content"] = this.content

        return json
    }

}