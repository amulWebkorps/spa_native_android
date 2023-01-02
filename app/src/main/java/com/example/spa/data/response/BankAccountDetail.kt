package com.example.spa.data.response

data class BankAccountDetail(
    val account_holder_name: String ="",
    val account_number: String="",
    val account_type: String="",
    val address: Any="",
    val bank_name: String="",
    val created_at: String="",
    val frequency: String="",
    val id: Int=0,
    val ifsc_code: Any? = null ,
    var is_active: Boolean = false,
    val updated_at: String="",
    val user_id: Int = 0
)