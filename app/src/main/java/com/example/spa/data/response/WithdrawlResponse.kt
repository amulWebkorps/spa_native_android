package com.example.spa.data.response

data class WithdrawlResponse(
     val id: Int,
     val amount: Double,
     val status: String,
     val completionDate: String?, // Use a nullable String if it can be null
     val userId: Int,
     val createdAt: String,
    val updatedAt: String
)