package com.example.mytips

import android.service.notification.Condition.SCHEME
import com.google.gson.FieldNamingPolicy
import com.google.gson.GsonBuilder
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import okhttp3.HttpUrl
import retrofit.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object Url {
    const val BASE_URL = "https://51bb-103-17-99-145.in.ngrok.io"

    const val USER = "/users"
    const val LOGIN = "$USER/login"
}