package com.example.mytips.ui.home.fragment

import android.content.Intent
import android.os.Bundle
import android.text.InputType
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.example.mytips.R
import com.example.mytips.base.BaseFragment
import com.example.mytips.data.request.User
import com.example.mytips.databinding.FragmentEditProfileBinding
import com.example.mytips.databinding.FragmentLoginBinding
import com.example.mytips.ui.home.activitiy.HomeActivity
import com.example.mytips.utilities.Resource
import com.example.mytips.utilities.showMessage
import com.example.mytips.utilities.showPassword
import com.example.mytips.utilities.validation.ApplicationException
import com.example.mytips.viewmodel.AuthViewModel

class EditProfileFragment : BaseFragment() {

    private lateinit var binding: FragmentEditProfileBinding
    private val authViewModel: AuthViewModel by viewModels()

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
        updateUserResponse()
        clickListener()
        setView()
        Log.e("TAG", "onViewCreated: ${session.user!!.first_name}", )

    }

    private fun setView() {
        binding.layoutPhone.editTextPhoneNumber.inputType = InputType.TYPE_NULL
        binding.layoutPhone.ccp.setCcpClickable(false)
        binding.textInputFirstName.setText(session.user!!.first_name)
        binding.textInputLastName.setText(session.user!!.last_name)
        binding.layoutPhone.editTextPhoneNumber.setText(session.user!!.mobile_number)
        binding.layoutPhone.ccp.setCountryForPhoneCode(session.countryCode.toInt())
    }

    private fun clickListener() {
        binding.buttonSave.setOnClickListener {
            if(isValidationSuccess()){
                toggleLoader(true)
                lifecycleScope.launchWhenCreated {
                    authViewModel.updateUserDetail(
                        session.token,
                        User(
                            email = session.user!!.email,
                            first_name =  binding.textInputFirstName.text.toString(),
                            last_name = binding.textInputLastName.text.toString(),
                        )
                    )
                }
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

        } catch (e: ApplicationException) {
            showMessage(binding.root,e.message)
            return false
        }
        return true
    }

    private fun updateUserResponse(){
        lifecycleScope.launchWhenCreated {
            authViewModel.updateUserDetail.collect { result ->
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
                      result.data.let {
                          Log.e("TAG", "updateUserResponse: $it", )
                          showMessage(binding.root,getString(R.string.user_update_success))
                          session.user=it!!.user
                          requireActivity().finish()
                      }
                    }
                }
            }
        }
    }

}