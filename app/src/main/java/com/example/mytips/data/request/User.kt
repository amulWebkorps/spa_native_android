package com.example.mytips.data.request

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize


@Parcelize
data class User(
    @SerializedName("country_code")
    val country_code: String?=null,

    @SerializedName("email")
    val email: String?=null,

    @SerializedName("first_name")
    val first_name: String?=null,

    @SerializedName("last_name")
    val last_name: String?=null,

    @SerializedName("mobile_number")
    val mobile_number: String?=null,

    @SerializedName("password")
    val password: String?=null,

    @SerializedName("password_confirmation")
    val password_confirmation: String?=null,

    @SerializedName("otp_code")
    val otp_code: String?=null,

): Parcelable