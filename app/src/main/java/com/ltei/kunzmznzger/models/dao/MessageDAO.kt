package com.ltei.kunzmznzger.models.dao

import com.ltei.kunzmznzger.libs.models.ModelManager
import com.ltei.kunzmznzger.models.Message

class MessageDAO : ModelManager<Message>() {
    override fun getNamespace(): String {
        return "messages"
    }

    override fun getModelInstanceClass(): Class<Message> {
        return Message::class.java
    }
}