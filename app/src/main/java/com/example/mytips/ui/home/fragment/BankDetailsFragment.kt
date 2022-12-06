package com.example.mytips.ui.home.fragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.mytips.R
import com.example.mytips.base.BaseFragment
import com.example.mytips.base.DialogUtils
import com.example.mytips.base.listener.BankDetailScreen
import com.example.mytips.databinding.FragmentBankDetailBinding
import com.example.mytips.ui.auth.activity.AuthActivity
import com.example.mytips.ui.home.adapter.BankDetailAdapter
import com.example.mytips.ui.home.adapter.ResentAdapter

class BankDetailsFragment:BaseFragment(), BankDetailAdapter.OnClick {

    private lateinit var binding: FragmentBankDetailBinding

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
        setAdapter()
        setClick()

    }

    private fun setClick() {
        binding.includeToolbar.imageViewBack.setOnClickListener {
            requireActivity().finish()
        }
        binding.buttonBankDetail.setOnClickListener {
         isolatedListener?.replaceFragment(BankDetailScreen.ADD_BANK_DETAIL)
        }
    }

    private fun setAdapter() {
        val resentTransactionAdapter = BankDetailAdapter(this)
        binding.recycleViewBankDetail.adapter = resentTransactionAdapter
    }

    override fun onDeleteClick(pos:Int) {
        DialogUtils().showGeneralDialog(
            requireContext(),
            message = getString(R.string.message_delete),
            positiveText = getString(R.string.button_yes),
            negativeText = getString(R.string.button_no),
            onClick = {
                //binding.recycleViewBankDetail.adapter!!.notifyItemRemoved(pos)
            }, onNoClick = {})
    }
}
