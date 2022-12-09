package com.example.mytips.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mytips.data.request.User
import com.example.mytips.data.response.*
import com.example.mytips.repo.auth.AuthRepository
import com.example.mytips.utilities.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val authRepository: AuthRepository
) : ViewModel() {

    private val _register = MutableSharedFlow<Resource<GetUser>>()
    val register = _register.asSharedFlow()

    private val _sendOtp = MutableSharedFlow<Resource<SendOtpResponse>>()
    val sendOtp = _sendOtp.asSharedFlow()

    private val _verifyOtp = MutableSharedFlow<Resource<VerifyOtpResponse>>()
    val verifyOtp = _verifyOtp.asSharedFlow()


    private val _loginUser = MutableSharedFlow<Resource<Login>>()
    val loginUser = _loginUser.asSharedFlow()

    private val _getUser = MutableSharedFlow<Resource<GetUser>>()
    val getUser = _getUser.asSharedFlow()

    private val _updateMobileNumber = MutableSharedFlow<Resource<UpdateMobileNumber>>()
    val updateMobileNumber = _updateMobileNumber.asSharedFlow()

    private val _updateUserDetail = MutableSharedFlow<Resource<UpdateUser>>()
    val updateUserDetail = _updateUserDetail.asSharedFlow()


    fun getRegister(user: User){
        viewModelScope.launch {
            authRepository.registerUser(user).collect { result ->
                when (result) {
                    is Resource.Error -> {
                        result.message?.let {
                            _register.emit(Resource.Error(result.message))
                        }
                    }
                    is Resource.Loading -> {
                        _register.emit(Resource.Loading(result.isLoading))
                    }
                    is Resource.Success -> {
                        _register.emit(Resource.Success(result.data))
                    }
                }
                Log.e("TAG", "getRegister: $result")
            }
        }
    }


    fun sendOtp(user: User){
        viewModelScope.launch {
            authRepository.sendOtp(user).collect { result ->
                when (result) {
                    is Resource.Error -> {
                        result.message?.let {
                            _sendOtp.emit(Resource.Error(result.message))
                        }
                    }
                    is Resource.Loading -> {
                        _sendOtp.emit(Resource.Loading(result.isLoading))
                    }
                    is Resource.Success -> {
                        _sendOtp.emit(Resource.Success(result.data))
                    }
                }
                Log.e("TAG", "sendOtp: $result")
            }
        }
    }


    fun verifyOtp(user: User){
        viewModelScope.launch {
            authRepository.verifyOtp(user).collect { result ->
                when (result) {
                    is Resource.Error -> {
                        result.message?.let {
                            _verifyOtp.emit(Resource.Error(result.message))
                        }
                    }
                    is Resource.Loading -> {
                        _verifyOtp.emit(Resource.Loading(result.isLoading))
                    }
                    is Resource.Success -> {
                        _verifyOtp.emit(Resource.Success(result.data))
                    }
                }
                Log.e("TAG", "verifyOtp: $result")
            }
        }
    }

    fun loginUser(user: User){
        viewModelScope.launch {
            authRepository.loginUser(user).collect { result ->
                when (result) {
                    is Resource.Error -> {
                        result.message?.let {
                            _loginUser.emit(Resource.Error(result.message))
                        }
                    }
                    is Resource.Loading -> {
                        _loginUser.emit(Resource.Loading(result.isLoading))
                    }
                    is Resource.Success -> {
                        _loginUser.emit(Resource.Success(result.data))
                    }
                }
                Log.e("TAG", "login: $result")
            }
        }
    }

    fun getUser(token:String){
        viewModelScope.launch {
            authRepository.getUser(token).collect { result ->
                when (result) {
                    is Resource.Error -> {
                        result.message?.let {
                            _getUser.emit(Resource.Error(result.message))
                        }
                    }
                    is Resource.Loading -> {
                        _getUser.emit(Resource.Loading(result.isLoading))
                    }
                    is Resource.Success -> {
                        _getUser.emit(Resource.Success(result.data))
                    }
                }
                Log.e("TAG", "getUser: $result")
            }
        }
    }
    fun updateMobileNumber(user: User){
        viewModelScope.launch {
            authRepository.updateMobileNumber(user).collect { result ->
                when (result) {
                    is Resource.Error -> {
                        result.message?.let {
                            _updateMobileNumber.emit(Resource.Error(result.message))
                        }
                    }
                    is Resource.Loading -> {
                        _updateMobileNumber.emit(Resource.Loading(result.isLoading))
                    }
                    is Resource.Success -> {
                        _updateMobileNumber.emit(Resource.Success(result.data))
                    }
                }
                Log.e("TAG", "updateMobileNumber: $result")
            }
        }
    }

    fun updateUserDetail(token:String,user: User){
        viewModelScope.launch {
            authRepository.updateUserDetails(token,user).collect { result ->
                when (result) {
                    is Resource.Error -> {
                        result.message?.let {
                            _updateUserDetail.emit(Resource.Error(result.message))
                        }
                    }
                    is Resource.Loading -> {
                        _updateUserDetail.emit(Resource.Loading(result.isLoading))
                    }
                    is Resource.Success -> {
                        _updateUserDetail.emit(Resource.Success(result.data))
                    }
                }
                Log.e("TAG", "updateMobileNumber: $result")
            }
        }
    }
}

