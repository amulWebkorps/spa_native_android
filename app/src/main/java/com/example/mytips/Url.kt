package com.example.mytips

import android.service.notification.Condition.SCHEME
import com.google.gson.FieldNamingPolicy
import com.google.gson.GsonBuilder
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import okhttp3.HttpUrl
import retrofit.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object Url {
    const val BASE_URL = "https://6782-103-187-101-22.ngrok.io"
}