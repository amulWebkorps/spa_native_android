package com.example.mytips.data.services

import com.example.mytips.data.remote.AuthApi
import com.example.mytips.data.remote.SettingsApi
import com.example.mytips.data.request.AddBankDetailRequest
import com.example.mytips.data.request.User
import com.example.mytips.data.response.*
import com.example.mytips.repo.auth.AuthRepository
import com.example.mytips.repo.settings.SettingsRepository
import com.example.mytips.utilities.Resource
import com.example.mytips.utilities.getErrorMessageResponse
import com.example.mytips.utilities.getErrorResponse
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
                    emit(Resource.Error(getErrorResponse(response.errorBody()).errors!!))
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
                    emit(Resource.Error(getErrorResponse(response.errorBody()).errors!!))
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