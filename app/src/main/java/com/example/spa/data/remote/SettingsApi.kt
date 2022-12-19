package com.example.spa.data.remote

import com.example.spa.Url
import com.example.spa.data.request.AddBankDetailRequest
import com.example.spa.data.response.*
import com.example.spa.utilities.Constants
import retrofit2.Response
import retrofit2.http.*

interface SettingsApi {

    @POST(Url.BANK_ACCOUNTS)
    suspend fun addBankAccount(@Header("Authorization")token:String , @Body bankDetailRequest: AddBankDetailRequest): Response<AddBankDetailsResponse>

    @GET(Url.BANK_ACCOUNTS)
    suspend fun bankAccountsList(@Header("Authorization")token:String ): Response<BankAccountList>

    @DELETE(Url.DELETE_BANK_ACCOUNTS+"{${Constants.ID}}")
    suspend fun deleteBankAccount(@Header("Authorization")token:String,@Path(
        Constants.ID
    )id :String): Response<GeneralResponse>

    @GET(Url.TRANSACTION)
    suspend fun resentTransaction(@Header("Authorization")token:String ,@Query("page_number") page:Int): Response<Transaction>

}