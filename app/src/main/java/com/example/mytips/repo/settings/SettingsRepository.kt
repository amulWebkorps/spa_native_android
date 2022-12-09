package com.example.mytips.repo.settings

import com.example.mytips.data.request.AddBankDetailRequest
import com.example.mytips.data.request.User
import com.example.mytips.data.response.*
import com.example.mytips.utilities.Resource
import kotlinx.coroutines.flow.Flow

interface SettingsRepository {
    suspend fun addBankDetail(token:String,bankDetailRequest: AddBankDetailRequest) : Flow<Resource<AddBankDetailsResponse>>
    suspend fun bankAccountsList(token:String) : Flow<Resource<BankAccountList>>
}