package com.example.mytips.ui.home.fragment

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.example.mytips.R
import com.example.mytips.base.BaseFragment
import com.example.mytips.base.DialogUtils
import com.example.mytips.base.listener.BankDetailScreen
import com.example.mytips.data.request.User
import com.example.mytips.databinding.FragmentBankDetailBinding
import com.example.mytips.ui.auth.activity.AuthActivity
import com.example.mytips.ui.home.adapter.BankDetailAdapter
import com.example.mytips.ui.home.adapter.ResentAdapter
import com.example.mytips.utilities.Resource
import com.example.mytips.utilities.showMessage
import com.example.mytips.viewmodel.AuthViewModel
import com.example.mytips.viewmodel.SettingsViewModel

class BankDetailsFragment:BaseFragment(), BankDetailAdapter.OnClick {


    private val settingsViewModel: SettingsViewModel by viewModels()
    private lateinit var binding: FragmentBankDetailBinding
    val resentTransactionAdapter = BankDetailAdapter(this)

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

        bankListResponse()
        toggleLoader(true)
        lifecycleScope.launchWhenCreated {
            settingsViewModel.bankAccountsList(
                session.token,
            )
        }
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
        binding.recycleViewBankDetail.adapter = resentTransactionAdapter
    }

    override fun onDeleteClick(pos:Int,id:Int) {
        DialogUtils().showGeneralDialog(
            requireContext(),
            message = getString(R.string.message_delete),
            positiveText = getString(R.string.button_yes),
            negativeText = getString(R.string.button_no),
            onClick = {
                //binding.recycleViewBankDetail.adapter!!.notifyItemRemoved(pos)
            }, onNoClick = {})
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
                            if(it!!.list.isEmpty()){
                                binding.textViewNoBankAccountAdded.visibility=View.VISIBLE
                            }else{
                                binding.textViewNoBankAccountAdded.visibility=View.GONE
                            }
                            resentTransactionAdapter.setListItem(it!!.list)
                            resentTransactionAdapter.notifyDataSetChanged()
                            Log.e("TAG", "updateUserResponse: $it", )
                        }
                    }
                }
            }
        }
    }
}
