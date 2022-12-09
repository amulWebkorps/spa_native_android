package com.example.mytips.data.response

data class AddBankDetailsResponse(
    val account_holder_name: String,
    val account_number: String,
    val account_type: String,
    val address: Any,
    val bank_name: String,
    val created_at: String,
    val id: Int,
    val ifsc_code: Any,
    val is_active: Boolean,
    val updated_at: String,
    val user_id: Int
)