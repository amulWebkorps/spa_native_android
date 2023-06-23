package com.example.spa.data.response

data class AddQRCodeResponse(
    val amount: Int,
    val created_at: String,
    val id: Int,
    val image_url: String,
    val updated_at: String,
    val url: String,
    val user_id: Int
)