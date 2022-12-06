package com.example.mytips.network

import android.content.Context
import com.example.mytips.Url
import com.example.mytips.data.remote.AuthApi
import com.example.mytips.data.remote.PhotoApi
import com.example.mytips.data.repository.AuthRepositoryImpl
import com.example.mytips.data.repository.PhotoRepositoryImpl
import com.example.mytips.repo.PhotoRepository
import com.example.mytips.repo.auth.AuthRepository
import com.example.mytips.utilities.core.Session
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
class AppModule {

    @Singleton
    @Provides
    fun providePhotoAPI(): PhotoApi {
        return Retrofit.Builder()
            .baseUrl(Url.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create()
    }

    @Singleton
    @Provides
    fun providePhotoRepository(photoApi: PhotoApi): PhotoRepository {
        return PhotoRepositoryImpl(photoApi)
    }


    @Singleton
    @Provides
    fun provideAuthAPI(): AuthApi {
        return Retrofit.Builder()
            .baseUrl(Url.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create()
    }

    @Singleton
    @Provides
    fun provideAuthRepository(authApi: AuthApi): AuthRepository {
        return AuthRepositoryImpl(authApi)
    }

    @Singleton
    @Provides
    fun provideSession(@ApplicationContext context: Context): Session {
        return Session(context)
    }

}