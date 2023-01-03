package com.example.spa.ui.home.fragment

import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.core.content.ContentProviderCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.example.spa.App
import com.example.spa.R

import com.example.spa.base.BaseFragment
import com.example.spa.data.request.GraphRequest
import com.example.spa.data.response.GraphDataList
import com.example.spa.databinding.FragmentSendBinding
import com.example.spa.ui.auth.activity.AuthActivity
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
        resentTransactionResponse()
        apiCall()
        graphDataResponse()
       // apiCallGraph("weekly")
    }
    private fun apiCall() {
        if (hasInternet(requireContext())) {
            internetManage(true)
            lifecycleScope.launchWhenCreated {
                 settingsViewModel.transactionList(
                    session.token, 0
                )
            }
        }else{
            internetManage(false)
        }
    }

    private fun apiCallGraph(day:String){
        if (hasInternet(requireContext())) {
            internetManage(true)
            lifecycleScope.launchWhenCreated {
                settingsViewModel.graphDataResponse(
                    session.token, day
                )
            }
        }else{
            internetManage(false)
        }
    }

    private fun internetManage(boolean: Boolean){
        if (boolean){
            binding.includeNoInternet.layoutNoInternet.hideView()
            binding.layoutAmount.showView()
            binding.layoutGraph.showView()
            binding.LayoutResentTransaction.showView()

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
    private val listChartDayValues = mutableListOf<GraphDataList>()
    private var listBarEntry = mutableListOf<BarEntry>()
    private var listDays = mutableListOf<GraphDataList>()
    private var listMonths = mutableListOf<GraphDataList>()

    private fun fillBarChartData() {
        listChartDayValues.clear()
        listBarEntry.clear()

        if (pos ==0) {
            apiCallGraph("weekly")
        }else{
            apiCallGraph("monthly")
        }
    }

    private fun setUpBarChart() {


        barChartStyle = if(pos == 1) {

            BarChartStyle(
                requireContext(),
                listMonths,
                ANALYTICS.MONTH(TIMES = listMonths.size)
            )
        }else{
            BarChartStyle(
                requireContext(),
                listDays,
                ANALYTICS.WEEK(TIMES = listDays.size)
            )
        }
        listChartDayValues.forEachIndexed { index, chartDays ->
           // if (chartDays.amount.toFloat() == 0f) {
                //chartDays.amount = 100
            //}
                listBarEntry.add(BarEntry(index.toFloat(), chartDays.amount.toFloat()))

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
                Handler().postDelayed({
                    toggleLoader(false)
                }, 3000)
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

    private fun graphDataResponse(){
        lifecycleScope.launchWhenCreated {
            settingsViewModel.graphData.collect { result ->
                toggleLoader(true)
                when (result) {
                    is Resource.Error -> {
                        toggleLoader(false)
                        result.message?.let {
                            showMessage(binding.root,it)
                        }
                    }
                    is Resource.Loading -> {}
                    is Resource.Success -> {
                        toggleLoader(false)
                        result.data?.let { it ->
                            if (pos == 0) {
                                listDays.clear()
                                for (i in 0 until it.list.size) {
                                    listDays.add(it.list[i])
                                    listChartDayValues.add(i,it.list[i])
                                }
                                setUpBarChart()
                            }else{
                                listMonths.clear()
                                for (i in 0 until it.list.size) {
                                    listMonths.add(it.list[i])
                                    listChartDayValues.add(i,it.list[i])
                                }
                                setUpBarChart()
                            }
                        }
                    }
                }
            }
        }
    }
}