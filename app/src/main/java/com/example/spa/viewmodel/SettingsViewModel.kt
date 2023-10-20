package com.example.spa.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.spa.data.request.AddBankDetailRequest
import com.example.spa.data.request.AddQRCodeRequest
import com.example.spa.data.request.GraphRequest
import com.example.spa.data.request.WithdrawlRequest
import com.example.spa.data.response.*
import com.example.spa.repo.settings.SettingsRepository
import com.example.spa.utilities.Resource
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


    private val _deleteBankAccount = MutableSharedFlow<Resource<GeneralResponse>>()
    val deleteBankAccount = _deleteBankAccount.asSharedFlow()

    private val _transaction = MutableSharedFlow<Resource<Transaction>>()
    val transaction = _transaction.asSharedFlow()

    private val _graphData = MutableSharedFlow<Resource<GraphResponse>>()
    val graphData = _graphData.asSharedFlow()

    private val _getQRCodes = MutableSharedFlow<Resource<AddQRCodeResponse>>()
    val getQRCodes = _getQRCodes.asSharedFlow()

    private val _getWithdraw = MutableSharedFlow<Resource<WithdrawlResponse>>()
    val getWithdraw = _getWithdraw.asSharedFlow()

    private val _getWallet = MutableSharedFlow<Resource<WalletAmountResponse>>()
    val getWallet = _getWallet.asSharedFlow()


    fun addBankDetail(token: String, addBankDetailRequest: AddBankDetailRequest) {
        viewModelScope.launch {
            settingsRepository.addBankDetail(token, addBankDetailRequest).collect { result ->
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

    fun bankAccountsList(token: String) {
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

    fun deleteBankAccount(token: String, id: String) {
        viewModelScope.launch {
            settingsRepository.deleteBankAccount(token, id).collect { result ->
                when (result) {
                    is Resource.Error -> {
                        result.message?.let {
                            _deleteBankAccount.emit(Resource.Error(result.message))
                        }
                    }

                    is Resource.Loading -> {
                        _deleteBankAccount.emit(Resource.Loading(result.isLoading))
                    }

                    is Resource.Success -> {
                        _deleteBankAccount.emit(Resource.Success(result.data))
                    }
                }
                Log.e("TAG", "delete bank: $result")
            }
        }
    }


    fun transactionList(token: String, page: Int) {
        viewModelScope.launch {
            settingsRepository.transactionList(token, page).collect { result ->
                when (result) {
                    is Resource.Error -> {
                        result.message?.let {
                            _transaction.emit(Resource.Error(result.message, code = result.code))
                        }
                    }

                    is Resource.Loading -> {
                        _transaction.emit(Resource.Loading(result.isLoading))
                    }

                    is Resource.Success -> {
                        _transaction.emit(Resource.Success(result.data))
                    }
                }
                Log.e("TAG", "_transaction: $result")
            }
        }
    }


    fun graphDataResponse(token: String, type: String) {
        viewModelScope.launch {
            settingsRepository.graphData(token, type).collect { result ->
                when (result) {
                    is Resource.Error -> {
                        result.message?.let {
                            _graphData.emit(Resource.Error(result.message,code=result.code))
                        }
                    }

                    is Resource.Loading -> {
                        _graphData.emit(Resource.Loading(result.isLoading))
                    }

                    is Resource.Success -> {
                        _graphData.emit(Resource.Success(result.data))
                    }
                }
                Log.e("TAG", "_graphData: $result")
            }
        }
    }

    fun getQRCodes(token: String, addBankDetailRequest: AddQRCodeRequest) {
        viewModelScope.launch {
            settingsRepository.getQRCodes(token, addBankDetailRequest).collect { result ->
                when (result) {
                    is Resource.Error -> {
                        result.message?.let {
                            _getQRCodes.emit(Resource.Error(result.message))
                        }
                    }

                    is Resource.Loading -> {
                        _getQRCodes.emit(Resource.Loading(result.isLoading))
                    }

                    is Resource.Success -> {
                        _getQRCodes.emit(Resource.Success(result.data))
                    }
                }
                Log.e("TAG", "add bank detail: $result")
            }
        }
    }

    fun getWithdrawRequest(token: String, withdrawlRequest: WithdrawlRequest) {
        viewModelScope.launch {
            settingsRepository.getWithdraw(token, withdrawlRequest).collect { result ->
                when (result) {
                    is Resource.Error -> {
                        result.message?.let {
                            _getWithdraw.emit(Resource.Error(result.message))
                        }
                    }

                    is Resource.Loading -> {
                        _getWithdraw.emit(Resource.Loading(result.isLoading))
                    }

                    is Resource.Success -> {
                        _getWithdraw.emit(Resource.Success(result.data))
                    }
                }
                Log.e("TAG", "add bank detail: $result")
            }
        }
    }

    fun wallet(token: String) {
        viewModelScope.launch {
            settingsRepository.wallet(token).collect { result ->
                when (result) {
                    is Resource.Error -> {
                        result.message?.let {
                            _getWallet.emit(Resource.Error(result.message))
                        }
                    }

                    is Resource.Loading -> {
                        _getWallet.emit(Resource.Loading(result.isLoading))
                    }

                    is Resource.Success -> {
                        _getWallet.emit(Resource.Success(result.data))
                    }
                }
                Log.e("TAG", "add bank detail: $result")
            }
        }
    }


}

