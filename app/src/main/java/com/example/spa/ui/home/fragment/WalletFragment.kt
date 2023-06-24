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
import com.example.spa.base.listener.BankDetailScreen
import com.example.spa.base.listener.IsolatedListener
import com.example.spa.base.listener.Screen
import com.example.spa.databinding.FragmentWalletBinding
import com.example.spa.ui.home.bottomSheet.AddAmountBottomSheet
import com.example.spa.utilities.Resource
import com.example.spa.utilities.showMessage
import com.example.spa.viewmodel.AuthViewModel

class WalletFragment:BaseFragment() {

    private lateinit var binding: FragmentWalletBinding
    lateinit var bottomSheet: AddAmountBottomSheet
    private val authViewModel: AuthViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentWalletBinding.inflate(layoutInflater)
        try {
            binding.textViewAmount.text = session.user!!.user_wallet.balance.toString() + " AED"
        }catch (e:Exception){}
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        bottomSheet= AddAmountBottomSheet {
            handleClick(it)
        }
        binding.buttonWithdrawal.setOnClickListener {
            fragmentManager?.let { it1 -> bottomSheet.show(it1,"TAG") }
        }
        binding.includeToolbar.imageViewBack.setOnClickListener {
            requireActivity().finish()
        }
        getUserResponse()
        callMe()
    }

    private fun handleClick(it: Int) {
      when(it){
          R.id.buttonWithdrawal->{
              isolatedListener?.replaceFragment(BankDetailScreen.PAYMENT_SUCCESS)
              bottomSheet.dismiss()
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
                                binding.textViewAmount.text = it!!.user_wallet.balance.toString() + " AED"
                            }catch (e:Exception){}
                        }
                    }
                }
            }
        }
    }

}