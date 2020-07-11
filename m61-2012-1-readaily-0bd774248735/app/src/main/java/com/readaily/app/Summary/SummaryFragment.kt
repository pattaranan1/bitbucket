package com.readaily.app.Summary

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.github.mikephil.charting.charts.HorizontalBarChart
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import com.readaily.app.DBHandler
import com.readaily.app.R
import kotlinx.android.synthetic.main.fragment_summary.*
import kotlin.collections.ArrayList


class SummaryFragment : Fragment() {


    lateinit var barChart: HorizontalBarChart
    lateinit var dbHandler: DBHandler

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_summary, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val entries = ArrayList<BarEntry>()

        var xAxisValues = arrayListOf("Mathematics","Physics","Chemistry","Biology","Computer","English","Thai","Social","History")
        var yAxisValues = arrayListOf<Float>(0f,0f,0f,0f,0f,0f,0f,0f,0f)

        dbHandler = DBHandler(requireContext())

        val db = dbHandler.getToDoitems()

        if(db.size != 0){
            for (i in db) {
                val index = xAxisValues.indexOf(i.subjectItem)
                yAxisValues[index] = yAxisValues[index] + (i.totaltimeItem.toFloat() / 60)
            }
        }


        for (i in yAxisValues.indices) {
            entries.add(BarEntry(i.toFloat(), yAxisValues[i]))//wait for timmer is ready
        }



        val bardataset = BarDataSet(entries,"Total time of reading")

        val bardata = BarData(bardataset)

        chart.description.text = "Time Spending (Minutes)"
        chart.description.textSize = 12f


        chart.xAxis.valueFormatter = IndexAxisValueFormatter(xAxisValues)
        chart.xAxis.position = XAxis.XAxisPosition.BOTTOM
        chart.xAxis.textSize = 14f
        chart.xAxis.setDrawGridLines(false)
        chart.xAxis.granularity = 1f
        chart.xAxis.isGranularityEnabled = true

        chart.data = bardata
        chart.setFitBars(true)
        //chart.xAxis.setCenterAxisLabels(true)
        chart.xAxis.setLabelCount(9)


        chart.axisLeft.setDrawLabels(false)
        chart.axisLeft.granularity = 1f
        chart.axisLeft.isGranularityEnabled = true

        chart.axisRight.granularity = 10f

        chart.axisRight.isGranularityEnabled = true
        chart.axisRight.setDrawGridLines(true)
        chart.axisRight.textSize = 14f

        chart.setVisibleYRangeMinimum(6f,chart.axisRight.axisDependency)
        chart.data.setDrawValues(false)

        chart.invalidate()


    }

    override fun setUserVisibleHint(isVisibleToUser: Boolean) {
        super.setUserVisibleHint(isVisibleToUser)

        fragmentManager!!.beginTransaction().detach(this).attach(this).commit()
    }

}


