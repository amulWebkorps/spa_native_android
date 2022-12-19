package com.example.spa.data.remote

import com.example.spa.Url
import com.example.spa.data.request.User
import com.example.spa.data.response.*
import retrofit2.Response
import retrofit2.http.*

interface AuthApi {

    @POST(Url.REGISTER)
    suspend fun registerUser(@Body user: User): Response<GetUser>


    @POST("/users/generate_otp")
    suspend fun sendOtp(@Body user: User): Response<SendOtpResponse>


    @POST("/users/validate_otp")
    suspend fun verifyOtp(@Body user: User): Response<VerifyOtpResponse>


    @POST(Url.LOGIN)
    suspend fun login(@Body user: User): Response<Login>


    @GET(Url.USER_PROFILE)
    suspend fun getUser(@Header("Authorization")token:String): Response<GetUser>

    @PATCH(Url.UPDATE_PHONE)
    suspend fun updateMobileNumber(@Body user: User): Response<UpdateMobileNumber>

    @PUT(Url.USER_UPDATE)
    suspend fun updateUserDetails(@Header("Authorization")token:String,@Body user: User): Response<UpdateUser>

    @PATCH(Url.RESET_PASSWORD)
    suspend fun resetPassword(@Header("Authorization")token:String,@Body user: User): Response<ResetPassword>

}