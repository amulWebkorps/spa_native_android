package com.example.spa

object Url {
    const val BASE_URL = "http://125.99.200.202:5000/"

    const val USER = "/users"
    const val V2_USER = "api/v2/users"
    const val LOGIN = "$USER/login"
    const val REGISTER = "$USER/registration"
    const val V2_REGISTER = "$V2_USER/registration"
    const val USER_PROFILE = "$USER/profile"
    const val UPDATE_PHONE = "$USER/update_mobile_number"
    const val RESET_PASSWORD = "$USER/reset_password"
    const val TRANSACTION = "$USER/transactions"
    const val VERSION_MANAGER = "/version_manager"

    const val BANK_ACCOUNTS="/bank_accounts"
    const val DELETE_BANK_ACCOUNTS="/bank_accounts/"
    const val USER_UPDATE="$USER/update"
    const val GRAPH_DATA="$USER/graph_data"
    const val V2_USER_UPDATE = "$V2_USER/update"
}