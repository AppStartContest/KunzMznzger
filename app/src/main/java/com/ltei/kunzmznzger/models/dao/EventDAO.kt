package com.ltei.kunzmznzger.models.dao

import com.ltei.kunzmznzger.libs.models.ModelManager
import com.ltei.kunzmznzger.models.Event

class EventDAO : ModelManager<Event>() {
    override fun getNamespace(): String {
        return "events"
    }

    override fun getModelInstanceClass(): Class<Event> {
        return Event::class.java
    }
}