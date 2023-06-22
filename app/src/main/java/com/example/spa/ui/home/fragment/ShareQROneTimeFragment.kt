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
import com.example.spa.R
import com.example.spa.base.BaseFragment
import com.example.spa.databinding.FragmentShareQrBinding
import com.google.zxing.WriterException


class ShareQROneTimeFragment:BaseFragment() {
    var bitmap: Bitmap? = null
    var qrgEncoder: QRGEncoder? = null
    private lateinit var binding:FragmentShareQrBinding

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
         generateQR()
        setClick()
        binding.textView.text = getString(R.string.share_qr_one_time_text)
    }

    private fun setClick() {
        binding.includeToolbar.imageViewBack.setOnClickListener {
            requireActivity().finish()
        }
        binding.shareQRCode.setOnClickListener {
            val shareIntent = Intent(Intent.ACTION_SEND)
            shareIntent.type = "text/plain"
            shareIntent.putExtra(Intent.EXTRA_SUBJECT, "Insert Subject here")
            val app_url = qrgEncoder!!.encodeAsBitmap().toString()
            shareIntent.putExtra(Intent.EXTRA_TEXT, app_url)
            startActivity(Intent.createChooser(shareIntent, "Share Link"))
        }
    }

    private fun generateQR() {

         val manager = requireContext().getSystemService(WINDOW_SERVICE) as WindowManager?
        val display: Display = manager!!.defaultDisplay
        val point = Point()
        display.getSize(point);

        val width = point.x
        val height = point.y

        var dimen = if (width < height) width else height
        dimen = dimen * 3 / 4
        qrgEncoder = QRGEncoder("https://spa-native.herokuapp.com/user_payment?user_id=10&product_name=Personal%20QR", null, QRGContents.Type.TEXT, dimen)

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