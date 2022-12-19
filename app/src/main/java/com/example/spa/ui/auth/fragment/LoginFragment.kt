package com.example.spa.ui.auth.fragment

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.example.spa.R
import com.example.spa.base.BaseFragment
import com.example.spa.base.listener.Screen
import com.example.spa.data.request.User
import com.example.spa.databinding.FragmentLoginBinding
import com.example.spa.ui.home.activitiy.HomeActivity
import com.example.spa.utilities.*
import com.example.spa.utilities.validation.ApplicationException
import com.example.spa.viewmodel.AuthViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.layout_phone_number.view.*

@AndroidEntryPoint
class LoginFragment :  BaseFragment() {


    private lateinit var binding: FragmentLoginBinding

    private val authViewModel: AuthViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentLoginBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setClick()
        loginResponse()
        getUserResponse()
        responseSendOtp()

        binding.textInputPassword.showPassword(binding.checkboxPassword.isChecked)
          }


    private fun setClick() {
   binding.viewForgotPassword.setOnClickListener {
       listener?.replaceFragment(Screen.FORGOT_PASSWORD,"login")
   }
        binding.textViewCreateAccount.setSpan("Register",R.font.poppins_medium,R.color.colorBlue72){
            listener?.replaceFragment(Screen.SIGN_UP)
        }
        binding.checkboxPassword.setOnClickListener {
            binding.textInputPassword.showPassword(binding.checkboxPassword.isChecked)
            binding.textInputPassword.setEnd()
        }
        binding.imageViewBack.setOnClickListener {
            requireActivity().onBackPressed();
        }
        binding.buttonLogin.setOnClickListener {
            if (isValidationSuccess()) {
          toggleLoader(true)

                lifecycleScope.launchWhenCreated {
                    authViewModel.loginUser(
                        User(
                            country_code= "+"+binding.layoutPhone.ccp.selectedCountryCode,
                            mobile_number= binding.layoutPhone.editTextPhoneNumber.text.toString(),
                            password= binding.textInputPassword.text.toString(),
                        )
                    )
                }
            }
        }
    }

    private fun isValidationSuccess(): Boolean {
        try {
            validator.submit(binding.Phone.editTextPhoneNumber)
                .checkEmpty().errorMessage(getString(R.string.error_phone_number))
                .checkMinDigits(10).errorMessage(getString(R.string.error_valid_phone))
                .check()
            validator.submit(binding.textInputPassword)
                .checkEmpty().errorMessage(getString(R.string.error_password))
                .check()

        } catch (e: ApplicationException) {
            showMessage(binding.root,e.message)
            return false
        }
        return true
    }

 private fun loginResponse() {
     lifecycleScope.launchWhenCreated {
         authViewModel.loginUser.collect { result ->
             when (result) {
                 is Resource.Error -> {
                     toggleLoader(false)
                     result.message?.let { showMessage(binding.root, it) }
                 }
                 is Resource.Loading -> {}
                 is Resource.Success -> {
                     toggleLoader(false)
                     result.data?.let { it ->
                         session.token = it.token
                         Log.e("TAG", "loginResponse: $it", )
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
                            session.user=it

                            Log.e("TAG", "getUserResponse: $it", )
                            if (it.is_mobile_verified) {
                                session.isLogin = true
                                val intent = Intent(requireContext(), HomeActivity::class.java)
                                requireActivity().finish()
                                startActivity(intent)
                            }else{
                                lifecycleScope.launchWhenCreated {
                                    authViewModel.sendOtp(
                                        User(
                                            country_code= it.country_code,
                                            mobile_number=it.mobile_number,
                                        )
                                    )
                                }
                            }
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
                        listener?.replaceFragment(Screen.VERIFICATION)
                    }
                }
            }
        }
    }
}