package com.example.mytips.data.response


import com.google.gson.annotations.SerializedName

data class Photo(
    @SerializedName("author")
    val author: String? = "",
    @SerializedName("download_url")
    val downloadUrl: String? = "",
    @SerializedName("height")
    val height: Int? = 0,
    @SerializedName("id")
    val id: String? = "",
    @SerializedName("url")
    val url: String? = "",
    @SerializedName("width")
    val width: Int? = 0
)