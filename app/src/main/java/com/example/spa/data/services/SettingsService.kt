package com.example.spa.data.services

import com.example.spa.data.remote.SettingsApi
import com.example.spa.data.request.AddBankDetailRequest
import com.example.spa.data.response.*
import com.example.spa.repo.settings.SettingsRepository
import com.example.spa.utilities.Resource
import com.example.spa.utilities.getErrorResponseArray
import com.example.spa.utilities.getErrorResponseArray
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import java.io.IOException

class SettingsService constructor(
    private val settingsApi: SettingsApi
) : SettingsRepository {


    override suspend fun addBankDetail(
        token: String,
        bankDetailRequest: AddBankDetailRequest
    ): Flow<Resource<AddBankDetailsResponse>> {
        return flow {
            emit(Resource.Loading(true))

            try {
                val response = settingsApi.addBankAccount(token, bankDetailRequest)
                if (response.isSuccessful) {
                    emit(Resource.Loading(false))
                    emit(Resource.Success(response.body()))
                } else {
                    emit(Resource.Loading(false))
                    emit(Resource.Error(getErrorResponseArray(response.errorBody()).errors[0]!!.toString()))
                }
            } catch (e: IOException) {
                emit(Resource.Loading(false))
                emit(Resource.Error("Something went wrong"))
            } catch (e: HttpException) {
                emit(Resource.Loading(false))
                emit(Resource.Error("Something went wrong"))
            }
        }
    }

    override suspend fun bankAccountsList(token: String): Flow<Resource<BankAccountList>> {
        return flow {
            emit(Resource.Loading(true))
            try {
                val response = settingsApi.bankAccountsList(token)
                if (response.isSuccessful) {
                    emit(Resource.Loading(false))
                    emit(Resource.Success(response.body()))
                } else {
                    emit(Resource.Loading(false))
                    emit(Resource.Error(getErrorResponseArray(response.errorBody()).errors[0]!!.toString()))
                }
            } catch (e: IOException) {
                emit(Resource.Loading(false))
                emit(Resource.Error("Something went wrong"))
            } catch (e: HttpException) {
                emit(Resource.Loading(false))
                emit(Resource.Error("Something went wrong"))
            }
        }
    }

    override suspend fun deleteBankAccount(token:String,id: String): Flow<Resource<GeneralResponse>> {
        return flow {
            emit(Resource.Loading(true))
            try {
                val response = settingsApi.deleteBankAccount(token,id)
                if (response.isSuccessful) {
                    emit(Resource.Loading(false))
                    emit(Resource.Success(response.body()))
                } else {
                    emit(Resource.Loading(false))
                    emit(Resource.Error(getErrorResponseArray(response.errorBody()).errors[0]!!.toString()))

                }
            } catch (e: IOException) {
                emit(Resource.Loading(false))
                emit(Resource.Error("Something went wrong"))
            } catch (e: HttpException) {
                emit(Resource.Loading(false))
                emit(Resource.Error("Something went wrong"))
            }
        }
    }

    override suspend fun transactionList(token: String, page: Int): Flow<Resource<Transaction>> {
        return flow {
            emit(Resource.Loading(true))
            try {
                val response = settingsApi.resentTransaction(token,page)
                if (response.isSuccessful) {
                    emit(Resource.Loading(false))
                    emit(Resource.Success(response.body()))
                } else {
                    emit(Resource.Loading(false))
                    emit(Resource.Error(getErrorResponseArray(response.errorBody()).errors[0]!!.toString()))

                }
            } catch (e: IOException) {
                emit(Resource.Loading(false))
                emit(Resource.Error("Something went wrong"))
            } catch (e: HttpException) {
                emit(Resource.Loading(false))
                emit(Resource.Error("Something went wrong"))
            }
        }
    }

}