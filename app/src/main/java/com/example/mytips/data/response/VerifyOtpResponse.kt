package com.example.mytips.data.response

data class VerifyOtpResponse(
    val user: GetUser,
    val message: String,
    val token: String,
)