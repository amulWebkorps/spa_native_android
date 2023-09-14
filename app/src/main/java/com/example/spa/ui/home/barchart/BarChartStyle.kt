package com.example.spa.ui.home.barchart

import android.content.Context
import android.util.Log
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import com.example.spa.R
import com.example.spa.data.response.GraphDataList
import com.example.spa.utilities.coolNumberFormat
import com.example.spa.utilities.formatDate
import com.example.spa.utilities.formatDateGraph
import com.example.spa.utilities.monthNameToNumber
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.formatter.ValueFormatter


sealed class ANALYTICS {
    data class DAYS(val TIMES: Int = 7) : ANALYTICS()
    data class WEEK(val TIMES: Int = 7) : ANALYTICS()
    data class MONTH(val TIMES: Int = 12) : ANALYTICS()
    data class YEAR(val TIMES: Int = 7) : ANALYTICS()
}

class BarChartStyle(
    private val context: Context,
    private val listDays: List<GraphDataList>,
    private val analytics: ANALYTICS
) {



    fun styleBarChart(barChart: BarChart) = barChart.apply {

        animateX(1000)
        animateY(1000)
        setExtraOffsets(0f, 50f, 20f, 20f)
        invalidate()
        axisLeft.valueFormatter = MyAxisValueFormat()
        axisRight.isEnabled = false

        axisLeft.apply {
            isEnabled = true

            Log.e("TAG", "styleBarChart: ${listDays}", )
           axisMinimum = 1F
           // axisMaximum = 10F*/
            granularity = 1F

            setDrawGridLines(false)
            setDrawAxisLine(false)

            textColor = ContextCompat.getColor(context, R.color.black)
            typeface = ResourcesCompat.getFont(context, R.font.poppins_medium)
            textSize = 12F

            /*spaceMax = 2.5f
            spaceMin = 2.5f
            spaceBottom = 10.5f*/
        }

        xAxis.apply {
            valueFormatter = MyXAxisValueFormat(listDays, analytics,context)

//            axisMinimum = 0f
            //axisMaximum = 29f*/

            isGranularityEnabled = true
            granularity = 1F
            setDrawGridLines(false)
            setDrawAxisLine(false)
//            labelCount = 7
            position = XAxis.XAxisPosition.BOTTOM
//            setLabelCount(7, false)
            when (analytics) {
                is ANALYTICS.DAYS -> setLabelCount(analytics.TIMES, false)
                is ANALYTICS.MONTH -> setLabelCount(analytics.TIMES, false)
                is ANALYTICS.WEEK -> setLabelCount(analytics.TIMES, false)
                is ANALYTICS.YEAR -> setLabelCount(analytics.TIMES, false)
            }
            setCenterAxisLabels(false)
            mLabelWidth = 10

            //spaceMin = 2f
            //spaceMax = 10f

            textColor = ContextCompat.getColor(context, R.color.black)
            typeface = ResourcesCompat.getFont(context, R.font.poppins_medium)
            textSize = 12F
        }

        setTouchEnabled(true)
        isDragEnabled = true
        setScaleEnabled(false)
        setPinchZoom(false)
        description = null
        legend.isEnabled = false
        //set width of bar
        data.barWidth = 0.40f
        setBackgroundColor(ContextCompat.getColor(context, R.color.white))

        isHighlightPerTapEnabled = true
        isHighlightPerDragEnabled = true
        isDoubleTapToZoomEnabled = false
        invalidate()
        setVisibleXRange(7F,7F)
        //moveViewToX(1F)
    }

    fun styleBarDataSet(barDataSet: BarDataSet) = barDataSet.apply {
        color = ContextCompat.getColor(context, R.color.black)
        valueTextColor = ContextCompat.getColor(context, R.color.grey)
        setDrawValues(false)
        isHighlightEnabled = false
        highLightAlpha = 10

        highLightAlpha = ContextCompat.getColor(context, R.color.grey)
        highLightColor = ContextCompat.getColor(context, R.color.grey)

        barBorderColor = ContextCompat.getColor(context, R.color.colorBlue72)
//        gradientColors = listOf( GradientColor(  R.color.colorBlue72,R.color.colorGrey7C))
//        setGradientColor(Color.parseColor("#707070") , Color.parseColor("#F5F7F9"))
        barBorderWidth = 15f
    }


    private class MyAxisValueFormat : ValueFormatter() {
        override fun getFormattedValue(value: Float): String {
//            val numberWithOutDot = String.format("%.0f", value / 1000000000)
//            val numberWithOutDot = String.format("%.0f", value)
//            return "${numberWithOutDot.toLong().coolNumberFormat()}    "

            return "${value.toLong().coolNumberFormat()}"
        }
    }

    private class MyXAxisValueFormat(val listDays: List<GraphDataList>, val analytics: ANALYTICS,val context: Context) :
        ValueFormatter() {
        //val listDays = mutableListOf("Sun","Mon","Tue","Wed","Thu","Fri","Sat")
        override fun getFormattedValue(value: Float): String {
            //Log.e("Hello", "getFormattedValue: ${value / 10}")
            /*val numberWithOutDot = String.format("%.0f", (value / 10))
            return "${numberWithOutDot.toInt() + 1}w"*/
            return try {
                when (analytics) {
//                    is ANALYTICS.YEAR -> {
//                        listDays[value.toInt()].month.subSequence(0, 3).toString()
//                    }
                    is ANALYTICS.MONTH -> {
//                        listDays[value.toInt()].value_for.subSequence(0, 3).toString()
                         "${monthNameToNumber(listDays[value.toInt()].value_for.subSequence(0, 3).toString(), context)}"
                    }
                    else -> {
                        formatDateGraph(listDays[value.toInt()].date)
                      //  listDays[value.toInt()].value_for.subSequence(0, 3).toString()
                    }
                }
            } catch (e: Exception) {
                value.toInt().toString()
            }
            //return value.toInt().toString()
        }
    }
}