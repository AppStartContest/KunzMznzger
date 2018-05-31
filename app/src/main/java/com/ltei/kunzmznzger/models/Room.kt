package com.ltei.kunzmznzger.models

import com.ltei.kunzmznzger.libs.models.Model
import com.ltei.kunzmznzger.libs.models.ModelManager
import com.ltei.kunzmznzger.models.dao.RoomDAO
import org.joda.time.DateTime

class Room : Model<Room>() {

    var name: String? = null
    var createdAt: DateTime? = null
    var updatedAt: DateTime? = null

    var users: ArrayList<User> = ArrayList()
    var aze: ArrayList<User> = ArrayList()

    override fun recopy(model: Room) {
        var copy = Room()
        copy.name = model.name
        copy.createdAt = model.createdAt
        copy.updatedAt = model.updatedAt
        copy.users = model.users
    }

    override fun getManagerInstance(): ModelManager<Room> {
        return RoomDAO()
    }

    override fun toString(): String {
        return "Room(id=${getId()} name=$name, createdAt=$createdAt, updatedAt=$updatedAt, users=$users)"
    }

    fun addUser(user: User){
        this.users.add(user)
    }
}