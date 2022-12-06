package com.example.mytips.ui.home.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.mytips.base.BaseFragment
import com.example.mytips.databinding.TransactionFragmentBinding
import com.example.mytips.ui.home.adapter.ResentAdapter
import com.example.mytips.ui.home.adapter.ResentTransactionAdapter

class TransactionFragment:BaseFragment() {

    private lateinit var binding: TransactionFragmentBinding
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = TransactionFragmentBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setAdapter()
    }

    private fun setAdapter() {
        val resentTransactionAdapter = ResentAdapter()
        binding.recycleViewTransaction.adapter = resentTransactionAdapter
    }
}