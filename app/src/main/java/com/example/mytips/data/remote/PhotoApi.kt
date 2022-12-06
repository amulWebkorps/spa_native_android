package com.example.mytips.data.remote

import com.example.mytips.data.response.Photo
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface PhotoApi {

    @GET("list")
    suspend fun getPhotosList(
        @Query("page") pageNumber: Int,
        @Query("limit") limitNumber: Int
    ): Response<List<Photo>>
}