package com.example.spa.utilities

import android.app.Activity
import android.content.Context
import android.net.ConnectivityManager
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
import com.example.spa.R
import java.text.DecimalFormat
import java.text.SimpleDateFormat
import java.util.*
import kotlin.math.ln
import kotlin.math.log


fun TextView.setSpan(
        clickableString: String,
        clickableFont: Int,
        clickableColor: Int,
        isUnderline: Boolean = false,
        onClick: () -> Unit
    ) {

        val clickablePartStart = text.indexOf(clickableString)

        val spannableString = SpannableString(text)
    Log.e("TAG", "setSpan1: ${spannableString}", )
    Log.e("TAG", "setSpan: ${clickablePartStart}", )
    Log.e("TAG", "setSpan3: ${clickableString}", )
        highlightColor = ContextCompat.getColor(context, R.color.white)

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

fun hasInternet(context: Context):Boolean{
    try{
        val connectivityManager=context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val networkConnectivity=connectivityManager.activeNetworkInfo
        if(networkConnectivity!=null&&networkConnectivity.isConnectedOrConnecting){
            return true
        }
    }
    catch(e:Exception){
        return false
    }
    return false
}


fun String.getInFrench(context: Context):String{
    val dateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
    try {
        val date=dateFormat.parse(this)
        val calendar = Calendar.getInstance()
        calendar.time = date

        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH) + 1 // Month is zero-based, so add 1
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        return "$day  ${getMonthStringFun(month,context)}  $year"

        println("Year: $year, Month: ${getMonthStringFun(month,context)}, Day: $day")
        Log.e("TAG", "\"Year: $year, Month: $month, Day: $day\"", )
    } catch (e: Exception) {
        e.printStackTrace()
    }
  return ""
}
fun monthNameToNumber(monthName: String,context: Context): String {
    val months = listOf("Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec")

    val index = months.indexOf(monthName)
    return getMonthStringFun(index,context)
}


fun getMonthStringFun(month: Int,context: Context): String {
return when(month){
    1->context.getString(R.string.jan)
        2->context.getString(R.string.feb)
    3->context.getString(R.string.mar)
    4->context.getString(R.string.apr)
    5->context.getString(R.string.may)
    6->context.getString(R.string.jun)
    7->context.getString(R.string.july)
    8->context.getString(R.string.aug)
    9->context.getString(R.string.sep)
    10->context.getString(R.string.oct)
    11->context.getString(R.string.nov)
    12->context.getString(R.string.dec)
    else->context.getString(R.string.jan)
}
}

fun formatDate(date: String): String {
    val convertedDate = convertDate(date)
    return SimpleDateFormat("dd MMM yyyy").format(convertedDate)
}

fun formatDateDOB(date: String): String {
    return SimpleDateFormat("dd MMM yyyy").format(SimpleDateFormat("yyyy-MM-dd").parse(date))
}

fun convertDate(dateString: String): Date? {
    return SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'").parse(dateString)
}

fun formatDateGraph(date: String): String {
    val convertedDate = convertDateGraph(date)
    return SimpleDateFormat("dd/MM").format(convertedDate)
}

fun convertDateGraph(dateString: String): Date? {
    return SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS").parse(dateString)
}

fun setAppLocale(context: Context, language: String) {
    val locale = Locale(language)
    Locale.setDefault(locale)
    val config = context.resources.configuration
    config.setLocale(locale)
    context.createConfigurationContext(config)
    context.resources.updateConfiguration(config, context.resources.displayMetrics)

  }
