package com.example.mytips.ui.auth.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.example.mytips.R
import com.example.mytips.base.BaseFragment
import com.example.mytips.base.listener.Screen
import com.example.mytips.data.request.User
import com.example.mytips.databinding.FragmentForgotPasswordBinding
import com.example.mytips.utilities.Constants
import com.example.mytips.utilities.Resource
import com.example.mytips.utilities.showMessage
import com.example.mytips.utilities.validation.ApplicationException
import com.example.mytips.viewmodel.AuthViewModel
import kotlinx.android.synthetic.main.layout_phone_number.view.*


class ForgotPasswordFragment :BaseFragment() {


    private val authViewModel: AuthViewModel by viewModels()
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
        responseSendOtp()

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
                    toggleLoader(true)
                    lifecycleScope.launchWhenCreated {
                        authViewModel.sendOtp(
                            User(
                                country_code= "+"+binding.layoutPhone.ccp.selectedCountryCode,
                                mobile_number= binding.layoutPhone.editTextPhoneNumber.text.toString(),
                            )
                        )
                    }
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

    private fun responseSendOtp(){
        lifecycleScope.launchWhenCreated {
            authViewModel.sendOtp.collect { result ->
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
                        listener?.replaceFragment(Screen.VERIFICATION,Constants.FORGOT_PASSWORD)
                    }
                }
            }
        }
    }


}