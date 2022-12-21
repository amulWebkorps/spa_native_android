package com.example.spa.data.remote

import com.example.spa.Url
import com.example.spa.data.request.User
import com.example.spa.data.response.*
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.*

interface RegisterApi {


    @Multipart
    @POST(Url.REGISTER)
     fun registerUserImage(@Part("email") email:RequestBody,
                                  @Part("first_name") first_name:RequestBody,
                                  @Part("last_name") last_name:RequestBody,
                                  @Part("mobile_number") mobile_number:RequestBody,
                                  @Part("country_code") country_code:RequestBody,
                                  @Part("password") password:RequestBody,
                                  @Part("password_confirmation") password_confirmation:RequestBody,
                                  @Part image:MultipartBody.Part,
                                  ): Response<GetUser>



}