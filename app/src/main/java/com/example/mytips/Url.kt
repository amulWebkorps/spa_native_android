package com.example.mytips

import android.service.notification.Condition.SCHEME
import com.google.gson.FieldNamingPolicy
import com.google.gson.GsonBuilder
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import okhttp3.HttpUrl
import retrofit.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object Url {
    const val BASE_URL = "https://d315-103-187-101-22.in.ngrok.io"

    const val USER = "/users"
    const val LOGIN = "$USER/login"
    const val REGISTER = "$USER/registration"
    const val USER_PROFILE = "$USER/profile"
    const val UPDATE_PHONE = "$USER/update_mobile_number"

    const val BANK_ACCOUNTS="/bank_accounts"
    const val DELETE_BANK_ACCOUNTS="/bank_accounts/"
    const val USER_UPDATE="$USER/update"
}