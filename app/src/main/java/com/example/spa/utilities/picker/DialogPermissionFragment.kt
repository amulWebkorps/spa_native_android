package com.example.spa.utilities.picker

import android.app.Dialog
import android.content.Context
import android.view.LayoutInflater
import com.example.spa.databinding.DialogPermissionBinding

class PermissionDialog {

    private lateinit var binding: DialogPermissionBinding


    fun showPermissionDialog(
        context: Context,
        positiveListener: () -> Unit = {  }
    ) {
        binding = DialogPermissionBinding.inflate(LayoutInflater.from(context))
        val dialog = Dialog(context).apply {
            setContentView(binding.root)
            setCancelable(true)
            window?.setBackgroundDrawableResource(android.R.color.transparent)

            with(binding) {
                imageViewClose.setOnClickListener { dismiss() }

                buttonSubmit.setOnClickListener {
                        positiveListener.invoke()
                        dismiss()
                }
            }
        }
        dialog.show()
    }


}