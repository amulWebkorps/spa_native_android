package com.example.spa.ui.home.fragment

import android.app.DatePickerDialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.text.InputType
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.example.spa.App
import com.example.spa.R
import com.example.spa.Url
import com.example.spa.base.BaseFragment
import com.example.spa.data.remote.AuthApi
import com.example.spa.data.request.User
import com.example.spa.data.response.UpdateUserResponse
import com.example.spa.databinding.FragmentEditProfileBinding
import com.example.spa.ui.home.activity.HomeActivity
import com.example.spa.utilities.*
import com.example.spa.utilities.validation.ApplicationException
import com.example.spa.viewmodel.AuthViewModel
import com.squareup.picasso.Picasso
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

class EditProfileFragment : BaseFragment() {

    private lateinit var binding: FragmentEditProfileBinding
    private val authViewModel: AuthViewModel by viewModels()
    var imageUri : Uri? =  null
       var isImage = false

    var calendar = Calendar.getInstance()


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
        clickListener()
        setView()
    }

    private fun setView() {

        if ((requireActivity().application as App).session.user!!.image != null && session.user!!.image.isNotEmpty()) {
            Log.e("TAG", "onCreate: ${session.user!!.image}", )


            Picasso.with(requireContext())
                .load((requireActivity().application as App).session.user!!.image)
                .placeholder(R.drawable.place_holder)
                .into(binding.imageViewProfile);
        }


        binding.layoutPhone.editTextPhoneNumber.inputType = InputType.TYPE_NULL
        binding.layoutPhone.ccp.setCcpClickable(false)
        binding.textInputFirstName.setText(session.user!!.first_name)
        binding.textInputLastName.setText(session.user!!.last_name)
        binding.layoutPhone.editTextPhoneNumber.setText(session.user!!.mobile_number)
        binding.layoutPhone.editTextPhoneNumber.setTextColor(ContextCompat.getColor(requireContext(),R.color.colorGreyB2))
        binding.layoutPhone.ccp.setCountryForPhoneCode(session.countryCode.toInt())
        binding.layoutPhone.ccp.showArrow(false)
        binding.textInputEmailAddress.setText(session.user!!.email)
        binding.textInputEmailAddress.isClickable = false

    }
    private fun updateDateInView() {
        val myFormat = "dd MMM yyyy" // mention the format you need
        val sdf = SimpleDateFormat(myFormat, Locale.US)
        binding.textInputDOB!!.setText(sdf.format(calendar.time))
    }
    private fun clickListener() {
//        calendar.add(Calendar.YEAR, -18);
        val dateSetListener =
            DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
                calendar.set(Calendar.YEAR, year)
                calendar.set(Calendar.MONTH, monthOfYear)
                calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)

                    updateDateInView()
                }



        binding.textInputDOB.setOnClickListener {
            // Create a calendar instance and set it to 18 years ago from the current date
            val calendar = Calendar.getInstance()

            val datePickerDialog = DatePickerDialog(
                requireContext(),
                dateSetListener,
                // Set DatePickerDialog to point to today's date when it loads up
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
            )


            calendar.add(Calendar.YEAR, -18)
            datePickerDialog.getDatePicker().setMaxDate(calendar.getTimeInMillis());

            // Show the DatePickerDialog
            datePickerDialog.show()
        }
        binding.buttonSave.setOnClickListener {
            if(isValidationSuccess()){
                if (hasInternet(requireContext())) {
                    toggleLoader(true)
                    CoroutineScope(Dispatchers.IO).launch {
                        upload()
                    }
                }else{
                    showMessage(binding.root,getString(R.string.no_internet_connection))
                }
//                lifecycleScope.launchWhenCreated {
//                    authViewModel.updateUserDetail(
//                        session.token,
//                        User(
//                            email = session.user!!.email,
//                            first_name =  binding.textInputFirstName.text.toString(),
//                         Alast_name = binding.textInputLastName.text.toString(),
//                        )
//                    )
//                }
            }
        }
        binding.imageViewProfile.setOnClickListener {
            imagePicker { uri ->
                isImage = true
                imageUri = uri
                binding?.imageViewProfile?.setImageURI(uri)
            }
        }
        binding.includeToolbar.imageViewBack.setOnClickListener {
            val intent= Intent(requireContext(), HomeActivity::class.java)
            intent.putExtra(Constants.SCREEN_NAME,Constants.EDIT_PROFILE)
            startActivity(intent)
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
    private suspend fun upload() {
        val fileDir = requireContext().filesDir
        val file = File(fileDir, "image.png")

        Log.e("TAG", "upload: 1",)

        try {
            Log.e("TAG", "upload: 2",)
            val inputStream = requireContext().contentResolver.openInputStream(imageUri!!)
            val outputStream = FileOutputStream(file)
            inputStream!!.copyTo(outputStream)
        } catch (e: Exception) {
        }

        val requestFile: RequestBody = RequestBody.create(
            "multipart/form-data".toMediaTypeOrNull(), file
        )
        val part = MultipartBody.Part.createFormData(
            "image", file.name.trim(), requestFile
        )


        val retrofit = Retrofit.Builder()
            .baseUrl(Url.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(AuthApi::class.java)

        val email: RequestBody = RequestBody.create(
            "text/plain".toMediaTypeOrNull(),
            session.user!!.email
        )
        val firstName: RequestBody = RequestBody.create(
            "text/plain".toMediaTypeOrNull(),
            binding.textInputFirstName.text.toString()
        )
        val lastName: RequestBody = RequestBody.create(
            "text/plain".toMediaTypeOrNull(),
            binding.textInputLastName.text.toString()
        )

        if (isImage) {
            try {
                val call: retrofit2.Response<UpdateUserResponse> = retrofit.updateUserDetailsImage(
                    session.token,
                    email, firstName, lastName, part
                )
                if (call.isSuccessful) {
                    toggleLoader(false)
                    Log.e("TAG", "upload: ${call.body()!!.user}",)
                    (requireActivity().application as App).session.user = call.body()!!.user
                    showMessage(binding.root, getString(R.string.user_update_success))
                } else {
                    toggleLoader(false)
                    showMessage(
                        binding.root,
                        getErrorResponseArray(call.errorBody()).errors[0].toString()!!
                    )
                }
            } catch (e: Exception) {
                toggleLoader(false)

                Log.e("TAG", "upload: ${e}", )
                showMessage(binding.root, getString(R.string.no_internet_connection))
            }
        }
    else{
        try {
            val call: retrofit2.Response<UpdateUserResponse> = retrofit.updateUserDetailsWithoutImage(
                session.token,User(
                    email = session.user!!.email,
                    first_name = binding.textInputFirstName.text.toString(),
                    last_name = binding.textInputLastName.text.toString()
                )
            )
            if (call.isSuccessful) {
                toggleLoader(false)
                Log.e("TAG", "upload: ${call.body()}",)
                session.user = call.body()!!.user
                showMessage(binding.root, getString(R.string.user_update_success))
            } else {
                toggleLoader(false)
                showMessage(
                    binding.root,
                    getErrorResponseArray(call.errorBody()).errors[0].toString()!!
                )
            }
        } catch (e: Exception) {
            toggleLoader(false)
            Log.e("TAG", "upload: ${e}", )
              }
    }
    }
}
