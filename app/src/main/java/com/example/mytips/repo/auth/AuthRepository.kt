package com.example.mytips.repo.auth

import com.example.mytips.data.request.User
import com.example.mytips.data.response.*
import com.example.mytips.utilities.Resource
import kotlinx.coroutines.flow.Flow

interface AuthRepository {
    suspend fun registerUser(user: User) : Flow<Resource<GetUser>>
    suspend fun sendOtp(user: User) : Flow<Resource<SendOtpResponse>>
    suspend fun verifyOtp(user: User) : Flow<Resource<VerifyOtpResponse>>
    suspend fun loginUser(user: User) : Flow<Resource<Login>>
    suspend fun getUser(token:String) : Flow<Resource<GetUser>>
    suspend fun updateMobileNumber(user: User) : Flow<Resource<UpdateMobileNumber>>
    suspend fun updateUserDetails(token:String,user: User) : Flow<Resource<UpdateUser>>
}