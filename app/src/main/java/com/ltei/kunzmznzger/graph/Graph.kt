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

      /* Exemple:
        val p1 = floatArrayOf(1f, 2f)
        val p2 = floatArrayOf(1.5f, 2.5f)
        val p3 = floatArrayOf(10f, 20f)
        val p4 = floatArrayOf(10.5f, 20.5f)

        val p5 = floatArrayOf(0.5f, 1.5f)
        val p6 = floatArrayOf(5f, 5f)
        val p7 = floatArrayOf(35f, 37f)
        val p8 = floatArrayOf(50f, 20.5f)
        val p9 = floatArrayOf(60f, 45.5f)

        val line_data: Array<FloatArray> = arrayOf(p1,p2,p3,p4)
        val line_data1: Array<FloatArray> = arrayOf(p5,p6,p7,p8,p9)

        val line_list: Array<Array<FloatArray>> = arrayOf(line_data,line_data1)

        Graph.plot_graph(chart , Graph.line_list)*/

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
            chart.invalidate() // refresh

        }

    }
}