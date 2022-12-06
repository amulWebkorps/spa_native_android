package com.example.mytips.ui.auth.fragment

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.example.mytips.R
import com.example.mytips.base.BaseFragment
import com.example.mytips.base.listener.Screen
import com.example.mytips.data.request.RegisterRequest
import com.example.mytips.data.request.User
import com.example.mytips.databinding.FragmentLoginBinding
import com.example.mytips.ui.home.activitiy.HomeActivity
import com.example.mytips.utilities.*
import com.example.mytips.utilities.validation.ApplicationException
import com.example.mytips.viewmodel.AuthViewModel
import com.example.mytips.viewmodel.PhotoListViewModel
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
                session.isLogin = true
                val intent= Intent(requireContext(), HomeActivity::class.java)
                requireActivity().finish()
                startActivity(intent)

//                binding.progressBar.visibility=View.VISIBLE
//                lifecycleScope.launchWhenCreated {
//                    authViewModel.loginUser(
//                        User(
//                            mobile_number= binding.layoutPhone.editTextPhoneNumber.text.toString(),
//                            password= binding.textInputPassword.text.toString(),
//                        )
//                    )
//                }

            }
        }
    }

//
//    private fun isValidEmail(): Boolean {
//        try {
//            validator.submit(binding.textInputEmail)
//                .checkEmpty().errorMessage(getString(R.string.error_enter_email))
//                .checkValidEmail().errorMessage(getString(R.string.error_valid_message))
//                .check()
//
//        } catch (e: ApplicationException) {
//            return false
//        }
//        return true
//    }
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
                     binding.progressBar.visibility=View.GONE
                        }
                 is Resource.Loading -> {}
                 is Resource.Success -> {
                     binding.progressBar.visibility=View.GONE
                     result.data?.let { it ->
                         session.token = it.token.toString()
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