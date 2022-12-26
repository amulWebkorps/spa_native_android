package com.example.spa.data.request

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize


@Parcelize
data class GraphRequest(

    @SerializedName("type")
    val type: String,

): Parcelable