package com.ltei.kunzmznzger.models.dao

import com.ltei.kunzmznzger.libs.models.ModelManager
import com.ltei.kunzmznzger.models.Room

class RoomDAO : ModelManager<Room>() {
    override fun getNamespace(): String {
        return "rooms"
    }

    override fun getModelInstanceClass(): Class<Room> {
        return Room::class.java
    }
}