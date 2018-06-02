package com.ltei.kunzmznzger

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import com.ltei.kunzmznzger.libs.api.UrlParametersMap
import com.ltei.kunzmznzger.models.Message
import com.ltei.kunzmznzger.models.Room
import com.ltei.kunzmznzger.models.User
import com.ltei.kunzmznzger.models.dao.UserDAO
import org.joda.time.DateTime
import java.util.*
import java.util.concurrent.CompletableFuture


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Example of GET request
        UserDAO().all(UrlParametersMap().orderBy("-id").with("rooms")) // Get all users, order by 'id' DESC, and their rooms
                .thenAccept({ users -> debug(users) }) // Getting the response, printing it
                .thenCompose { UserDAO().find(4) } // Then execute a new GET request: Find the user with ID 4
                .thenAccept(::debug) // then call our debug() function

        // Example of creation and updating requests
        val newUser = User() // Create the new user and setting an arbitrary name and phone
        newUser.name = "UserBala"
        newUser.phone = DateTime.now().toString("MMddHHmmss")
        newUser.save() // Saving it. Since it has no ID, it will create it
                .thenCompose(::update) // Then call the our update() method on the result
                .thenAccept { user -> debug(user) }

        val newRoom = Room()
        newRoom.name = "RoomBala"
        newRoom.addUser(newUser)
        newRoom.save().thenCompose(::update).thenAccept{room -> debug(room)}

        val newMessage = Message()
        newMessage.user = newUser
        newMessage.content = "Coucou"
        newMessage.room = newRoom
        newMessage.save().thenCompose(::update).thenAccept{message -> debug(message)}

        // Example of deletion
        UserDAO().getLast() // Get the last inserted user
                .thenCompose(::delete) // The call our delete() method
                .thenAccept({ bool -> debug("Deleted ? $bool")}) // Then accept the response and printing it
    }

    fun debug(arg: Any) {
        Log.d("[CONSOLE]", "$arg")
    }

    fun update(user: User): CompletableFuture<User>{
        debug(user)
        user.name = "A brand new name" // Change the name
        return user.save() // return the CompletableFuture returned by save().
                           // Since it has a valid ID, it will update it instead of creating a new one
    }

    fun update(message: Message): CompletableFuture<Message>{
        debug(message)
        return message.save() // return the CompletableFuture returned by save().
        // Since it has a valid ID, it will update it instead of creating a new one
    }

    fun update(room: Room): CompletableFuture<Room>{
        debug(room)
        //room.name = "A brand new name" // Change the name
        return room.save() // return the CompletableFuture returned by save().
        // Since it has a valid ID, it will update it instead of creating a new one
    }

    fun delete(user: User): CompletableFuture<User> {
        debug(user)
        return user.delete() // return the CompletableFuture returned by delete()
    }
}
