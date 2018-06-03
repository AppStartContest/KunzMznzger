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

    var users: MutableList<User> = ArrayList()
    var events : MutableList<Event> = ArrayList()

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

    fun addUser(user: User){
        this.users.add(user)
    }

    override fun toJson(): JSONObject {
        val json = JSONObject()
        json["name"] = this.name

        return json
    }
}