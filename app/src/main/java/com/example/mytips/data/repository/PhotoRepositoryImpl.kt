package com.example.mytips.data.repository

import com.example.mytips.data.remote.PhotoApi
import com.example.mytips.data.response.Photo
import com.example.mytips.repo.PhotoRepository
import com.example.mytips.utilities.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import java.io.IOException

class PhotoRepositoryImpl constructor(
    private val photoApi: PhotoApi
) : PhotoRepository {

    override suspend fun getPhotoList(pageNumber: Int): Flow<Resource<List<Photo>>> {
        return flow {
            emit(Resource.Loading(true))

            try {
                val response = photoApi.getPhotosList(pageNumber, 10)
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