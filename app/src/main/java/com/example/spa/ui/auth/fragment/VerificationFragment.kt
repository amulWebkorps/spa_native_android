package com.example.spa.ui.auth.fragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.example.spa.App
import com.example.spa.R
import com.example.spa.base.BaseFragment
import com.example.spa.base.listener.Screen
import com.example.spa.data.request.User
import com.example.spa.databinding.FragmentVerificationBinding
import com.example.spa.ui.home.activity.HomeActivity
import com.example.spa.utilities.*
import com.example.spa.utilities.validation.ApplicationException
import com.example.spa.viewmodel.AuthViewModel

class VerificationFragment : BaseFragment() {
    lateinit var value:String
    private lateinit var binding: FragmentVerificationBinding
    private val authViewModel: AuthViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentVerificationBinding.inflate(layoutInflater)
        value = requireArguments().getString(Constants.AUTH)!!
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val value = requireArguments().getString(Constants.AUTH)

        binding.textViewPhoneVerificationNumber.text = "("+session.countryCode+")"+"-"+session.phoneNumber + " "+getString(
                    R.string.change)
        setClick()
        response()
        getUserResponse()
        responseSendOtp()

    }

    private fun setClick() {
        binding.includeToolbar.imageViewBack.setOnClickListener {
            listener?.goBack()
        }
        binding.textViewPhoneVerificationNumber.setSpan(
            getString(
                R.string.change),
            R.font.poppins_medium,
            R.color.black,
            true
        ) {
            if(value==Constants.FORGOT_PASSWORD){
                listener?.goBack()
            }else {
                listener?.replaceFragment(Screen.FORGOT_PASSWORD, Constants.CHANGE)
            }
        }
        binding.textViewDontReceive.setSpan(getString(R.string.resend) ,  R.font.poppins_medium, R.color.colorBlue72) {
            if (hasInternet(requireContext())) {
                toggleLoader(true)
                lifecycleScope.launchWhenCreated {
                    authViewModel.sendOtp(
                        User(
                            country_code = session.countryCode,
                            mobile_number = session.phoneNumber,
                        )
                    )
                }
            }else{
                showMessage(binding.root,getString(R.string.no_internet_connection))
            }
        }
        binding.buttonSubmit.setOnClickListener {
            if (isValidationSuccess()) {
                if (hasInternet(requireContext())){
                    toggleLoader(true)
                    lifecycleScope.launchWhenCreated {
                        authViewModel.verifyOtp(
                            User(
                                country_code= session.countryCode,
                                mobile_number= session.phoneNumber,
                                otp_code = binding.otpView.text.toString()
                            )
                        )
                }
                }else{
                    showMessage(binding.root,getString(R.string.no_internet_connection))
                }
            }
        }
    }
    private fun isValidationSuccess(): Boolean {
        try {
            validator.submit(binding.otpView)
                .checkEmpty().errorMessage(getString(R.string.error_enter_otp))
                .check()

        } catch (e: ApplicationException) {
            showMessage(binding.root,e.message)
            return false
        }
        return true
    }



    private fun response(){
        lifecycleScope.launchWhenCreated {
            authViewModel.verifyOtp.collect { result ->
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
                        result.data?.let { it ->

                            session.token = it.token
                            if (value == Constants.FORGOT_PASSWORD) {
                                listener?.replaceFragment(
                                    Screen.RESET_PASSWORD,
                                    Constants.FORGOT_PASSWORD
                                )
                            }else {
                                //(requireActivity().application as App).session.user = it.user
                                lifecycleScope.launchWhenCreated {
                                    authViewModel.getUser(
                                        it.token
                                    )
                                }

                            }
                        }
                    }
                }
            }
        }
    }



    private fun getUserResponse() {
        lifecycleScope.launchWhenCreated {
            authViewModel.getUser.collect { result ->
                when (result) {
                    is Resource.Error -> {
                        toggleLoader(false)
                        result.message?.let { showMessage(binding.root, it) }
                    }
                    is Resource.Loading -> {}
                    is Resource.Success -> {
                        toggleLoader(false)
                        result.data?.let { it ->
                            session.phoneNumber=it.mobile_number
                            session.countryCode=it.country_code
                            (requireActivity().application as App).session.user = it
                            session.isLogin = true

                            val intent = Intent(requireContext(), HomeActivity::class.java)
                            requireActivity().finish()
                            startActivity(intent)

                        }
                        }
                    }
                }
            }
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
                        showMessage(binding.root , getString(R.string.otp_send))
                    }
                }
            }
        }
    }
}