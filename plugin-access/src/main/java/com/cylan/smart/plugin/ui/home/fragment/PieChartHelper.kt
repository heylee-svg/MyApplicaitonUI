package com.cylan.smart.plugin.ui.home.fragment

import android.content.Context
import android.graphics.Color
import android.view.View
import android.widget.TextView
import com.cylan.smart.plugin.R
import com.cylan.smart.plugin.data.bean.StaticsData
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.github.mikephil.charting.formatter.PercentFormatter
import java.util.*

/**
 * @author Lupy create on 19-1-14
 * @Description
 */

class PieChartHelper(
    var pieChart: PieChart,
    var pieChartEmptyView: TextView,
    var maleSumTv: TextView,
    var femaleSumTv: TextView
) {
    lateinit var context: Context


    init {
        context = pieChart.context
        pieChart.setTouchEnabled(false)
        pieChart.setUsePercentValues(true)
        pieChart.getDescription().setEnabled(false)
        pieChart.setExtraOffsets(5f, 10f, 5f, 5f)
        pieChart.setDragDecelerationFrictionCoef(0.95f)
        pieChart.setDrawHoleEnabled(true)
        pieChart.setHoleColor(Color.WHITE)
        pieChart.setTransparentCircleColor(Color.WHITE)
        pieChart.setTransparentCircleAlpha(110)
        pieChart.setHoleRadius(58f)
        pieChart.setTransparentCircleRadius(0f)
        pieChart.setDrawCenterText(true)
        pieChart.setRotationAngle(0f)
        pieChart.setRotationEnabled(true)
        pieChart.setHighlightPerTapEnabled(true)

        val l = pieChart.getLegend()
        l.setEnabled(false)
        l.setVerticalAlignment(Legend.LegendVerticalAlignment.TOP)
        l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.RIGHT)
        l.setOrientation(Legend.LegendOrientation.VERTICAL)
        l.setDrawInside(false)
        l.setXEntrySpace(7f)
        l.setYEntrySpace(0f)
        l.setYOffset(0f)

        pieChart.setEntryLabelColor(Color.WHITE)
        pieChart.setEntryLabelTextSize(12f)
        pieChart.setNoDataText("")

    }


    fun filterAndTransforData(staticsData: List<StaticsData>) {
//        emptyPieChart()

        pieChartEmptyView.visibility = View.GONE
        var maleSum: Int = 0
        var femaleSum: Int = 0
        staticsData?.forEach {
            maleSum += it.gender_stats.male
            femaleSum += it.gender_stats.female
        }

        maleSumTv.setText(context.getString(R.string.HOME_GENDER_MALE) + "${maleSum}人")
        femaleSumTv.setText(context.getString(R.string.HOME_GENDER_FEMALE) + "${femaleSum}人")

        var datas = ArrayList<PieEntry>()
        datas.add(PieEntry(femaleSum.toFloat()))
        datas.add(PieEntry(maleSum.toFloat()))
        setPieData(datas)
    }

    /**
     * 饼状图设置数据
     *
     * @param entries
     */
    private fun setPieData(entries: ArrayList<PieEntry>) {

        val dataSet = PieDataSet(entries, "Election Results")
        dataSet.setDrawIcons(false)

        val colors = ArrayList<Int>()
        colors.add(Color.parseColor("#FF9DC6"))
        colors.add(Color.parseColor("#5AD0D8"))
        dataSet.colors = colors

        val data = PieData(dataSet)
        data.setValueFormatter(PercentFormatter())
        data.setValueTextSize(0f)
        data.setValueTextColor(Color.WHITE)
        pieChart.setData(data)
        pieChart.invalidate()
    }


    /**
     * 饼状图空视图
     */
    private fun emptyPieChart() {
        val entries = ArrayList<PieEntry>()
        //女的
        entries.add(PieEntry(0f, ""))
        //男的
        entries.add(PieEntry(100f, ""))
        setPieData(entries)

        maleSumTv.setText(context.getString(R.string.HOME_GENDER_MALE) + "0")
        femaleSumTv.setText(context.getString(R.string.HOME_GENDER_FEMALE) + "0")
//        pieChartEmptyView.setVisibility(View.VISIBLE)
    }

}