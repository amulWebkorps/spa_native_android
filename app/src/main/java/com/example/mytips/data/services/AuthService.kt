package com.example.mytips.data.services

import com.example.mytips.data.remote.AuthApi
import com.example.mytips.data.request.User
import com.example.mytips.data.response.*
import com.example.mytips.repo.auth.AuthRepository
import com.example.mytips.utilities.Resource
import com.example.mytips.utilities.getErrorMessageResponse
import com.example.mytips.utilities.getErrorResponse
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
                    emit(Resource.Error(getErrorResponse(response.errorBody()).errors!!))
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
                    emit(Resource.Error(getErrorResponse(response.errorBody()).errors!!))
                }
            } catch (e: IOException) {
                emit(Resource.Loading(false))
                emit(Resource.Error("Something went wrong"))
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
                    emit(Resource.Error(getErrorResponse(response.errorBody()).errors!!))
                }
            } catch (e: IOException) {
                emit(Resource.Loading(false))
                emit(Resource.Error("Something went wrong"))
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
                    emit(Resource.Error(getErrorMessageResponse(response.errorBody()).errors!!))
                }
            } catch (e: IOException) {
                emit(Resource.Loading(false))
                emit(Resource.Error("Something went wrong"))
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
                    emit(Resource.Error(getErrorMessageResponse(response.errorBody()).errors!!))
                }
            } catch (e: IOException) {
                emit(Resource.Loading(false))
                emit(Resource.Error("Something went wrong"))
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
                    emit(Resource.Error(getErrorMessageResponse(response.errorBody()).errors.toString()!!))
                }
            } catch (e: IOException) {
                emit(Resource.Loading(false))
                emit(Resource.Error("Something went wrong"))
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
                    emit(Resource.Error(getErrorMessageResponse(response.errorBody()).errors.toString()!!))
                }
            } catch (e: IOException) {
                emit(Resource.Loading(false))
                emit(Resource.Error("Something went wrong"))
            } catch (e: HttpException) {
                emit(Resource.Loading(false))
                emit(Resource.Error("Something went wrong"))
            }
        }
    }
}