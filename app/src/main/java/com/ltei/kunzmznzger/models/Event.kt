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


    var createdAt : String? = null
    var updatedAt : String? = null
    var deletedAt : String? = null

    var name : String? = null
    var description : String? = null
    var date : Date? = null
    var time : Time? = null

    var messages : MutableList<Message> = arrayListOf()
    var room : Room? = null

    fun addMessage(message: Message){
        messages.add(message)
    }

    override fun recopy(model: Event) {
        var copy = Event()
        copy.room = model.room
        copy.createdAt = model.createdAt
        copy.updatedAt = model.updatedAt
        copy.deletedAt = model.deletedAt
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
        return "Event(id=${getId()} name=$name, ,description=$description, ,date=$date , time=$time createdAt=$createdAt, updatedAt=$updatedAt, room=$room, message=$messages"

    }

    override fun toJson(): JSONObject {
        val json = super.toJson()
        json["name"] = this.name
        json["description"] = this.description
        json["date"] = this.date.toString()
        json["time"] = this.time.toString()
        this.putFkIfRelationDefined(json, "room_id", this.room)
        //json["message"] = this.message_list

        return json
    }

}