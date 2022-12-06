package com.example.mytips.data.repository

import com.example.mytips.data.remote.AuthApi
import com.example.mytips.data.remote.PhotoApi
import com.example.mytips.data.request.RegisterRequest
import com.example.mytips.data.request.User
import com.example.mytips.data.response.*
import com.example.mytips.repo.PhotoRepository
import com.example.mytips.repo.auth.AuthRepository
import com.example.mytips.utilities.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import retrofit2.Response
import java.io.IOException

class AuthRepositoryImpl constructor(
    private val authApi: AuthApi
) : AuthRepository {

    override suspend fun registerUser(registerRequest: RegisterRequest): Flow<Resource<RegisterResponse>> {
        return flow {
            emit(Resource.Loading(true))

            try {
                val response = authApi.registerUser(registerRequest)
                if (response.isSuccessful) {
                    emit(Resource.Loading(false))
                    emit(Resource.Success(response.body()))
                } else {
                    emit(Resource.Loading(false))
                    emit(Resource.Error(response.message()))
                }
            } catch (e: IOException) {
                emit(Resource.Loading(false))
                emit(Resource.Error("Couldn't load data"))
            } catch (e: HttpException) {
                emit(Resource.Loading(false))
                emit(Resource.Error("Couldn't load data"))
            }
        }
    }

    override suspend fun sendOtp(user: User): Flow<Resource<SendOtpResponse>> {
        return flow {
            emit(Resource.Loading(true))

            try {
                val response = authApi.sendOtp(user)
                if (response.isSuccessful) {
                    emit(Resource.Loading(false))
                    emit(Resource.Success(response.body()))
                } else {
                    emit(Resource.Loading(false))
                    emit(Resource.Error(response.message()))
                }
            } catch (e: IOException) {
                emit(Resource.Loading(false))
                emit(Resource.Error("Couldn't load data"))
            } catch (e: HttpException) {
                emit(Resource.Loading(false))
                emit(Resource.Error("Couldn't load data"))
            }
        }

    }

    override suspend fun verifyOtp(user: User): Flow<Resource<VerifyOtpResponse>> {
        return flow {
            emit(Resource.Loading(true))

            try {
                val response = authApi.verifyOtp(user)
                if (response.isSuccessful) {
                    emit(Resource.Loading(false))
                    emit(Resource.Success(response.body()))
                } else {
                    emit(Resource.Loading(false))
                    emit(Resource.Error(response.message()))
                }
            } catch (e: IOException) {
                emit(Resource.Loading(false))
                emit(Resource.Error("Couldn't load data"))
            } catch (e: HttpException) {
                emit(Resource.Loading(false))
                emit(Resource.Error("Couldn't load data"))
            }
        }

    }

    override suspend fun loginUser(user: User): Flow<Resource<Login>> {
        return flow {
            emit(Resource.Loading(true))

            try {
                val response = authApi.login(user)
                if (response.isSuccessful) {
                    emit(Resource.Loading(false))
                    emit(Resource.Success(response.body()))
                } else {
                    emit(Resource.Loading(false))
                    emit(Resource.Error(response.message()))
                }
            } catch (e: IOException) {
                emit(Resource.Loading(false))
                emit(Resource.Error("Couldn't load data"))
            } catch (e: HttpException) {
                emit(Resource.Loading(false))
                emit(Resource.Error("Couldn't load data"))
            }
        }
    }
}