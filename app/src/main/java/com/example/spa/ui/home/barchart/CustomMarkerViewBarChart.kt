package com.example.spa.ui.home.barchart

import android.content.Context
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.example.spa.R
import com.example.spa.utilities.hideView
import com.example.spa.utilities.showView
import com.github.mikephil.charting.components.MarkerView
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.highlight.Highlight
import com.github.mikephil.charting.utils.MPPointF

class CustomMarkerViewBarChart(context: Context?, private val listChartData: List<BarEntry>) :
    MarkerView(context, R.layout.chart_custom_marker_view) {
    companion object {
        private const val TAG = "CustomMarkerView"
    }


    private val totalWidth = resources.displayMetrics.widthPixels
    private val textViewContent: TextView = findViewById<View>(R.id.textViewContent) as TextView
    private val imageViewBubbleEnd: ImageView =
        findViewById<View>(R.id.imageViewMessageBubbleEnd) as ImageView
    private val imageViewBubbleCenter: ImageView =
        findViewById<View>(R.id.imageViewMessageBubbleCenter) as ImageView
    private val imageViewBubbleStart: ImageView =
        findViewById<View>(R.id.imageViewMessageBubbleStart) as ImageView

    override fun refreshContent(e: Entry?, highlight: Highlight?) {
        //tvContent.text = "$${{ String.format("%.0f", (e?.y)?.div(10000)) }}"
        //textViewContent.text = "${e?.y}k"
        textViewContent.text = "${e?.y}"
        super.refreshContent(e, highlight)
    }

    override fun getOffsetForDrawingAtPoint(posX: Float, posY: Float): MPPointF {
        val supposedX = posX + width
        val mpPointF = MPPointF()

        mpPointF.x = when {
            supposedX > totalWidth -> {
                imageViewBubbleEnd.showView()
                imageViewBubbleCenter.hideView()
                imageViewBubbleStart.hideView()
                -width.toFloat() + 20f
            }
            posX - width < 0 -> {
                imageViewBubbleEnd.hideView()
                imageViewBubbleCenter.hideView()
                imageViewBubbleStart.showView()
                -20f
            }
            else -> {
                imageViewBubbleEnd.hideView()
                imageViewBubbleCenter.showView()
                imageViewBubbleStart.hideView()
                -width.toFloat() / 2
            }
        }

        mpPointF.y = if (posY > height)
            -height.toFloat() - 20f
        else
            0f

        return mpPointF
    }
}