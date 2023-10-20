package com.example.spa.ui.home.fragment

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.spa.R
import com.example.spa.base.BaseFragment
import com.example.spa.databinding.TransactionFragmentBinding
import com.example.spa.ui.home.activity.IsolatedActivity
import com.example.spa.ui.home.adapter.ResentAdapter
import com.example.spa.utilities.*
import com.example.spa.viewmodel.SettingsViewModel
import kotlinx.coroutines.launch

class TransactionFragment : BaseFragment() {


    private var doubleTapTime: Long = 0L
    private val CLICK_INTERVAL = 1000
    private var loading: Boolean = false
    private val settingsViewModel: SettingsViewModel by viewModels()
    var page = 0
    lateinit var resentTransactionAdapter :ResentAdapter


    private lateinit var binding: TransactionFragmentBinding
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        resentTransactionAdapter=ResentAdapter(requireContext())
        binding = TransactionFragmentBinding.inflate(layoutInflater)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setAdapter()
        setClick()
        resentTransactionResponse()
        page = 0
         apiCall()

        binding.swipeRefreshLayout.setOnRefreshListener {
            page = 0
            apiCall()
            binding.swipeRefreshLayout.isRefreshing = false
        }
        binding.noDataFoundLayout.textViewNoBankAccountAdded.setText(getString(R.string.no_transactions_yet))
    }


    private fun setClick() {
        binding.buttonWithdrawal.setOnClickListener {
            openActivity(Constants.WALLET)
        }
        binding.includeNoInternet.buttonTryAgain.setOnClickListener {
            page = 0
            apiCall()
        }
    }

    private fun openActivity(fragment: String) {
        val intent = Intent(requireContext(), IsolatedActivity::class.java)
        intent.putExtra(Constants.SCREEN_NAME, fragment)
        startActivity(intent)
    }

    private fun setAdapter() {
        binding.recycleViewTransaction.adapter = resentTransactionAdapter
        setUpLoadMoreListener()
    }


    private fun apiCall() {
        if (hasInternet(requireContext())) {
            Log.e("aaaa",session.token)
            binding.includeNoInternet.layoutNoInternet.hideView()
            binding.swipeRefreshLayout.showView()


            lifecycleScope.launch {
                settingsViewModel.transactionList(
                    session.token, page
                )
            }
        } else {
            binding.includeNoInternet.layoutNoInternet.showView()
            binding.swipeRefreshLayout.hideView()
        }
    }

    private fun resentTransactionResponse() {
        lifecycleScope.launchWhenCreated {
            settingsViewModel.transaction.collect { result ->
                if (page == 0) {
                    toggleLoader(true)
                    Handler().postDelayed({
                        toggleLoader(false)
                    }, 3000)
                }
                when (result) {
                    is Resource.Error -> {
                        toggleLoader(false)
                        loading = false
                        if (result.code == 403){
                            logoutMainApp(requireActivity())
                        }else{
                            result.message?.let {
                                showMessage(binding.root, it)
                            }
                        }

                    }
                    is Resource.Loading -> {}
                    is Resource.Success -> {
                        toggleLoader(false)
                        loading = false
                        result.data?.let { it ->
                            Log.e("TAG", "resentTransactionResponse: ${page}")

                            if (page <= 0) {
                                if (it.list.isEmpty()) {
                                    binding.noDataFoundLayout.textViewNoBankAccountAdded.showView()
                                } else {
                                    Log.e("TAG", "resentTransactionResponse: ${it.list}")
                                    binding.noDataFoundLayout.textViewNoBankAccountAdded.hideView()
                                    resentTransactionAdapter.setListItem(it.list)
                                }
                            } else {
                                resentTransactionAdapter.setListItem2(it.list)
                            }
                            page = it.page_number
                        }
                    }
                }
            }
        }
    }


    private fun setUpLoadMoreListener() {
        binding.recycleViewTransaction.addOnScrollListener(object :
            PageListener(binding.recycleViewTransaction.layoutManager as LinearLayoutManager) {
            override fun onScrolledItems(recyclerView: RecyclerView, newState: Int) {

            }

            override fun callWs(): Boolean {
                return resentTransactionAdapter.arrayList.size > Constants.DEFAULT_PAGE_SIZE
            }

            override fun isLoading(): Boolean {
                return loading
            }

            override fun loadMoreItems() {
                if (page != -1) {
                    apiCall()
                    loading = true
                }
            }
        })
    }

}