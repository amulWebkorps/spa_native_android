package com.example.mytips.data.remote

import android.util.Log
import com.example.mytips.data.request.RegisterRequest
import com.example.mytips.data.request.User
import com.example.mytips.data.response.*
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface AuthApi {

    @POST("/users/registration")
    suspend fun registerUser(@Body registerRequest: RegisterRequest): Response<RegisterResponse>


    @POST("/users/generate_otp")
    suspend fun sendOtp(@Body user: User): Response<SendOtpResponse>


    @POST("/users/validate_otp")
    suspend fun verifyOtp(@Body user: User): Response<VerifyOtpResponse>


    @POST("/users/login")
    suspend fun login(@Body user: User): Response<Login>

}