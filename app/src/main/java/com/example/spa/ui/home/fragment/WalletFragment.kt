package com.example.spa.ui.home.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.spa.R
import com.example.spa.base.BaseFragment
import com.example.spa.base.listener.BankDetailScreen
import com.example.spa.base.listener.IsolatedListener
import com.example.spa.base.listener.Screen
import com.example.spa.databinding.FragmentWalletBinding
import com.example.spa.ui.home.bottomSheet.AddAmountBottomSheet

class WalletFragment:BaseFragment() {

    private lateinit var binding: FragmentWalletBinding
    lateinit var bottomSheet: AddAmountBottomSheet

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentWalletBinding.inflate(layoutInflater)
//        try {
//            binding.textViewAmount.text = session.user!!.user_wallet.balance.toString() + " AED"
//        }catch (e:Exception){}
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

}