package com.example.spa.data.response

data class VerifyOtpResponse(
    val user: GetUser,
    val message: String,
    val token: String,
)