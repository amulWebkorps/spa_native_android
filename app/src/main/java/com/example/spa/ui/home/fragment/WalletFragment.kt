package com.example.spa.ui.home.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.spa.base.BaseFragment
import com.example.spa.databinding.FragmentWalletBinding
import com.example.spa.ui.home.bottomSheet.AddAmountBottomSheet

class WalletFragment:BaseFragment() {

    private lateinit var binding: FragmentWalletBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentWalletBinding.inflate(layoutInflater)
//        Log.e("TAG", "onCreateView: ${session.user!!.user_wallet.balance}", )
        binding.textViewAmount.text = session.user!!.user_wallet.balance.toString()+" AED"
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.buttonWithdrawal.setOnClickListener {
            AddAmountBottomSheet.showDialog(childFragmentManager)
        }
        binding.includeToolbar.imageViewBack.setOnClickListener {
            requireActivity().finish()
        }
    }

}