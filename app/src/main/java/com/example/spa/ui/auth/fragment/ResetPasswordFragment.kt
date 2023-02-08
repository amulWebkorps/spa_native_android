package com.example.spa.ui.auth.fragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.example.spa.R
import com.example.spa.base.BaseFragment
import com.example.spa.data.request.User
import com.example.spa.databinding.FragmentResetPasswordBinding
import com.example.spa.ui.auth.activity.AuthActivity
import com.example.spa.utilities.*
import com.example.spa.utilities.validation.ApplicationException
import com.example.spa.viewmodel.AuthViewModel
//import kotlinx.android.synthetic.main.layout_phone_number.view.*

class ResetPasswordFragment :BaseFragment() {
    private lateinit var binding: FragmentResetPasswordBinding

    private val authViewModel: AuthViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentResetPasswordBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setClick()
        resetPasswordResponse()
        binding.textInputConfirmPassword.showPassword(binding.checkboxPassword.isChecked)
        binding.textInputPassword.showPassword(binding.checkboxPassword.isChecked)
    }

    private fun setClick() {
        binding.checkboxPassword.setOnClickListener {
            binding.textInputPassword.showPassword(binding.checkboxPassword.isChecked)
            binding.textInputPassword.setEnd()
        }
        binding.checkboxConfirmPassword.setOnClickListener {
            binding.textInputConfirmPassword.showPassword(binding.checkboxConfirmPassword.isChecked)
            binding.textInputConfirmPassword.setEnd()
        }
        binding.includeToolbar.imageViewBack.setOnClickListener {
            listener?.goBack()
        }
        binding.buttonContinue.setOnClickListener {
            if (isValidationSuccess()){
              if (hasInternet(requireContext())){
                lifecycleScope.launchWhenCreated {
                    authViewModel.resetPassword(session.token,
                        User(
                            country_code= session.countryCode,
                            mobile_number= session.phoneNumber,
                            password= binding.textInputPassword.text.toString(),
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
            validator.submit(binding.textInputPassword)
                .checkEmpty().errorMessage(getString(R.string.error_password))
                .checkMinDigits(8).errorMessage(getString(R.string.error_min_password))
                .check()
            validator.submit(binding.textInputConfirmPassword)
                .matchString(binding.textInputPassword.text.toString()).errorMessage(getString(R.string.error_password_not_matched))
                .check()

        } catch (e: ApplicationException) {
            showMessage(binding.root,e.message)
            return false
        }
        return true
    }


    private fun resetPasswordResponse() {
        lifecycleScope.launchWhenCreated {
            authViewModel.resetPassword.collect { result ->
                when (result) {
                    is Resource.Error -> {
                        toggleLoader(false)
                        result.message?.let { showMessage(binding.root, it) }
                    }
                    is Resource.Loading -> {}
                    is Resource.Success -> {
                        toggleLoader(false)
                        result.data?.let { it ->

                            val intent= Intent(requireContext(), AuthActivity::class.java)
                            requireActivity().finish()
                            intent.putExtra(Constants.SCREEN_NAME,Constants.RESET_PASSWORD)
                            startActivity(intent)
                        }
                    }
                }
            }
        }
    }
}