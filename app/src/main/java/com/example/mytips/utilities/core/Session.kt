package com.example.mytips.utilities.core

import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences
import javax.inject.Inject
import javax.inject.Singleton

class Session(context: Context)  {



    private val sharedPreferences: SharedPreferences = context.getSharedPreferences(AppPreferences.SHARED_PREF_NAME, Context.MODE_PRIVATE)

    @SuppressLint("CommitPrefEdits")
    fun putBoolean(name: String, value: Boolean) {
        val editor = sharedPreferences.edit()
        editor!!.putBoolean(name, value)
        editor.apply()
    }

    fun getBoolean(name: String): Boolean {
        return sharedPreferences.getBoolean(name, false)
    }


    @SuppressLint("CommitPrefEdits")
    fun putString(name: String, value: String) {
        val editor = sharedPreferences.edit()
        editor!!.putString(name, value)
        editor.apply()
    }

    fun getString(name: String): String {
        return sharedPreferences.getString(name, "")!!
    }

    var isLogin: Boolean
    get() = getBoolean(IS_LOGIN)
    set(value) {putBoolean(IS_LOGIN, value)}

    var countryCode: String
        get() = getString(COUNTRY_CODE)
        set(value) {putString(COUNTRY_CODE, value)}

    var phoneNumber: String
        get() = getString(PHONE_NUMBER)
        set(value) {putString(PHONE_NUMBER, value)}

    var token: String
        get() = getString(TOKEN)
        set(value) {putString(TOKEN, value)}

    companion object {
        const val IS_LOGIN = "is-login"
        const val COUNTRY_CODE = "country-code"
        const val PHONE_NUMBER = "phone-number"
        const val TOKEN = "token"
    }
}
