package com.ltei.kunzmznzger.models

import com.ltei.kunzmznzger.libs.Dept
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

    var users: ArrayList<User> = ArrayList()
    var events: ArrayList<Event> = ArrayList()
    var messages: ArrayList<Message> = ArrayList()
    var expenses: ArrayList<Expense> = ArrayList()

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

    fun addUser(user: User) {
        this.users.add(user)
    }

    fun addEvent(event: Event) {
        this.events.add(event)
    }

    fun addMessage(message: Message) {
        this.messages.add(message)
    }

    fun addExpense(expense: Expense) {
        this.expenses.add(expense)
    }

    override fun toJson(): JSONObject {
        val json = JSONObject()
        json["name"] = this.name

        return json
    }

    override fun copyRelation(relation: String, room: Room): Room {
        when (relation) {
            "users" -> this.users = room.users
            "events" -> this.events = room.events
        }
        return this
    }

    // --- Helpers

    /**
     * Note: The room should have its expenses already loaded
     */
    fun calcUserExpenseStatus(user: User): Double {
        // Expenses in this room of this user
        val filtered = this.expenses.filter { it.user!!.id == user.id }

        val spentByLoggedUser = filtered.stream().mapToDouble { it.value!! }.sum()
        val roomAvg = this.calcExpenseAverage()

        return spentByLoggedUser - roomAvg
    }

    /**
     * Note: The room should have its expenses already loaded
     */
    fun calcExpenseAverage(): Double {
        val total = this.expenses.stream().mapToDouble { it.value!! }.sum()
        return total / this.users.size
    }

    // ----- Compute dept

    fun computeDepts(): ArrayList<Dept> {
        val avg = this.calcExpenseAverage()
        val averages = hashMapOf<User, Double>()

        for (user in this.users) {
            averages[user] = -avg + this.expenses.stream().filter { it.user!!.id == user.id }.mapToDouble { it.value!! }.sum()
        }

        val positives = averages.filter { it.value > 0 }
        val negatives = averages.filter { it.value < 0 }

        return arrayListOf()
    }
}

fun generateDummy(): Room {
    val room = Room()

    val u1 = dummyUser(1, "u1") // 65
    val u2 = dummyUser(2, "u2") // 50
    val u3 = dummyUser(3, "u3") // 35
    val u4 = dummyUser(4, "u4") // 80
    val u5 = dummyUser(5, "u5") // 25
    val u6 = dummyUser(6, "u6") // 0

    val e1 = dummyExpense(1, u1, 20.0)
    val e2 = dummyExpense(2, u1, 10.0)
    val e3 = dummyExpense(3, u1, 35.0)

    val e4 = dummyExpense(4, u2, 20.0)
    val e5 = dummyExpense(5, u2, 30.0)

    val e6 = dummyExpense(6, u3, 5.0)
    val e7 = dummyExpense(7, u3, 15.0)
    val e8 = dummyExpense(8, u3, 10.0)
    val e9 = dummyExpense(9, u3, 5.0)

    val e10 = dummyExpense(10, u4, 50.0)
    val e11 = dummyExpense(11, u4, 30.0)

    val e12 = dummyExpense(11, u5, 5.0)
    val e13 = dummyExpense(11, u5, 5.0)
    val e14 = dummyExpense(11, u5, 5.0)
    val e15 = dummyExpense(11, u5, 3.0)
    val e16 = dummyExpense(11, u5, 2.0)
    val e17 = dummyExpense(11, u5, 5.0)

    room.users.addAll(listOf(u1, u2, u3, u4, u5, u6))
    room.expenses.addAll(listOf(e1, e2, e3, e4, e5, e6, e7, e8, e9, e10, e11, e12, e13, e14, e15, e16, e17))

    return room
}

fun dummyUser(id: Int, name: String): User {
    val user = User()
    user.name = name
    user.username = name
    user.id = id

    return user
}

fun dummyExpense(id: Int, user: User, value: Double): Expense {
    val expense = Expense()
    expense.id = id
    expense.value = value
    expense.user = user
    return expense
}