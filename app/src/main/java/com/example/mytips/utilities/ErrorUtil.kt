package com.example.mytips.utilities

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Context
import android.util.Log
import android.view.View
import android.widget.Toast
import com.google.android.material.snackbar.Snackbar

class ErrorUtil {
  @SuppressLint("SuspiciousIndentation")
  fun createErrorDialog(context: Context, errorTitle:String?, errormsg:String){
   val alertbuilder=AlertDialog.Builder(context)
       with(alertbuilder){
           errorTitle?.let{title->
               setTitle(title)
           }
           setMessage(errormsg)
           setPositiveButton("Ok"){dialog, which->
             dialog.cancel()
           }
           alertbuilder.create().show()
       }
  }
    fun log(tag: String, msg: String) {
        Log.d(tag,msg)
    }
    fun err(tag: String, msg: String) {
        Log.e(tag,msg)
    }


}
fun showToast(ctx: Context, msg: String,gravity: Int?){
    val toast = Toast.makeText(ctx, msg, Toast.LENGTH_SHORT)
    gravity?.let {
        toast.setGravity(gravity, 0, 0)
    }

    toast.show()
}

fun showMessage(view: View,message: String, isError: Boolean = false) {


    val snackbar: Snackbar = Snackbar
        .make(
            view,
            message.toString(),
            Snackbar.LENGTH_LONG
        )

    val snackbarView: View = snackbar.view
//    snackbarView.setBackgroundColor(R.color.colorPrimaryDark)
//    snackbar.setBackgroundTint(R.color.colorPrimaryDark)
//
    //    val textView = snackbarView.findViewById<View>(R.id.snackbar_text) as TextView
    snackbar.show()
}

