package com.example.spa.ui.home.fragment

import android.content.Intent
import android.os.Bundle
import android.text.InputType
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.example.spa.R
import com.example.spa.base.BaseFragment
import com.example.spa.data.request.User
import com.example.spa.databinding.FragmentEditProfileBinding
import com.example.spa.ui.home.activitiy.HomeActivity
import com.example.spa.utilities.Constants
import com.example.spa.utilities.Resource
import com.example.spa.utilities.showMessage
import com.example.spa.utilities.validation.ApplicationException
import com.example.spa.viewmodel.AuthViewModel

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
//            val intent= Intent(requireContext(), HomeActivity::class.java)
//            intent.putExtra(Constants.SCREEN_NAME,Constants.EDIT_PROFILE)
//            startActivity(intent)
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
                          showMessage(binding.root,getString(R.string.user_update_success))
                          session.user=it!!.user
//                          val intent= Intent(requireContext(), HomeActivity::class.java)
//                          intent.putExtra(Constants.SCREEN_NAME,Constants.EDIT_PROFILE)
//                          startActivity(intent)
                          requireActivity().finish()
                      }
                    }
                }
            }
        }
    }

}
