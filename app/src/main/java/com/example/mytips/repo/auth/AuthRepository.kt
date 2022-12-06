package com.example.mytips.repo.auth

import com.example.mytips.data.request.RegisterRequest
import com.example.mytips.data.request.User
import com.example.mytips.data.response.Login
import com.example.mytips.data.response.RegisterResponse
import com.example.mytips.data.response.SendOtpResponse
import com.example.mytips.data.response.VerifyOtpResponse
import com.example.mytips.utilities.Resource
import kotlinx.coroutines.flow.Flow
import retrofit2.Response

interface AuthRepository {
    suspend fun registerUser(registerRequest: RegisterRequest) : Flow<Resource<RegisterResponse>>
    suspend fun sendOtp(user: User) : Flow<Resource<SendOtpResponse>>
    suspend fun verifyOtp(user: User) : Flow<Resource<VerifyOtpResponse>>
    suspend fun loginUser(user: User) : Flow<Resource<Login>>
}