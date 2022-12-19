package com.example.spa.data.response

data class Transaction(
    val list: List<TransactionList>,
    val page_number: Int
)