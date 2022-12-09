package com.example.mytips.data.remote

import com.example.mytips.Url
import com.example.mytips.data.request.AddBankDetailRequest
import com.example.mytips.data.response.*
import retrofit2.Response
import retrofit2.http.*

interface SettingsApi {

    @POST(Url.BANK_ACCOUNTS)
    suspend fun addBankAccount(@Header("Authorization")token:String , @Body bankDetailRequest: AddBankDetailRequest): Response<AddBankDetailsResponse>

    @GET(Url.BANK_ACCOUNTS)
    suspend fun bankAccountsList(@Header("Authorization")token:String ): Response<BankAccountList>



}