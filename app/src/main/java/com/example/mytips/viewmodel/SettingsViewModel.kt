package com.example.mytips.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mytips.data.request.AddBankDetailRequest
import com.example.mytips.data.request.User
import com.example.mytips.data.response.*
import com.example.mytips.repo.auth.AuthRepository
import com.example.mytips.repo.settings.SettingsRepository
import com.example.mytips.utilities.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val settingsRepository: SettingsRepository
) : ViewModel() {

    private val _addBankDetails = MutableSharedFlow<Resource<AddBankDetailsResponse>>()
    val addBankDetails = _addBankDetails.asSharedFlow()

    private val _bankAccountsList = MutableSharedFlow<Resource<BankAccountList>>()
    val bankAccountList = _bankAccountsList.asSharedFlow()


    fun addBankDetail(token:String,addBankDetailRequest: AddBankDetailRequest){
        viewModelScope.launch {
            settingsRepository.addBankDetail(token,addBankDetailRequest).collect { result ->
                when (result) {
                    is Resource.Error -> {
                        result.message?.let {
                            _addBankDetails.emit(Resource.Error(result.message))
                        }
                    }
                    is Resource.Loading -> {
                        _addBankDetails.emit(Resource.Loading(result.isLoading))
                    }
                    is Resource.Success -> {
                        _addBankDetails.emit(Resource.Success(result.data))
                    }
                }
                Log.e("TAG", "add bank detail: $result")
            }
        }
    }

    fun bankAccountsList(token:String){
        viewModelScope.launch {
            settingsRepository.bankAccountsList(token).collect { result ->
                when (result) {
                    is Resource.Error -> {
                        result.message?.let {
                            _bankAccountsList.emit(Resource.Error(result.message))
                        }
                    }
                    is Resource.Loading -> {
                        _bankAccountsList.emit(Resource.Loading(result.isLoading))
                    }
                    is Resource.Success -> {
                        _bankAccountsList.emit(Resource.Success(result.data))
                    }
                }
                Log.e("TAG", "bank list: $result")
            }
        }
    }


}

