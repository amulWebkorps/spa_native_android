package com.example.mytips.utilities.imagePicker

import android.Manifest
import android.app.Dialog
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.FileProvider
import androidx.fragment.app.FragmentManager
import com.example.mytips.R
import com.fondesa.kpermissions.allGranted
import com.fondesa.kpermissions.extension.permissionsBuilder
import com.fondesa.kpermissions.extension.send
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.theartofdev.edmodo.cropper.CropImage
import kotlinx.android.synthetic.main.bottomsheet_image_picker.*
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*

class ImagePickerNew : BottomSheetDialogFragment() {
    private lateinit var file: File
    private var onImageSelected: (Uri) -> (Unit) = {}
    private var onImageError: (String) -> (Unit) = {}
    private var imageUri: Uri? = null
    private lateinit var cropImageFile: File

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.bottomsheet_image_picker, container, false)
    }

    override fun getTheme(): Int = R.style.BottomSheetDialogTheme

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return BottomSheetDialog(requireContext(), theme)
    }

    private val cameraPermission by lazy {
        permissionsBuilder(
            Manifest.permission.CAMERA,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
        ).build()
    }

    private val galleryPermission by lazy {
        permissionsBuilder(Manifest.permission.WRITE_EXTERNAL_STORAGE).build()
    }

    private val camera = registerForActivityResult(ActivityResultContracts.TakePicture()) {
        if (it && imageUri != null) {
            dismiss()
//            imageUri?.let { uri -> cropImage(uri) }
            onImageSelected.invoke(imageUri!!)
        }
    }

    companion object {
        fun showDialog(
            fragmentManager: FragmentManager,
            onImageSelected: (Uri) -> (Unit),
            onImageError: (String) -> (Unit)
        ) {
            return ImagePickerNew().apply {
                this.onImageSelected = onImageSelected
                this.onImageError = onImageError

            }.show(fragmentManager, ImagePickerDialog2::class.java.simpleName)
        }
    }

    private val gallery = registerForActivityResult(ActivityResultContracts.GetContent()) {
        it?.let {
            cropImage(it)
            dismiss()
         //   onImageSelected(it)
        }
    }
    private fun cropImage(uri: Uri) {
        CropImage.activity(uri)
            .setFixAspectRatio(true)
            .setAspectRatio(1, 1)
            .setOutputUri(getUri(cropImageFile))
            .start(requireContext(), this)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        file = createFile()
        cropImageFile = createFile()
        imageUri = getUri(file)
        constraintTakePhoto.setOnClickListener {
            cameraPermission.send { it ->
                if (it.allGranted()) {
                    camera.launch(imageUri)
                }
            }
        }

        constraintOpenGallery.setOnClickListener {
            galleryPermission.send { result ->
                if (result.allGranted()) {
                    gallery.launch("image/*")
                }
            }
        }
    }


    private fun getUri(file: File): Uri {

        return FileProvider.getUriForFile(
            requireActivity().applicationContext,
            requireActivity().packageName + ".fileprovider", file
        )
    }

    @Throws(IOException::class)
    fun createFile(): File {
        val fileName = SimpleDateFormat("dd_MM_yyyy_HH_mm_ss", Locale.ENGLISH).format(Date())
        val storageDir =
            File(requireContext().cacheDir, getString(R.string.app_name)).apply { mkdirs() }
        return File(storageDir, "${fileName}.png").apply {
            createNewFile()
        }
    }
}