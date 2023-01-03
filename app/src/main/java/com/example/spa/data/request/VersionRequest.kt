package com.example.spa.data.request

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize


@Parcelize
data class VersionRequest(

    @SerializedName("device_type")
    val device_type: String,

    @SerializedName("version")
    val version: String
): Parcelable