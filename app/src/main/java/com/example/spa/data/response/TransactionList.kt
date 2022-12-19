package com.example.spa.data.response

data class TransactionList(
    val amount: Double,
    val bank_account_id: Int,
    val created_at: String,
    val description: String,
    val id: Int,
    val updated_at: String,
    val user_id: Int
)