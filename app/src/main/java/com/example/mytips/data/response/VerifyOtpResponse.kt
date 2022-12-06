package com.example.mytips.data.response

data class VerifyOtpResponse(
    val user: User,
    val message: String,
    val token: String,
)