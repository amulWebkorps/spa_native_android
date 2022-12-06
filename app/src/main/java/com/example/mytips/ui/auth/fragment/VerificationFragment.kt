package com.example.mytips.ui.auth.fragment

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
import com.example.mytips.base.listener.Screen
import com.example.mytips.data.request.RegisterRequest
import com.example.mytips.data.request.User
import com.example.mytips.databinding.FragmentVerificationBinding
import com.example.mytips.ui.auth.activity.AuthActivity
import com.example.mytips.ui.home.activitiy.HomeActivity
import com.example.mytips.utilities.Constants
import com.example.mytips.utilities.Resource
import com.example.mytips.utilities.setSpan
import com.example.mytips.utilities.showMessage
import com.example.mytips.utilities.validation.ApplicationException
import com.example.mytips.viewmodel.AuthViewModel
import dagger.hilt.android.AndroidEntryPoint

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
        binding.textViewPhoneVerificationNumber.text = session.countryCode+"-"+session.phoneNumber + " Change"
        setClick()
        response()
        responseSendOtp()

//        lifecycleScope.launchWhenCreated {
//            authViewModel.sendOtp(
//                User(
//                    country_code = session.countryCode,
//                    mobile_number = session.phoneNumber
//                )
//            )
//        }

    }

    private fun setClick() {
        binding.includeToolbar.imageViewBack.setOnClickListener {
            listener?.goBack()
        }
        binding.textViewPhoneVerificationNumber.setSpan(
            "Change",
            R.font.poppins_medium,
            R.color.black,
            true
        ) {
            listener?.replaceFragment(Screen.FORGOT_PASSWORD,Constants.CHANGE)
        }
        binding.textViewDontReceive.setSpan("Resend", R.font.poppins_medium, R.color.colorBlue72) {
        }
        binding.buttonSubmit.setOnClickListener {
            if (isValidationSuccess()) {


                if (value == Constants.FORGOT_PASSWORD) {
                    listener?.replaceFragment(Screen.RESET_PASSWORD,Constants.FORGOT_PASSWORD)
                }else{
                    session.isLogin = true
                    val intent= Intent(requireContext(), HomeActivity::class.java)
                    requireActivity().finish()
                    startActivity(intent)

//                    val phone = value.split("-")
//                    lifecycleScope.launchWhenCreated {
//                        authViewModel.verifyOtp(
//                            User(
//                                otp_code = binding.otpView.toString(),
//                                mobile_number = phone[1]
//                            )
//                        )
//                    }

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


    private fun responseSendOtp(){
        lifecycleScope.launchWhenCreated {
            authViewModel.sendOtp.collect { result ->
                when (result) {
                    is Resource.Error -> {
                        Log.e("TAG", "fg: $result", )
                        result.message?.let {
                            Log.e("TAG", "response: $it", )
                            showMessage(binding.root,it)
                        }
                    }
                    is Resource.Loading -> {}
                    is Resource.Success -> {
                        showMessage(binding.root , getString(R.string.otp_send))
                    }
                }
            }
        }
    }


    private fun response(){
        lifecycleScope.launchWhenCreated {
            authViewModel.verifyOtp.collect { result ->
                when (result) {
                    is Resource.Error -> {
                        Log.e("TAG", "fg: $result", )
                        result.message?.let {
                            Log.e("TAG", "response: $it", )
                            showMessage(binding.root,it)
                        }
                    }
                    is Resource.Loading -> {}
                    is Resource.Success -> {
                        result.data?.let { it ->
                            session.token = it.token
                            Log.e("TAG", "response: $it",)
                            session.isLogin = true
                            val intent= Intent(requireContext(), HomeActivity::class.java)
                            requireActivity().finish()
                            startActivity(intent)
                        }
                    }
                }
            }
        }
    }

}