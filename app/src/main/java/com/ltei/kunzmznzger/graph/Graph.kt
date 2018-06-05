package com.ltei.kunzmznzger.graph
import android.os.Parcel
import android.os.Parcelable
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.charts.*
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet
import com.github.mikephil.charting.utils.ColorTemplate
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*
import com.ltei.kunzmznzger.graph.DayAxisValueFormatter
import com.ltei.kunzmznzger.models.Expense
import com.ltei.kunzmznzger.models.Room
import com.ltei.kunzmznzger.models.User
import org.joda.time.DateTime
import org.joda.time.Days
import kotlin.collections.ArrayList

/**
 * Add this to your layout:
 *     <com.github.mikephil.charting.charts.LineChart
 *          android:id="@+id/chart"
 *          android:layout_width="match_parent"
 *          android:layout_height="match_parent" />
 *
 * // kunzum gradle all project :  maven { url 'https://jitpack.io' }
 * // app gradle : implementation 'com.github.PhilJay:MPAndroidChart:v3.0.3'
 */


class Graph() {

    companion object {

        /*val end = DateTime.now()
        val start = DateTime(2016,1,1,1,1)

        val nb_day = Days.daysBetween(start.toLocalDate(), end.toLocalDate()).getDays()*/


//        val p1 = floatArrayOf(1f, 2f)
//        val p2 = floatArrayOf(1.5f, 2.5f)
//        val p3 = floatArrayOf(10f, 20f)
//        val p4 = floatArrayOf(10.5f, 20.5f)
//
//        val p5 = floatArrayOf(0.5f, 1.5f)
//        val p6 = floatArrayOf(5f, 5f)
//        val p7 = floatArrayOf(35f, 37f)
//        val p8 = floatArrayOf(50f, 20.5f)
//        val p9 = floatArrayOf(60f, 45.5f)
//
//        val line_data: Array<FloatArray> = arrayOf(p1,p2,p3,p4)
//        val line_data1: Array<FloatArray> = arrayOf(p5,p6,p7,p8,p9)
//
//        val line_list: Array<Array<FloatArray>> = arrayOf(line_data,line_data1)

        /*Graph.plot_graph(chart , Graph.line_list)*/

        fun plot_expense_global(chart: LineChart , expense_list : ArrayList<Expense>) {

            val entry_list : ArrayList<ArrayList<Entry>> = ArrayList()

            entry_list.add(arrayListOf())
            for (i in 0 until expense_list.size) {

                // turn your data into Entry objects

                var time_value = get_nb_day(expense_list[i].createdAt)
                entry_list[0].add(Entry(time_value,expense_list[i].value!!.toFloat()))
            }

            val dataset_list: ArrayList<LineDataSet> = arrayListOf()

            for (i in 0 until entry_list.size)
            {
                val dataSet = LineDataSet(entry_list[i], "Global expense") // add entries to dataset
                // create random object - reuse this as often as possible
                val random = Random()
                // create a big random number - maximum is ffffff (hex) = 16777215 (dez)
                val nextInt = random.nextInt(256 * 256 * 256)
                // format it as hexadecimal string (with hashtag and leading zeros)
                val colorCode = String.format("#%06x", nextInt)
                dataSet.color = ColorTemplate.rgb(colorCode)
                dataSet.valueTextColor = ColorTemplate.rgb(colorCode)
                dataset_list.add(dataSet)
            }

            val lineData = LineData(dataset_list as List<ILineDataSet>?)

            chart.data = lineData
            chart.getXAxis().setValueFormatter( DayAxisValueFormatter(chart));
            chart.invalidate() // refresh

        }

        fun plot_expense_detail(chart: LineChart , expense_list : ArrayList<Expense>) {

            val entry_list : ArrayList<ArrayList<Entry>> = ArrayList()
            val name_list : ArrayList<String?> = ArrayList()

            var sortedList = expense_list.sortedWith(compareBy({ it.user!!.id }))

            entry_list.add(arrayListOf())
            var j = 0
            var id = sortedList[0].user!!.id
            name_list.add(sortedList[0].user?.name)
            for (i in 0 until sortedList.size) {

                if (id == sortedList[i].user!!.id){
                    var time_value = get_nb_day(sortedList[i].createdAt)
                    entry_list[j].add(Entry(time_value,sortedList[i].value!!.toFloat()))
                }
                else
                {
                    id = sortedList[i].user!!.id
                    entry_list.add(arrayListOf())
                    j+=1
                    var time_value = get_nb_day(sortedList[i].createdAt)
                    entry_list[j].add(Entry(time_value,sortedList[i].value!!.toFloat()))
                    name_list.add(sortedList[i].user?.name)
                }


            }

            val dataset_list: ArrayList<LineDataSet> = arrayListOf()

            for (i in 0 until entry_list.size)
            {
                val dataSet = LineDataSet(entry_list[i],name_list[i]) // add entries to dataset
                // create random object - reuse this as often as possible
                val random = Random()
                // create a big random number - maximum is ffffff (hex) = 16777215 (dez)
                val nextInt = random.nextInt(256 * 256 * 256)
                // format it as hexadecimal string (with hashtag and leading zeros)
                val colorCode = String.format("#%06x", nextInt)
                dataSet.color = ColorTemplate.rgb(colorCode)
                dataSet.valueTextColor = ColorTemplate.rgb(colorCode)
                dataset_list.add(dataSet)
            }

            val lineData = LineData(dataset_list as List<ILineDataSet>?)

            chart.data = lineData
            chart.getXAxis().setValueFormatter( DayAxisValueFormatter(chart));
            chart.invalidate() // refresh

        }

        fun get_nb_day(dateTime: DateTime?) : Float {

            val end = dateTime
            val start = DateTime(2016,1,1,1,1)

            return Days.daysBetween(start.toLocalDate(), end!!.toLocalDate()).getDays().toFloat()
        }

        fun plot_graph(chart: LineChart , line_list : Array<Array<FloatArray>>) {

            val entry_list : ArrayList<ArrayList<Entry>> = ArrayList()

            for (i in 0 until line_list.size) {
                entry_list.add(arrayListOf())
                // turn your data into Entry objects
                for (j in 0 until line_list[i].size){
                entry_list[i].add(Entry(line_list[i][j][0],line_list[i][j][1]))
                }
            }

            val dataset_list: ArrayList<LineDataSet> = arrayListOf()

            for (i in 0 until entry_list.size)
            {
                val dataSet = LineDataSet(entry_list[i], "Label" + i) // add entries to dataset
                // create random object - reuse this as often as possible
                val random = Random()
                // create a big random number - maximum is ffffff (hex) = 16777215 (dez)
                val nextInt = random.nextInt(256 * 256 * 256)
                // format it as hexadecimal string (with hashtag and leading zeros)
                val colorCode = String.format("#%06x", nextInt)
                dataSet.color = ColorTemplate.rgb(colorCode)
                dataSet.valueTextColor = ColorTemplate.rgb(colorCode)
                dataset_list.add(dataSet)
            }

            val lineData = LineData(dataset_list as List<ILineDataSet>?)

            chart.data = lineData
            chart.getXAxis().setValueFormatter( DayAxisValueFormatter(chart));
            chart.invalidate() // refresh

        }

        fun generateDummy(): Room {
            val room = Room()

            val u1 = dummyUser(1, "u1") // 0
            val u2 = dummyUser(2, "u2") // 0
            val u3 = dummyUser(3, "u3") // 0
            val u4 = dummyUser(4, "u4") // 0
            val u5 = dummyUser(5, "u5") // 120
            val u6 = dummyUser(6, "u6") // 0

            val e12 = dummyExpense(11, u5, 20.0,1)
            val e13 = dummyExpense(11, u5, 50.0,2)
            val e14 = dummyExpense(11, u5, 15.0,3)
            val e15 = dummyExpense(11, u5, 20.0,4)
            val e16 = dummyExpense(11, u5, 45.0,5)
            val e17 = dummyExpense(11, u5, 99.0,6)

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

        fun dummyExpense(id: Int, user: User, value: Double , day : Int): Expense {
            val expense = Expense()
            expense.id = id
            expense.value = value
            expense.user = user

            expense.createdAt = DateTime.now().plusDays(day)
            return expense
        }
    }
}