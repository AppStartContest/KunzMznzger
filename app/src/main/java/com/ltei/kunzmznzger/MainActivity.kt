package com.ltei.kunzmznzger

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.ltei.kunzmznzger.libs.api.UrlParametersMap
import com.ltei.kunzmznzger.models.User
import com.ltei.kunzmznzger.models.dao.UserDAO
import org.json.simple.JSONObject
import org.json.simple.parser.JSONParser

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        UserDAO().all(UrlParametersMap().orderBy("-id").with("rooms"))
                .thenAccept({ users -> println(users) })
                .thenCompose { UserDAO().find(4) }
                .thenAccept({ user -> println(user) })

        // Not working yet
//        val newUser = User()
//        newUser.name = "New user"
//        newUser.phone = "123456"
//        newUser.save().thenAccept({user -> println("[CREATED] $user")})
    }
}
