package com.example.spa.repo.settings

import com.example.spa.data.request.AddBankDetailRequest
import com.example.spa.data.request.AddQRCodeRequest
import com.example.spa.data.request.GraphRequest
import com.example.spa.data.response.*
import com.example.spa.utilities.Resource
import kotlinx.coroutines.flow.Flow

interface SettingsRepository {
    suspend fun addBankDetail(token:String,bankDetailRequest: AddBankDetailRequest) : Flow<Resource<AddBankDetailsResponse>>
    suspend fun bankAccountsList(token:String) : Flow<Resource<BankAccountList>>
    suspend fun deleteBankAccount(token:String,id:String) : Flow<Resource<GeneralResponse>>
    suspend fun transactionList(token:String,page:Int) : Flow<Resource<Transaction>>
    suspend fun graphData(token:String,type:String) : Flow<Resource<GraphResponse>>
    suspend fun getQRCodes(token:String,bankDetailRequest: AddQRCodeRequest) : Flow<Resource<AddQRCodeResponse>>
}