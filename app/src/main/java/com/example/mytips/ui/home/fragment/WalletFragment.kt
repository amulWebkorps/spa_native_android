package com.example.mytips.ui.home.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.mytips.base.BaseFragment
import com.example.mytips.databinding.FragmentWalletBinding
import com.example.mytips.ui.home.bottomSheet.AddAmountBottomSheet
import com.example.mytips.utilities.showToast

class WalletFragment:BaseFragment() {

    private lateinit var binding: FragmentWalletBinding

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
        binding.buttonWithdrawal.setOnClickListener {
            AddAmountBottomSheet.showDialog(childFragmentManager)
        }
        binding.includeToolbar.imageViewBack.setOnClickListener {
            requireActivity().finish()
        }
    }

}