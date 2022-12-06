package com.example.mytips.ui.home.fragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.mytips.R
import com.example.mytips.base.BaseFragment
import com.example.mytips.databinding.FragmentEditProfileBinding
import com.example.mytips.databinding.FragmentLoginBinding
import com.example.mytips.ui.home.activitiy.HomeActivity
import com.example.mytips.utilities.showMessage
import com.example.mytips.utilities.showPassword
import com.example.mytips.utilities.validation.ApplicationException

class EditProfileFragment : BaseFragment() {

    private lateinit var binding: FragmentEditProfileBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentEditProfileBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.buttonSave.setOnClickListener {
            if(isValidationSuccess()){
                requireActivity().finish()
            }
        }
        binding.imageViewProfile.setOnClickListener {
            imagePicker { uri ->
                binding?.imageViewProfile?.setImageURI(uri)
                }
        }
        binding.includeToolbar.imageViewBack.setOnClickListener {
            requireActivity().finish()
        }
    }

    private fun isValidationSuccess(): Boolean {
        try {
            validator.submit(binding.textInputFirstName)
                .checkEmpty().errorMessage(getString(R.string.error_firstName))
                .check()
            validator.submit(binding.textInputLastName)
                .checkEmpty().errorMessage(getString(R.string.error_lastName))
                .check()
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