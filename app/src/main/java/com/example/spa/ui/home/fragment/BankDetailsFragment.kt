package com.example.spa.ui.home.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.example.spa.R
import com.example.spa.base.BaseFragment
import com.example.spa.base.DialogUtils
import com.example.spa.base.listener.BankDetailScreen
import com.example.spa.databinding.FragmentBankDetailBinding
import com.example.spa.ui.home.adapter.BankDetailAdapter
import com.example.spa.utilities.*
import com.example.spa.viewmodel.SettingsViewModel

class BankDetailsFragment:BaseFragment(), BankDetailAdapter.OnClick {


    private val settingsViewModel: SettingsViewModel by viewModels()
    private lateinit var binding: FragmentBankDetailBinding
    val resentTransactionAdapter = BankDetailAdapter(this)
    var position : Int = 0

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentBankDetailBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        bankListApi()
        bankListResponse()
        bankDeleteResponse()
        setAdapter()
        setClick()

    }

    private fun bankListApi() {
        if (hasInternet(requireContext())){
            binding.includeNoInternet.layoutNoInternet.hideView()
            binding.recycleViewBankDetail.showView()
            toggleLoader(true)
            bankListResponse()
            lifecycleScope.launchWhenCreated {
                settingsViewModel.bankAccountsList(
                    session.token,
                )
            }
        }else{
            binding.includeNoInternet.layoutNoInternet.showView()
            binding.recycleViewBankDetail.hideView()
        }
    }

    private fun setClick() {
        binding.includeNoInternet.buttonTryAgain.setOnClickListener {
            bankListApi()
        }
        binding.includeToolbar.imageViewBack.setOnClickListener {
            requireActivity().finish()
        }
        binding.buttonBankDetail.setOnClickListener {
         isolatedListener?.replaceFragment(BankDetailScreen.ADD_BANK_DETAIL)
        }
    }

    private fun setAdapter() {
        Log.e("TAG", "setAdapter: ", )
        binding.recycleViewBankDetail.adapter = resentTransactionAdapter

    }

    override fun onDeleteClick(pos:Int,id:Int) {
        position = pos
        Log.e("TAG", "onDeleteClick: $position", )
        DialogUtils().showGeneralDialog(
            requireContext(),
            message = getString(R.string.message_delete),
            positiveText = getString(R.string.button_yes),
            negativeText = getString(R.string.button_no),
            onClick = {
                lifecycleScope.launchWhenCreated {
                    settingsViewModel.deleteBankAccount(
                        session.token,id.toString()
                    )
                }
            }, onNoClick = {
                 })
    }
    private fun bankListResponse(){
        lifecycleScope.launchWhenCreated {
            settingsViewModel.bankAccountList.collect { result ->
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
                        result.data.let {
                            Log.e("TAG", "bankListResponse: $it", )
                            resentTransactionAdapter.setListItem(it!!.list)
                            resentTransactionAdapter.notifyDataSetChanged()
                            if (it.list.isEmpty()){
                                binding.noDataFoundLayout.textViewNoBankAccountAdded.showView()
                            }else{
                                binding.noDataFoundLayout.textViewNoBankAccountAdded.hideView()
                            }
                        }
                    }
                }
            }
        }
    }
    private fun bankDeleteResponse(){
        lifecycleScope.launchWhenCreated {
            settingsViewModel.deleteBankAccount.collect { result ->
                when (result) {
                    is Resource.Error -> {
                        toggleLoader(false)
                        result.message?.let {
                            showMessage(binding.root,it)
                        }
                    }
                    is Resource.Loading -> {}
                    is Resource.Success -> {
//                        toggleLoader(false)
                        result.data.let {
                             resentTransactionAdapter.deleteItem(position)
                             showMessage(binding.root,it!!.message)
                             bankListApi()
//                            if (resentTransactionAdapter.itemCount == 0){
//                                binding.textViewNoBankAccountAdded.showView()
//                            }
                        }
                    }
                }
            }
        }
    }
}
