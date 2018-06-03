package com.ltei.kunzmznzger.models

import com.ltei.kunzmznzger.libs.Helpers
import com.ltei.kunzmznzger.libs.models.Model
import com.ltei.kunzmznzger.libs.models.ModelManager
import com.ltei.kunzmznzger.libs.time.Date
import com.ltei.kunzmznzger.libs.time.Time
import com.ltei.kunzmznzger.models.dao.EventDAO
import org.joda.time.DateTime
import org.json.simple.JSONObject

class Event : Model<Event>() {
    //var id = Int
    var room : Room? = null

    var created_at : String? = null
    var updated_at : String? = null
    var deleted_at : String? = null

    var name : String? = null
    var description : String? = null
    var date : Date? = null
    var time : Time? = null

    var messages : MutableList<Message> = arrayListOf<Message>()

    fun add_message(message: Message){
        messages.add(message)
    }

    override fun recopy(model: Event) {
        var copy = Event()
        copy.room = model.room
        copy.created_at = model.created_at
        copy.updated_at = model.updated_at
        copy.deleted_at = model.deleted_at
        copy.messages = model.messages
        copy.name = model.name
        copy.description = model.description
        copy.date = model.date
        copy.time = model.time
    }

    override fun getManagerInstance(): ModelManager<Event> {
        return EventDAO()
    }

    override fun toString(): String {
        return "Event(id=${getId()} name=$name, ,description=$description, ,date=$date , time=$time createdAt=$created_at, updatedAt=$updated_at, room=$room, message=$messages"

    }

    override fun toJson(): JSONObject {
        val json = super.toJson()
        json["name"] = this.name
        json["room_id"] = this.room?.id
        json["description"] = this.description
        json["date"] = this.date.toString()
        json["time"] = this.time.toString()
        //json["message"] = this.message_list

        return json
    }

}