package com.example.spa.base

import android.content.Context
import androidx.appcompat.app.AlertDialog
import com.example.spa.R
import com.example.spa.utilities.getColorFromResource

class DialogUtils {

    fun showGeneralDialog(
        context: Context,
        title: String = "",
        message: String,
        positiveText: String,
        negativeText: String,
        onClick: () -> Unit,
        onNoClick: () -> Unit
    ) {
        val dialog = AlertDialog.Builder(context, R.style.AlertDialogCustom)
            .setTitle(if (title.isEmpty()) context.getString(R.string.app_name) else title)
            .setMessage(message)
            .setCancelable(false)
            .setPositiveButton(positiveText) { dialog, _ ->
                onClick.invoke()
                dialog.dismiss()
            }
            .setNegativeButton(negativeText) { dialog, _ ->
                onNoClick.invoke()
                dialog.dismiss()
            }.create()

        dialog.setOnShowListener {
            dialog.getButton(android.app.AlertDialog.BUTTON_POSITIVE)
                .setTextColor(context.getColorFromResource(R.color.black))
            dialog.getButton(android.app.AlertDialog.BUTTON_NEGATIVE)
                .setTextColor(context.getColorFromResource(R.color.black))
        }

        dialog.show()
    }

}