package com.example.spa.ui.auth.fragment


import android.app.DatePickerDialog
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.core.content.ContextCompat.getSystemService
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.example.spa.App
import com.example.spa.R
import com.example.spa.Url
import com.example.spa.base.BaseFragment
import com.example.spa.base.listener.Screen
import com.example.spa.data.remote.AuthApi
import com.example.spa.data.request.User
import com.example.spa.data.response.GetUser
import com.example.spa.databinding.FragmentSignUpBinding
import com.example.spa.utilities.*
import com.example.spa.utilities.validation.ApplicationException
import com.example.spa.viewmodel.AuthViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.File
import java.io.FileOutputStream
import java.text.SimpleDateFormat
import java.util.*


class SignUpFragment :  BaseFragment()  {
    private lateinit var binding: FragmentSignUpBinding
    private val authViewModel: AuthViewModel by viewModels()
     var imageUri :Uri? =  null
     var part :MultipartBody.Part? =  null
    var year: Int? = null
    var month: Int? = null
    var date: Int? = null

    var calendar = Calendar.getInstance()

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
       // response()
        responseSendOtp()

        binding.textInputConfirmPassword.showPassword(binding.checkboxPassword.isChecked)
        binding.textInputPassword.showPassword(binding.checkboxPassword.isChecked)
    }
    private fun updateDateInView() {
        val myFormat = "MM/dd/yyyy" // mention the format you need
        val sdf = SimpleDateFormat(myFormat, Locale.US)
        binding.textInputDOB!!.setText(sdf.format(calendar.time))
    }

    private fun setClick() {
        val dateSetListener =
            DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
                calendar.set(Calendar.YEAR, year)
                calendar.set(Calendar.MONTH, monthOfYear)
                calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)
                updateDateInView()
            }

        binding.textInputDOB.setOnClickListener {
            DatePickerDialog(requireContext(),
                dateSetListener,
                // set DatePickerDialog to point to today's date when it loads up
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)).show()
    }
        binding.imageViewProfile.setOnClickListener {
            imagePicker { uri ->
                imageUri = uri
                binding?.imageViewProfile?.setImageURI(uri)
                binding.imageViewAddPic.visibility = View.INVISIBLE
            }
        }
        binding.textViewAlreadyAccount.setSpan(getString(R.string.sign_in) ,R.font.poppins_medium, R.color.colorBlue72){
            listener?.goBack()
        }

        binding.textViewTermsAndConditions.setSpan(getString(R.string.text_terms) ,R.font.poppins_medium, R.color.colorBlue72){
            val url = "http://www.google.com"
            startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(url)))
        }
        binding.includeToolbar.imageViewBack.setOnClickListener {
            listener?.goBack()
        }
        binding.checkboxPassword.setOnClickListener {
            binding.textInputPassword.showPassword(binding.checkboxPassword.isChecked)
            binding.textInputPassword.setEnd()
        }
        binding.checkboxConfirmPassword.setOnClickListener {
            binding.textInputConfirmPassword.showPassword(binding.checkboxConfirmPassword.isChecked)
            binding.textInputConfirmPassword.setEnd()
        }

        binding.buttonRegister.setOnClickListener {
               if (isValidationSuccess()) {
                    if (hasInternet(requireContext())) {
                    session.countryCode = "+" + binding.layoutPhone.ccp.selectedCountryCode
                    session.phoneNumber = binding.layoutPhone.editTextPhoneNumber.text.toString()
                    toggleLoader(true)
                    CoroutineScope(Dispatchers.IO).launch {
                        upload()
                    }
                }else{
                    showMessage(binding.root,getString(R.string.no_internet_connection))
                }
            }
        }

        binding.layoutEmail.textInputEmailAddress.addTextChangedListener (object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                if (isValidEmail()){
                    binding.layoutEmail.imageEmailVerify.visibility=View.VISIBLE
                }else{
                    binding.layoutEmail.imageEmailVerify.visibility=View.INVISIBLE
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
                            (requireActivity().application as App).session.user=it
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
            validator.submit(binding.layoutEmail.textInputEmailAddress)
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
                    .checkMinDigits(8).errorMessage(getString(R.string.error_valid_phone))
                    .check()
                validator.submit(binding.layoutEmail.textInputEmailAddress)
                    .checkEmpty().errorMessage(getString(R.string.error_enter_email))
                    .checkValidEmail().errorMessage(getString(R.string.error_valid_message))
                    .check()

                validator.submit(binding.textInputPassword)
                    .checkEmpty().errorMessage(getString(R.string.error_password))
                    .checkMinDigits(8).errorMessage(getString(R.string.error_min_password))
                    .check()
                validator.submit(binding.textInputConfirmPassword)
                    .checkEmpty().errorMessage(getString(R.string.empty_confirm_password))
                    .matchString(binding.textInputPassword.text.toString())
                    .errorMessage(getString(R.string.error_password_not_matched))
                    .check()
            if (!binding.checkBoxTermsAndConditions.isChecked) {
                throw ApplicationException(getString(R.string.error_terms_condition))
            }
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

                        showMessage(binding.root , getString(R.string.otp_send))
                        listener?.replaceFragment(Screen.VERIFICATION)
                    }
                }
            }
        }
    }


    private suspend fun upload(){
        val fileDir = requireContext().filesDir
       val file = File(fileDir,"image.png")


       if (imageUri != null) {
           try {
               Log.e("TAG", "upload: 1")
               val inputStream = requireContext().contentResolver.openInputStream(imageUri!!)
               val outputStream = FileOutputStream(file)
               inputStream!!.copyTo(outputStream)
               val requestFile: RequestBody = RequestBody.create(
                   "multipart/form-data".toMediaTypeOrNull(), file
               )
               part = MultipartBody.Part.createFormData(
                   "image", file.name.trim(), requestFile
               )
           }catch (e:Exception){}
        }else {
           Log.e("TAG", "upload: 2")

           part =  null
        }

        val retrofit = Retrofit.Builder()
            .baseUrl(Url.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(AuthApi::class.java)

        val email: RequestBody = RequestBody.create(
            "text/plain".toMediaTypeOrNull(),
            binding.layoutEmail.textInputEmailAddress.text.toString()
        )
        val firstName: RequestBody = RequestBody.create(
            "text/plain".toMediaTypeOrNull(),
            binding.textInputFirstName.text.toString()
        )
        val lastName: RequestBody = RequestBody.create(
            "text/plain".toMediaTypeOrNull(),
            binding.textInputLastName.text.toString()
        )
        val mobileNumber: RequestBody = RequestBody.create(
            "text/plain".toMediaTypeOrNull(),
            binding.layoutPhone.editTextPhoneNumber.text.toString()
        )
        val countryCode: RequestBody = RequestBody.create(
            "text/plain".toMediaTypeOrNull(),
            "+"+binding.layoutPhone.ccp.selectedCountryCode
        )
        val password: RequestBody = RequestBody.create(
            "text/plain".toMediaTypeOrNull(),
            binding.textInputPassword.text.toString()
        )
        val confirmPassword: RequestBody = RequestBody.create(
            "text/plain".toMediaTypeOrNull(),
            binding.textInputConfirmPassword.text.toString()
        )

        try {
        val call: retrofit2.Response<GetUser> =  retrofit.registerUserImage(
            email, firstName,lastName,mobileNumber,countryCode,password,confirmPassword,
            part
        )

        if(call.isSuccessful){
            toggleLoader(false)
            call.body()?.let { it ->
                session.countryCode = it.country_code
                session.phoneNumber = it.mobile_number
//                (requireActivity().application as App).session.user = it
                lifecycleScope.launchWhenCreated {
                    authViewModel.sendOtp(
                        User(
                            country_code =  it.country_code,
                            mobile_number = it.mobile_number,
                        )
                    )
                }
                Log.e("TAG", "upload: ${it}")
            }
        }else{
            toggleLoader(false)
            showMessage(binding.root,getErrorResponseArray(call.errorBody()).errors[0].toString()!!)
        }
        }catch (e:Exception){
            toggleLoader(false)
            showMessage(binding.root,getString(R.string.no_internet_connection))
        }
    }
}