package com.example.spa.ui.home.fragment

import android.content.Context.WINDOW_SERVICE
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Point
import android.os.Bundle
import android.util.Log
import android.view.*
import androidmads.library.qrgenearator.QRGContents
import androidmads.library.qrgenearator.QRGEncoder
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.example.spa.R
import com.example.spa.base.BaseFragment
import com.example.spa.databinding.FragmentShareQrBinding
import com.example.spa.utilities.Resource
import com.example.spa.utilities.showMessage
import com.example.spa.viewmodel.AuthViewModel
import com.google.zxing.WriterException


class ShareQROneTimeFragment : BaseFragment() {
    var bitmap: Bitmap? = null
    var qrgEncoder: QRGEncoder? = null
    private lateinit var binding: FragmentShareQrBinding
    private val authViewModel: AuthViewModel by viewModels()
    var linkUrl: String? = ""

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentShareQrBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setClick()
        binding.textView.text = getString(R.string.share_qr_one_time_text)
        getUserResponse()
        callMe()

    }

    private fun callMe() {
        toggleLoader(true)
        lifecycleScope.launchWhenCreated {
            authViewModel.getUser(
                session.token
            )
        }
    }

    private fun getUserResponse() {
        Log.e("payment_link", "result")
        lifecycleScope.launchWhenCreated {
            authViewModel.getUser.collect { result ->
                when (result) {
                    is Resource.Error -> {
                        toggleLoader(false)
                        result.message?.let { showMessage(binding.root, it) }
                    }
                    is Resource.Loading -> {}
                    is Resource.Success -> {
                        toggleLoader(false)
                        result.data?.let { it ->
                            if (it?.payment_link != null && it.payment_link.isNotEmpty()) {
                                linkUrl=it.payment_link
                                generateQR(it.payment_link)
                            }
                        }
                    }
                }
            }
        }
    }

    private fun setClick() {
        binding.includeToolbar.imageViewBack.setOnClickListener {
            requireActivity().finish()
        }
        binding.shareQRCode.setOnClickListener {
            if (linkUrl!!.isNotEmpty()){
                val shareIntent = Intent(Intent.ACTION_SEND)
                shareIntent.type = "text/plain"
                shareIntent.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.app_name))
                //val app_url = qrgEncoder!!.encodeAsBitmap().toString()
                //shareIntent.putExtra(Intent.EXTRA_TEXT, app_url)
                shareIntent.putExtra(
                    Intent.EXTRA_TEXT,
                    "Greeting's from My Tips, "+session.user!!.first_name+" "+session.user!!.last_name+" has shared a payment link with you. Please click on the link to proceed with the payment :- $linkUrl"
                )
                startActivity(Intent.createChooser(shareIntent, "Share Link"))
            }
        }
    }

    private fun generateQR(paymentLink: String) {

        val manager = requireContext().getSystemService(WINDOW_SERVICE) as WindowManager?
        val display: Display = manager!!.defaultDisplay
        val point = Point()
        display.getSize(point);

        val width = point.x
        val height = point.y

        var dimen = if (width < height) width else height
        dimen = dimen * 3 / 4
        qrgEncoder = QRGEncoder(paymentLink, null, QRGContents.Type.TEXT, dimen)

        try {
            // getting our qrcode in the form of bitmap.
            bitmap = qrgEncoder!!.encodeAsBitmap()
            // the bitmap is set inside our image 
            // view using .setimagebitmap method.
            binding.idIVQrcode.setImageBitmap(bitmap)
        } catch (e: WriterException) {
            // this method is called for 
            // exception handling.
            Log.e("TAG", "generateQR: ${e.toString()}")
        }
    }

}