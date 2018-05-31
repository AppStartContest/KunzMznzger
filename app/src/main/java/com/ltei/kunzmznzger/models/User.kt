package com.ltei.kunzmznzger.models

import com.ltei.kunzmznzger.libs.models.Model
import com.ltei.kunzmznzger.libs.models.ModelManager
import com.ltei.kunzmznzger.models.dao.UserDAO

class User : Model<User>() {

    var name: String? = null
    var phone: String? = null

    var rooms: MutableList<Room> = mutableListOf()

    override fun recopy(model: User) {
        val copy = User()
        copy.id = model.id
        copy.name = model.name
        copy.phone = model.phone
        copy.rooms = model.rooms
    }

    override fun getManagerInstance(): ModelManager<User> {
        return UserDAO()
    }

    override fun toString(): String {
        return "User(id=${getId()} name=$name, phone=$phone, rooms=$rooms)"
    }

    fun addRoom(room: Room){
        this.rooms.add(room)
    }
}