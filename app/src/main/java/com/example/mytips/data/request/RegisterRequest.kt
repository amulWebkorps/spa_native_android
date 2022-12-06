package com.example.mytips.data.request

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class RegisterRequest(
    @SerializedName("user")
    val user: User,
): Parcelable