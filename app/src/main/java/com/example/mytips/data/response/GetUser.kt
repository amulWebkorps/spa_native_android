package com.example.mytips.data.response

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize
import kotlinx.android.parcel.RawValue


@Parcelize
data class GetUser(

    @SerializedName("country_code")
    val country_code: String,

    @SerializedName("created_at")
    val created_at: String,

    @SerializedName("email")
    val email: String,

    @SerializedName("first_name")
    val first_name: String,

    @SerializedName("id")
    val id: Int,

    @SerializedName("is_mobile_verified")
    val is_mobile_verified: Boolean,

    @SerializedName("last_name")
    val last_name: String,

    @SerializedName("mobile_number")
    val mobile_number: String,

    @SerializedName("updated_at")
    val updated_at: String,

    @SerializedName("user_wallet")
    val user_wallet:@RawValue UserWallet
): Parcelable