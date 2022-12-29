package com.example.spa.network

import android.content.Context
import com.example.spa.Url
import com.example.spa.data.remote.AuthApi
import com.example.spa.data.remote.SettingsApi
import com.example.spa.data.services.AuthService
import com.example.spa.data.services.SettingsService
import com.example.spa.repo.auth.AuthRepository
import com.example.spa.repo.settings.SettingsRepository
import com.example.spa.utilities.core.Session
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create
import java.util.concurrent.TimeUnit
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
class AppModule {


    var httpClient: OkHttpClient.Builder = OkHttpClient.Builder()
        .callTimeout(1, TimeUnit.MINUTES)
        .connectTimeout(3, TimeUnit.SECONDS)
        .readTimeout(3, TimeUnit.SECONDS)
        .writeTimeout(3, TimeUnit.SECONDS)


    @Singleton
    @Provides
    fun provideAuthAPI(): AuthApi {
        return Retrofit.Builder()
            .baseUrl(Url.BASE_URL)
            .client(httpClient.build())
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create()
    }

    @Singleton
    @Provides
    fun provideAuthRepository(authApi: AuthApi): AuthRepository {
        return AuthService(authApi)
    }


    @Singleton
    @Provides
    fun provideSettingAPI(): SettingsApi {
        return Retrofit.Builder()
            .baseUrl(Url.BASE_URL)
            .client(httpClient.build())
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create()
    }

    @Singleton
    @Provides
    fun provideSettingRepository(settingsApi: SettingsApi): SettingsRepository {
        return SettingsService(settingsApi)
    }

    @Singleton
    @Provides
    fun provideSession(@ApplicationContext context: Context): Session {
        return Session(context)
    }

}