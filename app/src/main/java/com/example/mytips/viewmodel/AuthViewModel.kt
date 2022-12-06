package com.example.mytips.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.mytips.data.request.RegisterRequest
import com.example.mytips.data.request.User
import com.example.mytips.data.response.*
import com.example.mytips.repo.PhotoRepository
import com.example.mytips.repo.auth.AuthRepository
import com.example.mytips.utilities.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.math.log

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val authRepository: AuthRepository
) : ViewModel() {

    private val _register = MutableSharedFlow<Resource<RegisterResponse>>()
    val register = _register.asSharedFlow()

    private val _sendOtp = MutableSharedFlow<Resource<SendOtpResponse>>()
    val sendOtp = _sendOtp.asSharedFlow()

    private val _verifyOtp = MutableSharedFlow<Resource<VerifyOtpResponse>>()
    val verifyOtp = _verifyOtp.asSharedFlow()


    private val _loginUser = MutableSharedFlow<Resource<Login>>()
    val loginUser = _loginUser.asSharedFlow()


    fun getRegister(registerRequest: RegisterRequest){
        viewModelScope.launch {
            authRepository.registerUser(registerRequest).collect { result ->
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
                Log.e("TAG", "getRegister: $result")
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
                Log.e("TAG", "getRegister: $result")
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
                Log.e("TAG", "getRegister: $result")
            }
        }
    }
}

