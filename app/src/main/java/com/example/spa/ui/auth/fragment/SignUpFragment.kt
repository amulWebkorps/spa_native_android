package com.example.spa.ui.auth.fragment


import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.example.spa.R
import com.example.spa.base.BaseFragment
import com.example.spa.base.listener.Screen
import com.example.spa.data.request.User
import com.example.spa.databinding.FragmentSignUpBinding
import com.example.spa.utilities.*
import com.example.spa.utilities.validation.ApplicationException
import com.example.spa.viewmodel.AuthViewModel

class SignUpFragment :  BaseFragment()  {
    private lateinit var binding: FragmentSignUpBinding
    private val authViewModel: AuthViewModel by viewModels()


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSignUpBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setClick()
        response()
        responseSendOtp()
        binding.textInputConfirmPassword.showPassword(binding.checkboxPassword.isChecked)
        binding.textInputPassword.showPassword(binding.checkboxPassword.isChecked)
    }
    private fun setClick() {
        binding.imageViewProfile.setOnClickListener {
            imagePicker { uri ->
                binding?.imageViewProfile?.setImageURI(uri)
                binding.imageViewAddPic.visibility = View.INVISIBLE
            }
        }
        binding.textViewAlreadyAccount.setSpan("Sign In" ,R.font.poppins_medium, R.color.colorBlue72){
            listener?.goBack()
        }
        binding.includeToolbar.imageViewBack.setOnClickListener {
            listener?.goBack()
        }
        binding.checkboxPassword.setOnClickListener {
            binding.textInputPassword.showPassword(binding.checkboxPassword.isChecked)
            binding.textInputPassword.setEnd()
        }
        binding.checkboxConfirmPassword.setOnClickListener {
            binding.textInputConfirmPassword.showPassword(binding.checkboxPassword.isChecked)
            binding.textInputConfirmPassword.setEnd()
        }

        binding.buttonRegister.setOnClickListener {
            session.countryCode = "+"+binding.layoutPhone.ccp.selectedCountryCode
            session.phoneNumber = binding.layoutPhone.editTextPhoneNumber.text.toString()
            if (isValidationSuccess()){
                 toggleLoader(true)
                lifecycleScope.launchWhenCreated {
                    authViewModel.getRegister(User(
                            country_code= "+"+binding.layoutPhone.ccp.selectedCountryCode,
                            email= binding.textInputEmailAddress.text.toString(),
                            first_name= binding.textInputFirstName.text.toString(),
                            last_name =binding.textInputLastName.text.toString(),
                            mobile_number= binding.layoutPhone.editTextPhoneNumber.text.toString(),
                            password= binding.textInputPassword.text.toString(),
                            password_confirmation= binding.textInputConfirmPassword.text.toString(),
                    ))
               }
            }
        }

        binding.textInputEmailAddress.addTextChangedListener (object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                if (isValidEmail()){
                    binding.imageEmailVerify.visibility=View.VISIBLE
                }else{
                    binding.imageEmailVerify.visibility=View.INVISIBLE
                }
            }

            override fun afterTextChanged(p0: Editable?) {

            }

        })
    }

    private fun response(){

        lifecycleScope.launchWhenCreated {
            authViewModel.register.collect { result ->
                when (result) {
                    is Resource.Error -> {
                        toggleLoader(false)
                        result.message?.let {it->
                            showMessage(binding.root,it)
                        }
                    }
                    is Resource.Loading -> {}
                    is Resource.Success -> {
                        toggleLoader(false)
                        result.data?.let { it ->
                            session.countryCode=it.country_code
                            session.phoneNumber=it.mobile_number
                            session.user=it
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

    private fun isValidEmail(): Boolean {
        try {
            validator.submit(binding.textInputEmailAddress)
                .checkEmpty().errorMessage(getString(R.string.error_enter_email))
                .checkValidEmail().errorMessage(getString(R.string.error_valid_message))
                .check()

        } catch (e: ApplicationException) {
            return false
        }
        return true
    }


    private fun isValidationSuccess(): Boolean {
        try {
//            if (binding.imageViewProfile.drawable == null){
//                validator.checkEmpty().errorMessage(getString(R.string.error_profile_photo)).check()
//            }else {
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
                validator.submit(binding.textInputEmailAddress)
                    .checkEmpty().errorMessage(getString(R.string.error_enter_email))
                    .checkValidEmail().errorMessage(getString(R.string.error_valid_message))
                    .check()

                validator.submit(binding.textInputPassword)
                    .checkEmpty().errorMessage(getString(R.string.error_password))
                    .checkMinDigits(8).errorMessage(getString(R.string.error_min_password))
                    .check()
                validator.submit(binding.textInputConfirmPassword)
                    .matchString(binding.textInputPassword.text.toString())
                    .errorMessage(getString(R.string.error_password_not_matched))
                    .check()

//            }
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
                        listener?.replaceFragment(Screen.VERIFICATION)
                    }
                }
            }
        }
    }
}