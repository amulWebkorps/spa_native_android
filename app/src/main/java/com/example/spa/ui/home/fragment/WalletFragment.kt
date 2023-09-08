package com.example.spa.ui.home.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.example.spa.App
import com.example.spa.R
import com.example.spa.base.BaseFragment
import com.example.spa.base.DialogUtils
import com.example.spa.base.listener.BankDetailScreen
import com.example.spa.data.request.WithdrawlRequest
import com.example.spa.databinding.FragmentWalletBinding
import com.example.spa.ui.home.bottomSheet.AddAmountBottomSheet
import com.example.spa.utilities.Resource
import com.example.spa.utilities.hasInternet
import com.example.spa.utilities.showMessage
import com.example.spa.viewmodel.AuthViewModel
import com.example.spa.viewmodel.SettingsViewModel

class WalletFragment:BaseFragment() {

    private val settingsViewModel: SettingsViewModel by viewModels()
    private lateinit var binding: FragmentWalletBinding
    lateinit var bottomSheet: AddAmountBottomSheet
    private val authViewModel: AuthViewModel by viewModels()
     var isBankAdded = false

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentWalletBinding.inflate(layoutInflater)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        getWallet()
        bankListApi()
        bankListResponse()
        bottomSheet= AddAmountBottomSheet {i,s->
            handleClick(i,s)
        }
        binding.buttonWithdrawal.setOnClickListener {
            if (isBankAdded) {
                fragmentManager?.let { it1 -> bottomSheet.show(it1, "TAG") }
            }else{
                DialogUtils().showGeneralDialog(
                    requireContext(),
                    message = getString(R.string.message_withdraw_popup),
                    positiveText = getString(R.string.button_ok),
                    negativeText = "" ,
                    onClick = {},
                    onNoClick = {

                    })
            }
        }
        binding.includeToolbar.imageViewBack.setOnClickListener {
            requireActivity().finish()
        }
//        getUserResponse()
//        callMe()
    }
    private fun bankListApi() {
        if (hasInternet(requireContext())){
            toggleLoader(true)
            bankListResponse()
            lifecycleScope.launchWhenCreated {
                settingsViewModel.bankAccountsList(
                    session.token,
                )
            }
        }else{
            showMessage(binding.root,getString(R.string.no_internet_connection))
        }
    }
    private fun handleClick(it: Int,value:String) {
      when(it){
          R.id.buttonWithdrawal->{
//              isolatedListener?.replaceFragment(BankDetailScreen.PAYMENT_SUCCESS)
//              bottomSheet.dismiss()
              getWithdraw(value)
          }
          R.id.imageViewBack->{
              bottomSheet.dismiss()
              Log.e("error","image ViewBack")
          }
      }
    }
    private fun callMe() {
        toggleLoader(true)
        lifecycleScope.launchWhenCreated {
            authViewModel.getUser(
                session.token
            )
        }
    }
    private fun getUserResponse() {
        Log.e("payment_link", "result")
        lifecycleScope.launchWhenCreated {
            authViewModel.getUser.collect { result ->
                when (result) {
                    is Resource.Error -> {
                        toggleLoader(false)
                        result.message?.let { showMessage(binding.root, it) }
                    }
                    is Resource.Loading -> {}
                    is Resource.Success -> {
                        toggleLoader(false)
                        result.data?.let { it ->
                            try {
                                (requireActivity().application as App).session.user =it
//                                binding.textViewAmount.text = it!!.user_wallet.balance.toString() + " AED"
                            }catch (e:Exception){}
                        }
                    }
                }
            }
        }
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
                          if (it?.list?.isNotEmpty() == true){
                               isBankAdded = true
                          }
                        }
                    }
                }
            }
        }
    }
    private fun getWithdraw(value: String) {
        toggleLoader(true)
        if (hasInternet(requireContext())){
            getWithDrawResponse()
            lifecycleScope.launchWhenCreated {
                settingsViewModel.getWithdrawRequest(
                    session.token, WithdrawlRequest(value)
                )
            }
        }else{
            showMessage(
                binding.root,
                getString(R.string.no_internet_connection)
            )
        }
    }
    private fun getWithDrawResponse() {
        lifecycleScope.launchWhenCreated {
            settingsViewModel.getWithdraw.collect { result ->
                when (result) {
                    is Resource.Error -> {
                        toggleLoader(false)
                        result.message?.let { showMessage(binding.root, it) }
                    }
                    is Resource.Loading -> {}
                    is Resource.Success -> {
                        result.data.let {
                            Log.e("TAG", it.toString(),)
                        }
                        toggleLoader(false)
                        isolatedListener?.replaceFragment(BankDetailScreen.PAYMENT_SUCCESS)
                        bottomSheet.dismiss()
                    }
                }
            }
        }
    }

    private fun getWallet() {
        toggleLoader(true)
        if (hasInternet(requireContext())){
            getWalletResponse()
            lifecycleScope.launchWhenCreated {
                settingsViewModel.wallet(
                    session.token
                )
            }
        }else{
            showMessage(
                binding.root,
                getString(R.string.no_internet_connection)
            )
        }
    }
    private fun getWalletResponse() {
        lifecycleScope.launchWhenCreated {
            settingsViewModel.getWallet.collect { result ->
                when (result) {
                    is Resource.Error -> {
                        toggleLoader(false)
//                         result.message?.let { showMessage(binding.root, it) }
                    }
                    is Resource.Loading -> {}
                    is Resource.Success -> {
                        result.data.let {
                           binding.textViewAmount.text = it?.wallet + " AED"
                        }
                        toggleLoader(false)

                    }
                }
            }
        }
    }
}