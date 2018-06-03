package com.ltei.kunzmznzger

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.widget.ArrayAdapter
import com.ltei.kunzmznzger.R.id.main_button_dept
import com.ltei.kunzmznzger.R.id.main_button_earns
import com.ltei.kunzmznzger.models.Expense
import com.ltei.kunzmznzger.models.Message
import com.ltei.kunzmznzger.models.Room
import com.ltei.kunzmznzger.models.User
import com.ltei.kunzmznzger.models.dao.RoomDAO
import com.ltei.kunzmznzger.models.dao.UserDAO
import com.ltei.kunzmznzger.view.DialogEnterText
import com.ltei.kunzmznzger.view.HistoryActivity
import com.ltei.kunzmznzger.view.HistorySort
import kotlinx.android.synthetic.main.activity_main.*
import org.joda.time.DateTime
import org.json.simple.JSONObject
import java.util.concurrent.CompletableFuture


class MainActivity : AppCompatActivity() {


    companion object {
        const val RC_CREATE_GROUP = 1
    }


    fun getHistorySort(): ArrayList<HistorySort> {
        val result = ArrayList<HistorySort>()
        result.add(HistorySort.biggestAmount())
        result.add(HistorySort.lowestAmount())
        result.add(HistorySort.mostRecentFirst())
        result.add(HistorySort.mostRecentLast())
        return result
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        main_button_earns.setOnClickListener({
            val intent = Intent(this, HistoryActivity::class.java)
            val expenses = ArrayList<Expense>()
            expenses.add(Expense())
            expenses.add(Expense())
            expenses.add(Expense())
            intent.putExtra(HistoryActivity.EXTRAS_LIST, expenses)
            intent.putExtra(HistoryActivity.EXTRAS_SORT, getHistorySort())
            startActivity(intent)
        })

        main_button_dept.setOnClickListener({
            val intent = Intent(this, HistoryActivity::class.java)
            intent.putExtra(HistoryActivity.EXTRAS_LIST, ArrayList<Expense>())
            intent.putExtra(HistoryActivity.EXTRAS_SORT, getHistorySort())
            startActivity(intent)
        })

        main_listview_groups.adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1,
                arrayOf("a", "b", "a", "b", "a", "b", "a", "b", "a", "b", "a", "b", "a", "b", "a", "b", "a", "b", "a", "b", "a", "b"))

        main_button_create_group.setOnClickListener({
            val dialog = DialogEnterText(this, "Enter a name for the new room")
            dialog.setOnCancelListener({
                //TODO Create room
                //TODO Goto room
            })
            dialog.show()
        })

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        when (requestCode) {
            RC_CREATE_GROUP -> {
                //TODO Refresh groups
            }
        }
    }

    override fun onResume() {
        super.onResume()
        //TODO Refresh groups
    }






















    fun robinTest() {
        //        // Example of GET request
//        UserDAO().all(UrlParametersMap().orderBy("-id").with("rooms")) // Get all users, order by 'id' DESC, and their rooms
//                .thenAccept({ users -> debug(users) }) // Getting the response, printing it
//                .thenCompose { UserDAO().find(4) } // Then execute a new GET request: Find the user with ID 4
//                .thenAccept(::debug) // then call our debug() function
//


//        // Example of attachment of two models
//        val newUser = User() // Create the new user and setting an arbitrary name and phone
//        newUser.name = "UserBala"
//        newUser.phone = DateTime.now().toString("MMddHHmmss")
//        newUser.save() // Create the user
//                .thenCompose({ user: User ->
//                    // Find the last room
//                    RoomDAO().findLast().thenCompose { room: Room ->
//                        // Attach this room to the user we just created
//                        user.attach(room)
//                    }
//                })
//                .thenAccept(::debug)


        // Example of detachment of two models
//        UserDAO().findLast()
//                .thenCompose { user: User ->
//                    RoomDAO().findLast().thenCompose { room: Room ->
//                        user.detach(room)
//                    }
//                }
//                .thenAccept(::debug)
//
//        // Example of sync of two models
//        UserDAO().findLast()
//                .thenCompose { user: User ->
//                    RoomDAO().findLast().thenCompose { room: Room ->
//                        user.sync(room) // Sync this room to this user. It will detach other rooms already attached to this user
////                        user.sync(room, true) // Sync this room to this user WITHOUT detaching other rooms already attached to this user
////                        user.sync(room, JSONObject()) // Sync this room to this user with additional data (empty here)
////                        user.sync(room, JSONObject(), true) // Sync this room to this user with additional data (empty here)
//                        // and without other rooms already attached to this user
//                    }
//                }
//                .thenAccept(::debug)

//        UserDAO().register("Register${DateTime.now().toString("MMddHHmmss")}", "testpw")
//                .thenCompose {
//                    debug(it)
//                        UserDAO().auth(it!!.username!!, "testpw")
//                }
//                .thenAccept(::debug)

        val username = "Register" + DateTime.now().toString("MMddHHmmss") // Unique username
        val pw = "testpw"

        UserDAO().register(username, pw) // Registration example
                .thenCompose {
                    debug(it)
                    UserDAO().auth(username, pw) // Authentication example
                }.thenCompose {
                    debug(it)
                    // Find last room to attach it to the user
                    RoomDAO().findLast().thenCompose { room: Room? ->
                        debug(room)
                        it?.attach(room)
                    }
                }
                .thenAccept(::debug)
    }

    fun debug(arg: Any?) {
        Log.d("[CONSOLE]", "$arg")
    }

    fun update(user: User): CompletableFuture<User> {
        debug(user)
        user.name = "A brand new name" // Change the name
        return user.save() // return the CompletableFuture returned by save().
        // Since it has a valid ID, it will update it instead of creating a new one
    }

    fun update(message: Message): CompletableFuture<Message> {
        debug(message)
        return message.save() // return the CompletableFuture returned by save().
        // Since it has a valid ID, it will update it instead of creating a new one
    }

    fun update(room: Room): CompletableFuture<Room> {
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
