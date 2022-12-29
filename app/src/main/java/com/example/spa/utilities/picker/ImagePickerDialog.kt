package com.example.spa.utilities.picker

import android.Manifest
import android.app.Activity.RESULT_OK
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.fragment.app.FragmentManager
import com.example.spa.R
import com.example.spa.base.BaseBottomSheetDialogFragment
import com.example.spa.base.DialogUtils
import com.example.spa.databinding.BottomsheetImagePickerBinding
import com.example.spa.utilities.lazyFast
import com.fondesa.kpermissions.extension.*
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.theartofdev.edmodo.cropper.CropImage
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*

class ImagePickerDialog : BaseBottomSheetDialogFragment() {
    private lateinit var cropImageFile: File
    val REQUEST_CODE =101

    companion object {
        fun showDialog(
            fragmentManager: FragmentManager,
            onImageSelected: (Uri) -> (Unit),
            onImageError: (String) -> (Unit)
        ) {
            return ImagePickerDialog().apply {
                this.onImageSelected = onImageSelected
                this.onImageError = onImageError
            }.show(fragmentManager, ImagePickerDialog::class.java.simpleName)
        }

        fun showDialog(
            fragmentManager: FragmentManager,
            title: String?, message: String,
            positive: String, negative: String,
            positiveListener: () -> Unit = {},
            negativeListener: () -> Unit = {}
        ) {

        }
    }
    private val requestGallery = 2
    private lateinit var file: File
    private var onImageSelected: (Uri) -> (Unit) = {}
    private var onImageError: (String) -> (Unit) = {}
    private var imageUri: Uri? = null
    private var _binding: BottomsheetImagePickerBinding? = null
    private val binding get() = _binding!!

    private val cameraPermission by lazyFast {
        permissionsBuilder(
            Manifest.permission.CAMERA/*,
            Manifest.permission.READ_EXTERNAL_STORAGE*/
        ).build()
    }


    private val galleryPermission by lazyFast {
        permissionsBuilder(Manifest.permission.READ_EXTERNAL_STORAGE).build()
    }

    private val camera = registerForActivityResult(ActivityResultContracts.TakePicture()) {
        if (it && imageUri != null) {
            /*dismiss()
            onImageSelected.invoke(imageUri!!)*/
            imageUri?.let { uri -> cropImage(uri) }
        }
    }

    fun showDialog(
        fragmentManager: FragmentManager,
        onImageSelected: (Uri) -> (Unit),
        onImageError: (String) -> (Unit)
    ) {
        return ImagePickerDialog().apply {
            this.onImageSelected = onImageSelected
            this.onImageError = onImageError
        }.show(fragmentManager, ImagePickerDialog::class.java.simpleName)
    }

    private val gallery = registerForActivityResult(ActivityResultContracts.GetContent()) {
        it?.let {
            cropImage(it)
            /*dismiss()
            onImageSelected(it)*/
        }
    }

    private fun cropImage(uri: Uri) {
        CropImage.activity(uri)
            .setFixAspectRatio(true)
            .setAspectRatio(1, 1)
            .setOutputUri(getUri(cropImageFile))
            .start(requireContext(), this)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_CODE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                openCamera()
            } else {
                Toast.makeText(requireContext(), "camera permission denied", Toast.LENGTH_LONG).show();
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        when (requestCode) {
            requestGallery -> {
                if (resultCode == RESULT_OK) {
                    data?.data?.let {
                        cropImage(it)
                    }
                }
            }

            CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE -> {
                dismiss()
                onImageError.invoke("Error in select image!")
            }
            CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE -> {
                dismiss()
                val result = CropImage.getActivityResult(data)
                if (result != null) {
                    if (result.uri != null) {
                        result?.let { onImageSelected.invoke(result.uri) }
                    }
                }
            }
        }
    }

    override fun isTitleAvailable() = false
    override fun getBinding(): View {
        _binding = BottomsheetImagePickerBinding.inflate(LayoutInflater.from(context), null, false)
        return binding.root
    }

    override fun getTheme(): Int = R.style.BottomSheetDialogTheme

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return BottomSheetDialog(requireContext(), theme)
    }


    override fun onReady() {
        file = createFile()
        cropImageFile = createFile()
        imageUri = getUri(file)
        binding.constraintTakePhoto.setOnClickListener {
            //if (requireContext().checkCameraPermission()) {
                openCamera()
//            }else{
//                PermissionDialog().showPermissionDialog(requireContext()) {
//
//                }
//            }
        }
        binding.constraintOpenGallery.setOnClickListener {
            intentGallery()
        }
    }
    fun openCamera(){
        when {
            ContextCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.CAMERA
            ) == PackageManager.PERMISSION_GRANTED -> {
                camera.launch(imageUri)
            }
            shouldShowRequestPermissionRationale("camera permission") -> {
                camera.launch(imageUri)
            // In an educational UI, explain to the user why your app requires this
            // permission for a specific feature to behave as expected, and what
            // features are disabled if it's declined. In this UI, include a
            // "cancel" or "no thanks" button that lets the user continue
            // using your app without granting the permission.

        }
            else -> {
                // You can directly ask for the permission.
                requestPermissions(
                    arrayOf(Manifest.permission.CAMERA),
                    REQUEST_CODE)
            }
        }
//                camera.launch(imageUri)
        }
    private fun intentGallery() {
        Log.e("TAG", "intentGallery: 1", )
        val galleryIntent = Intent(
            Intent.ACTION_GET_CONTENT, MediaStore.Images.Media.EXTERNAL_CONTENT_URI
        )
        galleryIntent.setType("image/*");
        startActivityForResult(galleryIntent, requestGallery)
    }
    fun Context.checkCameraPermission(): Boolean {
        Log.e("TAG", "intentGallery: 2", )
        val permission = Manifest.permission.CAMERA
        val res: Int = this.checkCallingOrSelfPermission(permission)
        return res == PackageManager.PERMISSION_GRANTED
    }

    private fun getUri(file: File): Uri {

        Log.e("TAG", "intentGallery: 3", )
        return FileProvider.getUriForFile(
            requireActivity().applicationContext,
            requireActivity().packageName + ".fileprovider", file
        )
    }

    @Throws(IOException::class)
    fun createFile(): File {

        Log.e("TAG", "intentGallery: 4", )
        val fileName = SimpleDateFormat("dd_MM_yyyy_HH_mm_ss", Locale.ENGLISH).format(Date())
        val storageDir =
            File(requireContext().cacheDir, getString(R.string.app_name)).apply { mkdirs() }
        return File(storageDir, "${fileName}.png").apply {
            createNewFile()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}