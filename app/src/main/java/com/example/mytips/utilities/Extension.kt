package com.example.mytips.utilities

import android.content.Context
import android.os.Build
import android.text.Spannable
import android.text.SpannableString
import android.text.TextPaint
import android.text.method.HideReturnsTransformationMethod
import android.text.method.LinkMovementMethod
import android.text.method.PasswordTransformationMethod
import android.text.style.ClickableSpan
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.TextView
import androidx.annotation.ColorRes
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import com.google.gson.Gson
import okhttp3.ResponseBody
import org.json.JSONException
import org.json.JSONObject
import java.lang.Error
import java.text.DecimalFormat
import kotlin.math.ln


fun TextView.setSpan(
        clickableString: String,
        clickableFont: Int,
        clickableColor: Int,
        isUnderline: Boolean = false,
        onClick: () -> Unit
    ) {
        val clickablePartStart = text.indexOf(clickableString)

        val spannableString = SpannableString(text)

        //highlightColor = ContextCompat.getColor(context, clickableColor)

        spannableString.setSpan(
            CustomTypefaceSpan(ResourcesCompat.getFont(context!!, clickableFont), ""),
            clickablePartStart,
            clickablePartStart + clickableString.length,
            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        )


        val clickableSpan = object : ClickableSpan() {
            override fun onClick(widget: View) = onClick.invoke()
            override fun updateDrawState(ds: TextPaint) {
                ds.color = ContextCompat.getColor(context, clickableColor)
                if (isUnderline)
                    ds.isUnderlineText = true
            }
        }
        spannableString.setSpan(
            clickableSpan,
            clickablePartStart,
            clickablePartStart + clickableString.length,
            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        )

        this.text = spannableString
        this.movementMethod = LinkMovementMethod.getInstance()
    }

fun EditText.showPassword(isShow: Boolean = false) {
    if (isShow) {
        this.transformationMethod =
            HideReturnsTransformationMethod.getInstance()
    } else {
        this.transformationMethod =
            PasswordTransformationMethod.getInstance()
    }
}

fun EditText.setEnd() {
    this.setSelection(this.text.length)
}
fun <T> lazyFast(operation: () -> T): Lazy<T> = lazy(LazyThreadSafetyMode.NONE) {
    operation()
}

fun Long.coolNumberFormat(): String {
    if (this < 1000) return "" + this
    val exp = (ln(this.toDouble()) / ln(1000.0)).toInt()
    val format = DecimalFormat("0.#")
    val value: String = format.format(this / Math.pow(1000.0, exp.toDouble()))
    return String.format("%s%c", value, "KMBTPE"[exp - 1])
}

fun View.showView() {
    visibility = View.VISIBLE
}

fun View.hideView() {
    visibility = View.GONE
}


fun Context.getColorFromResource(@ColorRes colorInt: Int): Int {

    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
        this.resources.getColor(colorInt, theme)
    else
        this.resources.getColor(colorInt)
}


