package com.example.mytips.ui.home.fragment

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.mytips.R
import com.example.mytips.base.BaseFragment
import com.example.mytips.databinding.FragmentAddBankDetailBinding
import com.example.mytips.utilities.showMessage
import com.example.mytips.utilities.validation.ApplicationException
import kotlinx.android.synthetic.main.fragment_add_bank_detail.*


class AddBankDetailFragment:BaseFragment() {

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
                    isolatedListener?.goBack()
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
                .checkMinDigits(16).errorMessage(getString(R.string.label_error_acc))
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
        } catch (e: ApplicationException) {
            showMessage(binding.root,e.message)
            return false
        }
        return true
    }
}