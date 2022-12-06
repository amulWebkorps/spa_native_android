package com.example.mytips.utilities.imagePicker;

import android.Manifest
import android.app.Activity
import android.app.Dialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.FileProvider
import androidx.fragment.app.FragmentManager
import com.example.mytips.R
import com.fondesa.kpermissions.allGranted
import com.fondesa.kpermissions.extension.permissionsBuilder
import com.fondesa.kpermissions.extension.send


import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import kotlinx.android.synthetic.main.bottomsheet_image_picker.*

import java.io.File

class ImagePickerDialog2 : BottomSheetDialogFragment() {

    private val openCamera = 654
    private val pickImage = 659
    private var tempImage = "thumbImage"
    private var onImageSelected: (Uri) -> (Unit) = {}
    private var onImageError: (String) -> (Unit) = {}

    companion object {
        fun showDialog(
            fragmentManager: FragmentManager,
            onImageSelected: (Uri) -> (Unit),
            onImageError: (String) -> (Unit)
        ) {
            return ImagePickerDialog2().apply {
                this.onImageSelected = onImageSelected
                this.onImageError = onImageError
            }.show(fragmentManager, ImagePickerDialog2::class.java.simpleName)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.bottomsheet_image_picker, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        constraintTakePhoto.setOnClickListener {
            permissionsBuilder(
                Manifest.permission.CAMERA,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            )
                .build()
                .send { result ->
                    if (result.allGranted()) {
                        captureImageFromCamera()
                    }
                }

        }
        constraintOpenGallery.setOnClickListener {

            permissionsBuilder(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .build()
                .send { result ->

                    if (result.allGranted()) {
                        pickImageFromGallery()
                    }
                }

        }
    }

    override fun getTheme(): Int = R.style.BottomSheetDialogTheme

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return BottomSheetDialog(requireContext(), theme)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        when (requestCode) {
            openCamera -> {
                if (resultCode == Activity.RESULT_OK) {
                    var f = File(Environment.getExternalStorageDirectory().toString())
                    var uri: Uri? = null
                    for (temp in f.listFiles()) {
                        if (temp.name == tempImage) {
                            f = temp
                            uri = getUri(f)
                            break
                        }
                    }
                    dismiss()
                    if (uri != null) {
                        onImageSelected.invoke(uri)
                    } else {
                        onImageError.invoke("Error!")
                    }
                } else {
                    dismiss()
                    onImageError.invoke("Error!")
                }
            }
            pickImage -> {
                if (resultCode == Activity.RESULT_OK) {
                    val selectedImage = data?.data
                    if (selectedImage != null) {
                        val realPath = ImageFilePath.getPath(requireContext(), selectedImage)
                        val file = File(realPath)
                        Log.e("ImagePick","Path ${file.path}")
                        val uri = getUri(file)
                        dismiss()
                        onImageSelected(uri)
                    } else {
                        onImageError.invoke("Error!")
                    }
                } else {
                    dismiss()
                    onImageError.invoke("Error!")
                }
            }
        }
    }

    private fun captureImageFromCamera() {
        val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        val f = File(Environment.getExternalStorageDirectory(), tempImage)
        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, getUri(f))
        startActivityForResult(cameraIntent, openCamera)
    }

    private fun pickImageFromGallery() {
        val intent = Intent()
        intent.type = "image/*"
        intent.action = Intent.ACTION_GET_CONTENT
        startActivityForResult(
            Intent.createChooser(
                intent,
                getString(R.string.gallery)
            ), pickImage
        )
    }

    private fun getUri(file: File): Uri {
        return FileProvider.getUriForFile(
            requireActivity().applicationContext,
            requireActivity().packageName + ".fileprovider", file
        )
    }
}