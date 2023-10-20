package com.example.spa.ui.home.fragment

import android.app.DatePickerDialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.text.InputType
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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
import com.example.spa.utilities.Constants
import com.example.spa.utilities.Resource
import com.example.spa.utilities.formatDateDOB
import com.example.spa.utilities.getErrorResponseArray
import com.example.spa.utilities.hasInternet
import com.example.spa.utilities.showMessage
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
import java.util.Calendar
import java.util.Locale

class EditProfileFragment : BaseFragment() {

    private lateinit var binding: FragmentEditProfileBinding
    private val authViewModel: AuthViewModel by viewModels()
    var imageUri: Uri? = null
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
            Log.e("TAG", "onCreate: ${session.user!!.image}")


            Picasso.with(requireContext())
                .load((requireActivity().application as App).session.user!!.image)
                .placeholder(R.drawable.place_holder)
                .into(binding.imageViewProfile);
        }


        binding.layoutPhone.editTextPhoneNumber.inputType = InputType.TYPE_NULL
        if (session.language == getString(R.string.arabic)){
            binding.layoutPhone.editTextPhoneNumber.gravity= Gravity.END
        }else{
            binding.layoutPhone.editTextPhoneNumber.gravity= Gravity.START
        }
        binding.layoutPhone.ccp.setCcpClickable(false)
        binding.textInputFirstName.setText(session.user!!.first_name)
        binding.textInputLastName.setText(session.user!!.last_name)

        binding.textInputCardId.setText(session.user!!.business_id_card)
        binding.textInputAboutMe.setText(session.user!!.about_me)

        if(session.user!!.dob != null && session.user!!.dob.isNotEmpty()){
            try {
                binding.textInputDOB.setText(formatDateDOB(session.user!!.dob))
                var dateOld=SimpleDateFormat("yyyy-MM-dd").parse(session.user!!.dob)
                val yearInt=(SimpleDateFormat("yyyy").format(dateOld)).toInt()
                val monthInt=(SimpleDateFormat("MM").format(dateOld)).toInt()
                val dayInt=(SimpleDateFormat("dd").format(dateOld)).toInt()
                calendar.set(Calendar.YEAR, yearInt)
                calendar.set(Calendar.MONTH, monthInt-1)
                calendar.set(Calendar.DAY_OF_MONTH, dayInt)
            } catch (e: Exception) {
            }
        }
        binding.layoutPhone.editTextPhoneNumber.setText(session.user!!.mobile_number)
        binding.layoutPhone.editTextPhoneNumber.setTextColor(
            ContextCompat.getColor(
                requireContext(),
                R.color.colorGreyB2
            )
        )
        //binding.layoutPhone.ccp.setCountryForPhoneCode(session.countryCode.toInt())
        binding.layoutPhone.ccp.setCountryForNameCode(session.countryChars)
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
            val mCurrentDate = Calendar.getInstance();
            mCurrentDate.set(
                mCurrentDate.get(Calendar.YEAR) - 18,
                mCurrentDate.get(Calendar.MONTH),
                mCurrentDate.get(Calendar.DAY_OF_MONTH)
            )
            var intYear=calendar.get(Calendar.YEAR) - 18
            if (binding.textInputDOB!!.text.toString().isNotEmpty()) {
                intYear=calendar.get(Calendar.YEAR)
            }
            val value: Long = mCurrentDate.timeInMillis
            val datePickerDialog = DatePickerDialog(
                requireContext(),
                dateSetListener,
                // Set DatePickerDialog to point to today's date when it loads up
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
            )


            calendar.add(Calendar.YEAR, -18)
            datePickerDialog.datePicker.maxDate = value
            // Show the DatePickerDialog
            datePickerDialog.show()
        }
        binding.buttonSave.setOnClickListener {
            if (isValidationSuccess()) {
                if (hasInternet(requireContext())) {
                    toggleLoader(true)
                    CoroutineScope(Dispatchers.IO).launch {
                        upload()
                    }
                } else {
                    showMessage(binding.root, getString(R.string.no_internet_connection))
                }
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
            val intent = Intent(requireContext(), HomeActivity::class.java)
            intent.putExtra(Constants.SCREEN_NAME, Constants.EDIT_PROFILE)
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
            showMessage(binding.root, e.message)
            return false
        }
        return true
    }

    private fun updateUserResponse() {
        lifecycleScope.launchWhenCreated {
            authViewModel.updateUserDetail.collect { result ->
                when (result) {
                    is Resource.Error -> {
                        toggleLoader(false)
                        result.message?.let {
                            showMessage(binding.root, it)
                        }
                    }
                    is Resource.Loading -> {}
                    is Resource.Success -> {
                        toggleLoader(false)
                        result.data.let {
                            showMessage(binding.root, getString(R.string.user_update_success))
                            session.user = it!!.user
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

        Log.e("TAG", "upload: 1")

        try {
            Log.e("TAG", "upload: 2")
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
        val myFormat = "dd/MM/yyyy"
        val sdf = SimpleDateFormat(myFormat, Locale.US)
        var sDOB = ""
        if (binding.textInputDOB!!.text.toString().isNotEmpty()) {
            sDOB = sdf.format(calendar.time)
        }
        val dob: RequestBody = RequestBody.create(
            "text/plain".toMediaTypeOrNull(),
            sDOB
        )
        val businessID: RequestBody = RequestBody.create(
            "text/plain".toMediaTypeOrNull(),
            binding.textInputCardId.text.toString()
        )
        val aboutMe: RequestBody = RequestBody.create(
            "text/plain".toMediaTypeOrNull(),
            binding.textInputAboutMe.text.toString()
        )

        if (isImage) {
            try {
                val call: retrofit2.Response<UpdateUserResponse> = retrofit.updateUserDetailsImage(
                    session.token,
                    email, firstName, lastName,businessID,aboutMe,dob, part
                )
                if (call.isSuccessful) {
                    toggleLoader(false)
                    Log.e("TAG", "upload: ${call.body()!!.user}")
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

                Log.e("TAG", "upload: ${e}")
                showMessage(binding.root, getString(R.string.no_internet_connection))
            }
        } else {
            try {
                val myFormat = "dd/MM/yyyy"
                val sdf = SimpleDateFormat(myFormat, Locale.US)
                var sDOB = ""
                if (binding.textInputDOB!!.text.toString().isNotEmpty()) {
                    sDOB = sdf.format(calendar.time)
                }
                val call: retrofit2.Response<UpdateUserResponse> =
                    retrofit.updateUserDetailsWithoutImage(
                        session.token, User(
                            email = session.user!!.email,
                            first_name = binding.textInputFirstName.text.toString(),
                            last_name = binding.textInputLastName.text.toString(),
                            business_id_card = binding.textInputCardId.text.toString(),
                            about_me = binding.textInputAboutMe.text.toString(),
                            dob = sDOB,
                        )
                    )
                if (call.isSuccessful) {
                    toggleLoader(false)
                    Log.e("TAG", "upload: ${call.body()}")
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
                Log.e("TAG", "upload: ${e}")
            }
        }
    }
}
