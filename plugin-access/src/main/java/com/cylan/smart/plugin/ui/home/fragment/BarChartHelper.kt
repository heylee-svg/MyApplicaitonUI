package com.cylan.smart.plugin.ui.home.fragment

import android.graphics.Color
import android.view.View
import android.widget.TextView
import com.cylan.smart.plugin.data.bean.StaticsData
import com.cylan.smart.plugin.widget.DogBarChart
import com.github.mikephil.charting.components.AxisBase
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.formatter.IAxisValueFormatter
import com.github.mikephil.charting.formatter.IValueFormatter
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet
import com.github.mikephil.charting.utils.ViewPortHandler
import java.util.ArrayList

/**
 * @author Lupy create on 19-1-14
 * @Description　柱状图帮助类
 */

class BarChartHelper(var barChart: DogBarChart, var barChartEmptyView: TextView) {
    val age = arrayOf("<=17", "18~24", "25~29", "30~39", "40~49", ">=50")

    init {
        barChart.setTouchEnabled(false)
        barChart.setDrawBarShadow(false)
        barChart.setDrawValueAboveBar(true)
        val description = barChart.getDescription()
        description.setTextColor(Color.parseColor("#AEAEAE"))
        description.setTextSize(12f)
        barChart.setMaxVisibleValueCount(60)
        barChart.setDrawGridBackground(false)
        barChart.setNoDataText("")


        val xAxis = barChart.getXAxis()
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM)
        xAxis.setDrawGridLines(false)
        xAxis.setLabelCount(6)
        xAxis.setAxisLineColor(Color.parseColor("#f0f0f0"))
        xAxis.setDrawAxisLine(true)
        xAxis.setTextColor(Color.parseColor("#AEAEAE"))
        xAxis.setValueFormatter { value, axis ->
            val index = value.toInt()
            if (index < 0 || index >= age.size) {
                ""
            } else age[index]
        }

        val leftAxis = barChart.getAxisLeft()
        leftAxis.setEnabled(true)
        leftAxis.setLabelCount(5, false)
        leftAxis.setDrawAxisLine(false)
        leftAxis.enableGridDashedLine(10f, 5f, 0f)
        leftAxis.setGridColor(Color.parseColor("#f0f0f0"))
        leftAxis.setSpaceTop(15f)
        leftAxis.setTextSize(0f)
        leftAxis.setTextColor(Color.parseColor("#ffffff"))
        leftAxis.setAxisMinimum(0f)

        val rightAxis = barChart.getAxisRight()
        rightAxis.setEnabled(false)

        val l = barChart.getLegend()
        l.setEnabled(false)
    }

    /**
     * 过滤转换数据
     */
    fun filterAndTransforData(staticsData: List<StaticsData>) {
        barChartEmptyView.setVisibility(View.GONE)

        var ageCount: Array<Int> = arrayOf(0, 0, 0, 0, 0, 0)
        for (data in staticsData) {
            data.age_stats.forEach {
                var key = it.age
                var index = getIndexByKey(key)
                if (index != -1) {
                    ageCount[index] += it.num
                }
            }
        }


        val values = ArrayList<BarEntry>()
        for (i in 0..5) {
            values.add(BarEntry(i.toFloat(), ageCount[i].toFloat(), "labal"))
        }
        setData(values)
    }


    /**
     * 柱状图设置数据
     *
     * @param barEntries
     */
    private fun setData(barEntries: ArrayList<BarEntry>) {
        val barDataSet = BarDataSet(barEntries, "sdf")
        barDataSet.color = Color.parseColor("#5AD0D8")
        barDataSet.setDrawIcons(false)
        barDataSet.valueTextColor = Color.parseColor("#aeaeae")
        barDataSet.valueFormatter =
                IValueFormatter { value, entry, dataSetIndex, viewPortHandler ->
                    if (value.toInt() == 0) {
                        ""
                    } else Math.round(value).toString()
                }
        val dataSets = ArrayList<IBarDataSet>()
        dataSets.add(barDataSet)
        val data = BarData(dataSets)
        data.barWidth = 0.5f
        data.setValueTextSize(10f)
        barChart.setData(data)
        barChart.invalidate()
    }


    /**
     * 柱状图为空
     */
    private fun emptyBarChart() {
        val values = ArrayList<BarEntry>()
        for (i in 0..5) {
            values.add(BarEntry((i + 1).toFloat(), 0f, "labal"))
        }
        setData(values)
        barChartEmptyView.setVisibility(View.VISIBLE)
    }

    /**
     * "1,17", "18,24", "25,29", "30,39", "40,49", "50,80"
     */
    private fun getIndexByKey(key: String): Int = when (key) {
        "1,17" -> 0
        "18,24" -> 1
        "25,29" -> 2
        "30,39" -> 3
        "40,49" -> 4
        "50,80" -> 5
        else -> {
            -1
        }
    }

}