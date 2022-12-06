package com.example.mytips.ui.auth.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.mytips.R
import com.example.mytips.base.BaseFragment
import com.example.mytips.base.listener.Screen
import com.example.mytips.databinding.FragmentForgotPasswordBinding
import com.example.mytips.utilities.Constants
import com.example.mytips.utilities.showMessage
import com.example.mytips.utilities.validation.ApplicationException


class ForgotPasswordFragment :BaseFragment() {

    private lateinit var binding: FragmentForgotPasswordBinding
    lateinit var value:String
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentForgotPasswordBinding.inflate(layoutInflater)
        value = requireArguments().getString(Constants.AUTH)!!
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setView()
        setClick()

    }

    private fun setClick() {
        binding.buttonContinue.setOnClickListener {
            session.countryCode = "+"+binding.layoutPhone.ccp.selectedCountryCode
            session.phoneNumber = binding.layoutPhone.editTextPhoneNumber.text.toString()

            if (isValidationSuccess()){
                if (value == Constants.CHANGE){
                    listener?.goBack()
                }
                else{
               listener?.replaceFragment(Screen.VERIFICATION,Constants.FORGOT_PASSWORD)
            }
          }
        }
        binding.includeToolbar.imageViewBack.setOnClickListener {
            listener?.goBack()
        }
    }

    private fun setView() {

        if (value == Constants.CHANGE){
            binding.textViewForgotPassword.text = getString(R.string.label_change_phone_number)
            binding.textViewForgotPasswordSubHead.text = getString(R.string.label_mobile_number_subhead)
        }
    }


    private fun isValidationSuccess(): Boolean {
        try {
            validator.submit(binding.layoutPhone.editTextPhoneNumber)
                .checkEmpty().errorMessage(getString(R.string.error_phone_number))
                .checkMinDigits(10).errorMessage(getString(R.string.error_valid_phone))
                .check()
        } catch (e: ApplicationException) {
            showMessage(binding.root,e.message)
            return false
        }
        return true
    }

}