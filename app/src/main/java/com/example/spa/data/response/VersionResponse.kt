package com.example.spa.data.response

data class VersionResponse(
    val isForceUpdate: Boolean = false,
    val isSoftUpdate: Boolean = false,
    val message: String
)