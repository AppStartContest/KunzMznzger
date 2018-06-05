package com.ltei.kunzmznzger.models

import com.ltei.kunzmznzger.libs.Debt
import com.ltei.kunzmznzger.libs.models.Model
import com.ltei.kunzmznzger.libs.models.ModelManager
import com.ltei.kunzmznzger.models.dao.RoomDAO
import org.joda.time.DateTime
import org.json.simple.JSONObject
import java.io.Serializable
import kotlin.math.max

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

        val spentByUser = filtered.stream().mapToDouble { it.value!! }.sum()
        val roomAvg = this.calcExpenseAverage()

        return spentByUser - roomAvg
    }

    /**
     * Note: The room should have its expenses already loaded
     */
    fun calcExpenseAverage(): Double {
        val total = this.expenses.stream().mapToDouble { it.value!! }.sum()
        return total / this.users.size
    }

    // ----- Compute dept

    fun computeDepts(): ArrayList<Debt> {
        val debts = arrayListOf<Debt>()
        val avg = this.calcExpenseAverage()
        val averages = hashMapOf<User, Double>()

        for (user in this.users) {
            averages[user] = -avg + this.expenses.stream().filter { it.user!!.id == user.id }.mapToDouble { it.value!! }.sum()
        }

        val positives = averages.filter { it.value > 0 } as HashMap<User, Double>
        val negatives = averages.filter { it.value < 0 } as HashMap<User, Double>

        var totalToTransfer = positives.values.sum()

        // Algorithm:
        // If we find an equality, then we use it.
        // Else:
        //  The one who owes the most gives money to the one who spent the most.
        //  The amount of money is: (-1 * min(debt, spent))
        //  'debt' is the positive value of a negative float. (The difference between how much he paid and the room average)
        //  'spent' is the amount spent

        while (totalToTransfer != 0.0) {
            var maxPosUser = positives.keys.first()
            var maxPosValue = -1.0

            var minNegUser = negatives.keys.first()
            var minNegValue = 0.0

            // Direct correspondence
            for (positive in positives) {
                for (negative in negatives) {
                    if (negative.value < 0.0 && positive.value + negative.value == 0.0) {
                        maxPosUser = positive.key
                        minNegUser = negative.key
                        maxPosValue = positive.value
                        minNegValue = negative.value
                    }
                }
            }

            if (maxPosValue == -1.0 && minNegValue == 0.0) {
                // -------------
                // No direct correspondence found.

                // Looking for the max positive
                for ((puser, pvalue) in positives) {
                    if (pvalue == 0.0) continue
                    if (pvalue > maxPosValue) {
                        maxPosValue = pvalue
                        maxPosUser = puser
                    }
                }

                // Looking for the more negative
                for ((nuser, nvalue) in negatives) {
                    if (nvalue == 0.0) continue
                    // If the nvalue is lower than the lowest so far AND ...
                    if (nvalue < minNegValue) {
                        minNegValue = nvalue
                        minNegUser = nuser
                    }
                }

                minNegValue = max(minNegValue, -maxPosValue)
            }

            // Update the maps, register the dept and update the total
            positives[maxPosUser] = positives[maxPosUser]!! + minNegValue
            negatives[minNegUser] = negatives[minNegUser]!! - minNegValue

            debts.add(Debt(minNegUser, -minNegValue , maxPosUser))

            totalToTransfer += minNegValue
        }

        return debts
    }
}

fun generateDummy(): Room {
    val room = Room()

    val u1 = dummyUser(1, "u1") // 0
    val u2 = dummyUser(2, "u2") // 0
    val u3 = dummyUser(3, "u3") // 0
    val u4 = dummyUser(4, "u4") // 0
    val u5 = dummyUser(5, "u5") // 120
    val u6 = dummyUser(6, "u6") // 0

    val e12 = dummyExpense(11, u5, 20.0)
    val e13 = dummyExpense(11, u5, 20.0)
    val e14 = dummyExpense(11, u5, 20.0)
    val e15 = dummyExpense(11, u5, 20.0)
    val e16 = dummyExpense(11, u5, 20.0)
    val e17 = dummyExpense(11, u5, 20.0)

    room.users.addAll(listOf(u1, u2, u3, u4, u5, u6))
    room.expenses.addAll(listOf(e12, e13, e14, e15, e16, e17))

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