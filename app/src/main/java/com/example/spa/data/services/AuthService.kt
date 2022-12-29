package com.example.spa.data.services

import android.content.Context
import android.provider.Settings.Global.getString
import android.util.Log
import com.example.spa.R
import com.example.spa.data.remote.AuthApi
import com.example.spa.data.request.User
import com.example.spa.data.response.*
import com.example.spa.repo.auth.AuthRepository
import com.example.spa.utilities.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import java.io.IOException

class AuthService constructor(
    private val authApi: AuthApi
) : AuthRepository {

    override suspend fun registerUser(user: User): Flow<Resource<GetUser>> {
        return flow {
            emit(Resource.Loading(true))
            try {
                val response = authApi.registerUser(user)
                if (response.isSuccessful) {
                    emit(Resource.Loading(false))
                    emit(Resource.Success(response.body()))
                } else {
                    emit(Resource.Loading(false))
                    emit(Resource.Error(getErrorResponseArray(response.errorBody()).errors[0].toString()!!))
                }
            } catch (e: IOException) {
                emit(Resource.Loading(false))
                emit(Resource.Error("Please check your internet connectivity"))
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
                        emit(Resource.Error(getErrorResponseArray(response.errorBody()).errors[0].toString()!!))
                }
            } catch (e: IOException) {
                emit(Resource.Loading(false))
                emit(Resource.Error("Please check your internet connectivity"))
            } catch (e: HttpException) {
                emit(Resource.Loading(false))
                emit(Resource.Error("Something went wrong"))
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
                        emit(Resource.Error(getErrorResponseArray(response.errorBody()).errors[0].toString()!!))
                }
            } catch (e: IOException) {
                emit(Resource.Loading(false))
                emit(Resource.Error("Please check your internet connectivity"))
            } catch (e: HttpException) {
                emit(Resource.Loading(false))
                emit(Resource.Error("Something went wrong"))
            }
        }

    }

    override suspend fun loginUser(user: User): Flow<Resource<Login>> {
        return flow {
            emit(Resource.Loading(true))
            try {
                val response = authApi.login(user)
                if (response.isSuccessful && response!=null) {
                    emit(Resource.Loading(false))
                    emit(Resource.Success(response.body()))
                } else {
                    emit(Resource.Loading(false))
                        emit(Resource.Error(getErrorResponseArray(response.errorBody()).errors[0].toString()!!))
                }
            } catch (e: IOException) {
                emit(Resource.Loading(false))
                emit(Resource.Error("Please check your internet connectivity"))
            } catch (e: HttpException) {
                emit(Resource.Loading(false))
                emit(Resource.Error("Something went wrong"))
            }
        }
    }

    override suspend fun getUser(token: String): Flow<Resource<GetUser>> {
        return flow {
            emit(Resource.Loading(true))
            try {
                val response = authApi.getUser(token)
                if (response.isSuccessful && response!=null) {
                    emit(Resource.Loading(false))
                    emit(Resource.Success(response.body()))
                } else {
                    emit(Resource.Loading(false))
                        emit(Resource.Error(getErrorResponseArray(response.errorBody()).errors[0].toString()!!))
                }
            } catch (e: IOException) {
                emit(Resource.Loading(false))
                emit(Resource.Error("Please check your internet connectivity"))
            } catch (e: HttpException) {
                emit(Resource.Loading(false))
                emit(Resource.Error("Something went wrong"))
            }
        }
    }

    override suspend fun updateMobileNumber(user: User): Flow<Resource<UpdateMobileNumber>> {
        return flow {
            emit(Resource.Loading(true))
            try {
                val response = authApi.updateMobileNumber(user)
                if (response.isSuccessful && response!=null) {
                    emit(Resource.Loading(false))
                    emit(Resource.Success(response.body()))
                } else {
                    emit(Resource.Loading(false))
                        emit(Resource.Error(getErrorResponseArray(response.errorBody()).errors[0].toString()!!))
                }
            } catch (e: IOException) {
                emit(Resource.Loading(false))
                emit(Resource.Error("Please check your internet connectivity"))
            } catch (e: HttpException) {
                emit(Resource.Loading(false))
                emit(Resource.Error("Something went wrong"))
            }
        }
    }

    override suspend fun updateUserDetails(token: String,user: User): Flow<Resource<UpdateUser>> {
        return flow {
            emit(Resource.Loading(true))
            try {
                val response = authApi.updateUserDetails(token,user)
                if (response.isSuccessful && response!=null) {
                    emit(Resource.Loading(false))
                    emit(Resource.Success(response.body()))
                } else {
                    emit(Resource.Loading(false))
                    emit(Resource.Error(getErrorResponseArray(response.errorBody()).errors[0].toString()!!))
                }
            } catch (e: IOException) {
                emit(Resource.Loading(false))
                emit(Resource.Error("Please check your internet connectivity"))
            } catch (e: HttpException) {
                emit(Resource.Loading(false))
                emit(Resource.Error("Something went wrong"))
            }
        }
    }

    override suspend fun resetPassword(token: String, user: User): Flow<Resource<ResetPassword>> {
        return flow {
            emit(Resource.Loading(true))
            try {
                val response = authApi.resetPassword(token, user)
                if (response.isSuccessful && response != null) {
                    emit(Resource.Loading(false))
                    emit(Resource.Success(response.body()))
                } else {
                    emit(Resource.Loading(false))
                        emit(Resource.Error(getErrorResponseArray(response.errorBody()).errors[0].toString()!!))
                }
            } catch (e: IOException) {
                emit(Resource.Loading(false))
                emit(Resource.Error("Please check your internet connectivity"))
            } catch (e: HttpException) {
                emit(Resource.Loading(false))
                emit(Resource.Error("Something went wrong"))
            }
        }
    }
}