package com.example.mytips.repo

import com.example.mytips.data.response.Photo
import com.example.mytips.utilities.Resource
import kotlinx.coroutines.flow.Flow

interface PhotoRepository {
    suspend fun getPhotoList(pageNumber: Int): Flow<Resource<List<Photo>>>
}