package com.example.spa.ui.home.fragment

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.example.spa.R
import com.example.spa.base.BaseFragment
import com.example.spa.data.request.AddBankDetailRequest
import com.example.spa.databinding.FragmentAddBankDetailBinding
import com.example.spa.utilities.Resource
import com.example.spa.utilities.showMessage
import com.example.spa.utilities.validation.ApplicationException
import com.example.spa.viewmodel.SettingsViewModel
import kotlinx.android.synthetic.main.fragment_add_bank_detail.*


class AddBankDetailFragment:BaseFragment() {

    private val settingsViewModel: SettingsViewModel by viewModels()

    private lateinit var binding: FragmentAddBankDetailBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentAddBankDetailBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setClick()
        responseAddBankDetail()
        val items = listOf("Saving", "Current")
        val adapter = ArrayAdapter(requireContext(), R.layout.layout_item, items)
        (binding.textInputAccountType as? AutoCompleteTextView)?.setAdapter(adapter)
    }

    private fun setClick() {
        binding.apply {
            includeToolbar.imageViewBack.setOnClickListener {
                isolatedListener?.goBack()
            }
            textInputConfirmAccountNumber.addTextChangedListener (object : TextWatcher {
                override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

                }

                override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                    if (isValidAccountNumber()){
                        binding.imageEmailVerify.visibility=View.VISIBLE
                    }else{
                        binding.imageEmailVerify.visibility=View.INVISIBLE
                    }
                }

                override fun afterTextChanged(p0: Editable?) {

                }

            })
            buttonBankDetail.setOnClickListener {
                if (isValidationSuccess()){
                    toggleLoader(true)
                    lifecycleScope.launchWhenCreated {
                        settingsViewModel.addBankDetail(
                            session.token,
                            AddBankDetailRequest(
                            bank_name  = binding.textInputBankName.text.toString(),
                            account_holder_name = binding.textInputNameAccountHolder.text.toString(),
                            account_number  = binding.textInputConfirmAccountNumber.text.toString(),
                            account_type =binding.textInputAccountType.text.toString(),
                            ifsc_code = binding.textInputCode.text.toString(),
                        )
                      )
                    }
                }
            }
        }
    }


    private fun isValidAccountNumber(): Boolean {
        try {
            validator.submit(binding.textInputConfirmAccountNumber)
                .matchString(binding.textInputAccountNumber.text.toString()).errorMessage(getString(
                    R.string.error_account_not_matched))
                .check()
        } catch (e: ApplicationException) {
            return false
        }
        return true
    }


    private fun isValidationSuccess(): Boolean {
        try {
            validator.submit(binding.textInputAccountNumber)
                .checkEmpty().errorMessage(getString(R.string.label_error_contact_number))
                .checkMinDigits(10).errorMessage(getString(R.string.label_error_acc))
                .check()
            validator.submit(binding.textInputConfirmAccountNumber)
                 .matchString(binding.textInputAccountNumber.text.toString()).errorMessage(getString(
                                     R.string.error_account_not_matched))
                .check()
            validator.submit(binding.textInputCode)
                .checkEmpty().errorMessage(getString(R.string.error_enter_code))
                .check()
            validator.submit(binding.textInputNameAccountHolder)
                .checkEmpty().errorMessage(getString(R.string.error_account_holder_name))
                .check()
            validator.submit(binding.textInputBankName)
                .checkEmpty().errorMessage(getString(R.string.error_bank_name))
                .check()
        } catch (e: ApplicationException) {
            showMessage(binding.root,e.message)
            return false
        }
        return true
    }


    private fun responseAddBankDetail(){
        lifecycleScope.launchWhenCreated {
            settingsViewModel.addBankDetails.collect { result ->
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
                            showMessage(binding.root,getString(R.string.bank_detail_added_successfully))
                            Log.e("TAG", "responseAddBankDetail: $it", )
                            isolatedListener?.goBack()
                        }
                      }
                }
            }
        }
    }
}