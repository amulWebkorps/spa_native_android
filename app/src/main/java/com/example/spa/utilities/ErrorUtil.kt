package com.example.spa.utilities

import android.annotation.SuppressLint
import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.fragment.app.FragmentActivity
import com.example.spa.App
import com.example.spa.R
import com.example.spa.ui.auth.activity.AuthActivity
import com.google.android.material.snackbar.Snackbar
import com.google.gson.Gson
import okhttp3.ResponseBody

class ErrorUtil {
    @SuppressLint("SuspiciousIndentation")
    fun createErrorDialog(context: Context, errorTitle: String?, errormsg: String) {
        val alertbuilder = AlertDialog.Builder(context)
        with(alertbuilder) {
            errorTitle?.let { title ->
                setTitle(title)
            }
            setMessage(errormsg)
            setPositiveButton("Ok") { dialog, which ->
                dialog.cancel()
            }
            alertbuilder.create().show()
        }
    }

    fun log(tag: String, msg: String) {
        Log.d(tag, msg)
    }

    fun err(tag: String, msg: String) {
        Log.e(tag, msg)
    }


}

fun showToast(ctx: Context, msg: String, gravity: Int?) {
    val toast = Toast.makeText(ctx, msg, Toast.LENGTH_SHORT)
    gravity?.let {
        toast.setGravity(gravity, 0, 0)
    }

    toast.show()
}

fun showMessage(view: View, message: String, isError: Boolean = false) {

    var newMsg=message
    if (message == "Mobile number is incorrect"){
        newMsg=view.context.getString(R.string.mobile_number_is_incorrect)
    }else if (message == "Country code is incorrect"){
        newMsg=view.context.getString(R.string.country_code_is_incorrect)
    }else if (message == "password is incorrect"){
        newMsg=view.context.getString(R.string.password_is_incorrect)
    }else if (message == "Mobile number has already been taken"){
        newMsg=view.context.getString(R.string.mobile_number_has_already_been_taken)
    }else if (message == "invalid otp code"){
        newMsg=view.context.getString(R.string.invalid_otp_code)
    }else if (message == "Email Id already exists"){
        newMsg=view.context.getString(R.string.email_id_already_exists)
    }else if (message == "Amount can\\'t be more than wallet balance"){
        newMsg=view.context.getString(R.string.amount_can_be_more_than_wallet_balance)
    }else if (message == "successfully deleted"){
        newMsg=view.context.getString(R.string.successfully_deleted)
    }else if (message == "Account number is not a number"){
        newMsg=view.context.getString(R.string.account_number_is_not_a_number)
    }else if (message == "User is suspended"){
        newMsg=view.context.getString(R.string.user_is_suspended)
    }else if (message == "Something went wrong"){
        newMsg=view.context.getString(R.string.something_went_wrong)
    }
    val snackbar: Snackbar = Snackbar
        .make(
            view,
            newMsg.toString(),
            Snackbar.LENGTH_LONG
        )

    val snackbarView: View = snackbar.view
//    snackbarView.setBackgroundColor(R.color.colorPrimaryDark)
//    snackbar.setBackgroundTint(R.color.colorPrimaryDark)
//
    //    val textView = snackbarView.findViewById<View>(R.id.snackbar_text) as TextView
    snackbar.show()
}

fun logoutMainApp(requireActivity: FragmentActivity) {
    (requireActivity.application as App).session.isLogin = false
    (requireActivity.application as App).session.editor.clear()
    val intent= Intent(requireActivity, AuthActivity::class.java)
    val bundle = Bundle()
    bundle.putBoolean(Constants.COME_FROM_SUSPEND, true)
    intent.putExtras(bundle)
    requireActivity.startActivity(intent)
}

fun getErrorResponse(error: ResponseBody?): Errors {
    val gson = Gson()
    var baseResponse = Errors()
    baseResponse.errors = "Something went wrong"
    try {
        baseResponse = gson.fromJson(error!!.charStream(), Errors::class.java)
        baseResponse.errors = baseResponse.errors
    } catch (e: Exception) {
    }
    return baseResponse
}

fun getErrorResponseArray(error: ResponseBody?): ErrorsArray {
    val gson = Gson()
    var baseResponse = ErrorsArray()
    baseResponse.errors = arrayOf("Something went wrong")
    try {
        baseResponse = gson.fromJson(error!!.charStream(), ErrorsArray::class.java)
        baseResponse.errors = baseResponse.errors

    } catch (e: Exception) {
    }
    return baseResponse
}

fun getErrorResponseArray2(error: ResponseBody?, code: Int?, context: Context): ErrorsArray {
    val gson = Gson()
    var baseResponse = ErrorsArray()
    baseResponse.errors = arrayOf("Something went wrong")
    try {
        baseResponse = gson.fromJson(error!!.charStream(), ErrorsArray::class.java)
        baseResponse.errors = baseResponse.errors

        //  session.isLogin = false
        if (code == 401) {

            val intent = Intent(context, AuthActivity::class.java)
            (context as Activity).finish()
            context.startActivity(intent)
        }
    } catch (e: Exception) {
    }
    return baseResponse
}

