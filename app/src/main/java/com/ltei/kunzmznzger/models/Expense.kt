package com.ltei.kunzmznzger.models

import com.ltei.kunzmznzger.libs.models.Model
import com.ltei.kunzmznzger.libs.models.ModelManager
import com.ltei.kunzmznzger.models.dao.ExpenseDAO
import org.joda.time.DateTime
import org.json.simple.JSONObject
import java.io.Serializable

class Expense : Model<Expense>(), Serializable {
    //var id = Int
    var user : User? = null
    var room : Room? = null

    var name : String? = null
    var created_at : DateTime? = null
    var updated_at : DateTime? = null
    var deleted_at : DateTime? = null
    var description : String? = null

    var value : Float? = null

    var message_list : MutableList<Message> = arrayListOf<Message>()

    fun add_message(message: Message){
        message_list.add(message)
    }

    override fun recopy(model: Expense) {
        var copy = Expense()
        copy.user = model.user
        copy.room = model.room
        copy.value = model.value
        copy.created_at = model.created_at
        copy.updated_at = model.updated_at
        copy.deleted_at = model.deleted_at
        copy.message_list = model.message_list
        copy.description = model.description
    }

    override fun getManagerInstance(): ModelManager<Expense> {
        return ExpenseDAO()
    }

    override fun toString(): String {
        return "Expense(id=${getId()} value=$value, createdAt=$created_at, updatedAt=$updated_at, user=$user, room=$room, message=$message_list"

    }

    override fun toJson(): JSONObject {
        val json = super.toJson()
        json["user_id"] = this.user!!.id
        json["room_id"] = this.room!!.id
        json["value"] = this.value
        //json["message"] = this.message_list

        return json
    }

}