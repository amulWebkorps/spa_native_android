package com.example.mytips.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.mytips.data.response.Photo
import com.example.mytips.repo.PhotoRepository
import com.example.mytips.utilities.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PhotoListViewModel @Inject constructor(
    private val photoRepository: PhotoRepository
) : ViewModel() {

    private val _photoList = MutableSharedFlow<Resource<List<Photo>>>()
    val photoList = _photoList.asSharedFlow()

    private var pageNumber2 = 0

    init {
        getPhotoList(pageNumber2)
    }

    fun getPhotoList(pageNumber: Int) = viewModelScope.launch {
        photoRepository.getPhotoList(pageNumber2).collect { result ->
            when (result) {
                is Resource.Error -> {
                    result.message?.let {
                        _photoList.emit(Resource.Error(result.message))
                    }
                }
                is Resource.Loading -> {
                    _photoList.emit(Resource.Loading(result.isLoading))
                }
                is Resource.Success -> {
                    _photoList.emit(Resource.Success(result.data))
                }
            }
        }
    }
}

