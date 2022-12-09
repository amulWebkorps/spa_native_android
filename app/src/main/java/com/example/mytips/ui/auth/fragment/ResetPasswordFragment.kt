package com.example.mytips.ui.auth.fragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.mytips.R
import com.example.mytips.base.BaseFragment
import com.example.mytips.databinding.FragmentResetPasswordBinding
import com.example.mytips.ui.auth.activity.AuthActivity
import com.example.mytips.ui.home.activitiy.HomeActivity
import com.example.mytips.utilities.setEnd
import com.example.mytips.utilities.showMessage
import com.example.mytips.utilities.showPassword
import com.example.mytips.utilities.validation.ApplicationException

class ResetPasswordFragment :BaseFragment() {
    private lateinit var binding: FragmentResetPasswordBinding

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
        binding.textInputConfirmPassword.showPassword(binding.checkboxPassword.isChecked)
        binding.textInputPassword.showPassword(binding.checkboxPassword.isChecked)
    }

    private fun setClick() {
        binding.checkboxPassword.setOnClickListener {
            binding.textInputPassword.showPassword(binding.checkboxPassword.isChecked)
            binding.textInputPassword.setEnd()
        }
        binding.checkboxConfirmPassword.setOnClickListener {
            binding.textInputConfirmPassword.showPassword(binding.checkboxPassword.isChecked)
            binding.textInputConfirmPassword.setEnd()
        }
        binding.includeToolbar.imageViewBack.setOnClickListener {
            listener?.goBack()
        }
        binding.buttonContinue.setOnClickListener {
            if (isValidationSuccess()){
                val intent= Intent(requireContext(), AuthActivity::class.java)
                requireActivity().finish()
                startActivity(intent)
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
}