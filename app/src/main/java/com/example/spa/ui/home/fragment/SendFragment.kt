package com.example.spa.ui.home.fragment

import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.example.spa.R

import com.example.spa.base.BaseFragment
import com.example.spa.databinding.FragmentSendBinding
import com.example.spa.ui.home.activitiy.IsolatedActivity
import com.example.spa.ui.home.adapter.ResentTransactionAdapter
import com.example.spa.ui.home.barchart.*
import com.example.spa.utilities.*
import com.example.spa.utilities.validation.ApplicationException
import com.example.spa.viewmodel.SettingsViewModel
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import kotlinx.android.synthetic.main.layout_phone_number.view.*


class SendFragment(context: Context) : BaseFragment() {


    private val settingsViewModel: SettingsViewModel by viewModels()

    val resentTransactionAdapter = ResentTransactionAdapter()

    private lateinit var binding: FragmentSendBinding
    private lateinit var barChartStyle: BarChartStyle
    var pos:Int = 0
    var LIST = arrayOf("Item 1", "Item 2", "Item 3", "Item 4")
    var LIST_MONTHLY = arrayOf("weekly", "Monthly")
    var dialog: Dialog = Dialog(context)
    private lateinit var editText:AutoCompleteTextView
    private lateinit var cancel:ImageView
    private lateinit var send:View

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSendBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setAdapter()
        setClick()
        fillBarChartData()

        resentTransactionResponse()
        apiCall()
    }

    private fun apiCall() {
        if (hasInternet(requireContext())) {
            binding.includeNoInternet.layoutNoInternet.hideView()
            binding.layoutAmount.showView()
            binding.layoutGraph.showView()
            binding.LayoutResentTransaction.showView()

            lifecycleScope.launchWhenCreated {
                 settingsViewModel.transactionList(
                    session.token, 0
                )
            }
        }else{
            binding.includeNoInternet.layoutNoInternet.showView()
            binding.layoutAmount.hideView()
            binding.layoutGraph.hideView()
            binding.LayoutResentTransaction.hideView()
        }
    }

    private fun openActivity(fragment: String){
        val intent= Intent(requireContext(), IsolatedActivity::class.java)
        intent.putExtra(Constants.SCREEN_NAME,fragment)
        startActivity(intent)
    }

    override fun onResume() {
        super.onResume()
        binding.spinner2.setSelection(0, true);
    }

    private fun setClick() {
        val adapter2: ArrayAdapter<String> = ArrayAdapter(
            requireContext(),
            R.layout.layout_item_monthly,
            LIST_MONTHLY
        )
            binding.spinner2.adapter = adapter2;
            binding.spinner2.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
              try {
                  (parent.getChildAt(0) as TextView?)!!.setTextColor(Color.WHITE)
                  (parent.getChildAt(0) as TextView?)!!.setCompoundDrawablesWithIntrinsicBounds(
                      0,
                      0,
                      R.drawable.ic_bottom_arrow_white,
                      0
                  )
                  (parent.getChildAt(0) as TextView?)!!.compoundDrawablePadding = 10;
                  pos = position
                  fillBarChartData()
              }catch (e : Exception){}
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }
        binding.includeNoInternet.buttonTryAgain.setOnClickListener {
            apiCall()
        }
        binding.buttonGenerateQRCode.setOnClickListener {
            if (isValid()) {
                dialog.setContentView(R.layout.enter_reason_dialouge);
                dialog.window!!.setLayout(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
                );
                dialog.setCancelable(true);
                dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
                editText = dialog.findViewById(R.id.textInputReason);
                cancel = dialog.findViewById(R.id.imageViewCross)
                send = dialog.findViewById(R.id.buttonSend)
                send.setOnClickListener {
                    openActivity(Constants.SHARE_QR)
                     dialog.dismiss()
                }
                cancel.setOnClickListener { dialog.dismiss() }
                val adapter: ArrayAdapter<String> = ArrayAdapter(
                    requireContext(),
                    R.layout.layout_item,
                    LIST
                )
                editText.setAdapter(adapter)
                dialog.show();
            }
        }
    }



    private fun isValid(): Boolean {
        try {
            validator.submit(binding.editTextAmount)
                .checkEmpty().errorMessage(getString(R.string.error_enter_amount))
                .check()
        } catch (e: ApplicationException) {
            showMessage(binding.root,e.message)
            return false
        }
        return true
    }

    //BarChart
    private val listChartDayValues = mutableListOf<ChartDays>()
    private var listBarEntry = mutableListOf<BarEntry>()
    private var listDays = mutableListOf<Days>()
    private var listMonths = mutableListOf<Days>()

    private fun fillBarChartData() {
        listChartDayValues.clear()
        listBarEntry.clear()

        if (pos ==0) {
            listChartDayValues.add(ChartDays(1F, 9F))
            listChartDayValues.add(ChartDays(2F, 4F))
            listChartDayValues.add(ChartDays(3F, 8F))
            listChartDayValues.add(ChartDays(4F, 6F))
            listChartDayValues.add(ChartDays(5F, 4.2F))
            listChartDayValues.add(ChartDays(6F, 4.3F))
            listChartDayValues.add(ChartDays(7F, 9.6F))
        }else{
            listChartDayValues.add(ChartDays(1F, 90F))
            listChartDayValues.add(ChartDays(2F, 40F))
            listChartDayValues.add(ChartDays(3F, 88F))
            listChartDayValues.add(ChartDays(4F, 63F))
            listChartDayValues.add(ChartDays(5F, 42F))
            listChartDayValues.add(ChartDays(6F, 43F))
            listChartDayValues.add(ChartDays(7F, 90F))
            listChartDayValues.add(ChartDays(8F, 92F))
            listChartDayValues.add(ChartDays(9F, 42F))
            listChartDayValues.add(ChartDays(10F, 58F))
            listChartDayValues.add(ChartDays(11F, 76F))
            listChartDayValues.add(ChartDays(12F, 42F))
        }
        //After add data call barchart setup
        setUpBarChart()
    }

    private fun setUpBarChart() {
        listDays.add(Days("Mon"))
        listDays.add(Days("Tue"))
        listDays.add(Days("Wed"))
        listDays.add(Days("Thu"))
        listDays.add(Days("Fri"))
        listDays.add(Days("Sat"))
        listDays.add(Days("Sun"))

        listMonths.add(Days("Jan"))
        listMonths.add(Days("Feb"))
        listMonths.add(Days("Mar"))
        listMonths.add(Days("Apr"))
        listMonths.add(Days("May"))
        listMonths.add(Days("Jun"))
        listMonths.add(Days("July"))
        listMonths.add(Days("Aug"))
        listMonths.add(Days("Sep"))
        listMonths.add(Days("Oct"))
        listMonths.add(Days("Nov"))
        listMonths.add(Days("Dec"))


        barChartStyle = if(pos == 1) {
            BarChartStyle(
                requireContext(),
                listMonths,
                ANALYTICS.DAYS(TIMES = listMonths.size)
            )
        }else{
            BarChartStyle(
                requireContext(),
                listDays,
                ANALYTICS.DAYS(TIMES = listDays.size)
            )
        }
        listChartDayValues.forEachIndexed { index, chartDays ->
            listBarEntry.add(BarEntry(index.toFloat(), chartDays.value))
        }

        val barDataSet = BarDataSet(listBarEntry, "BarChartStyle")
        val barData = BarData(barDataSet)

        binding.barChart.apply {

            val barChartRender =
                CustomBarChartRender(this, this.animator, this.viewPortHandler)
            barChartRender.setRadius(20)
            this.renderer = barChartRender

            val mv = CustomMarkerViewBarChart(requireActivity(), listBarEntry)
            mv.chartView = this
            marker = mv

            data = barData
            //xAxis.valueFormatter = IndexAxisValueFormatter(listLabelName)
//
            barChartStyle.styleBarChart(this)
            barChartStyle.styleBarDataSet(barDataSet)
        }
    }



    private fun setAdapter() {
        binding.recycleViewTransaction.adapter = resentTransactionAdapter
    }


    private fun resentTransactionResponse(){
        lifecycleScope.launchWhenCreated {
            settingsViewModel.transaction.collect { result ->
                toggleLoader(true)
                when (result) {
                    is Resource.Error -> {
                        toggleLoader(false)
                        binding.LayoutResentTransaction.hideView()
                        result.message?.let {
                            showMessage(binding.root,it)
                        }
                    }
                    is Resource.Loading -> {}
                    is Resource.Success -> {
                        toggleLoader(false)
                        result.data?.let { it ->
                            if (it.list.isEmpty()){
                                binding.LayoutResentTransaction.hideView()
                            }else{
                                binding.LayoutResentTransaction.showView()
                                resentTransactionAdapter.setListItem(it.list)
                            }
                           }
                    }
                }
            }
        }
    }
}