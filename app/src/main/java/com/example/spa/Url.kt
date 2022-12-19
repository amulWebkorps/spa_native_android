package com.example.spa

object Url {
    const val BASE_URL = "http://122.168.199.5:3000/"

    const val USER = "/users"
    const val LOGIN = "$USER/login"
    const val REGISTER = "$USER/registration"
    const val USER_PROFILE = "$USER/profile"
    const val UPDATE_PHONE = "$USER/update_mobile_number"
    const val RESET_PASSWORD = "$USER/reset_password"
    const val TRANSACTION = "$USER/transactions"

    const val BANK_ACCOUNTS="/bank_accounts"
    const val DELETE_BANK_ACCOUNTS="/bank_accounts/"
    const val USER_UPDATE="$USER/update"
}