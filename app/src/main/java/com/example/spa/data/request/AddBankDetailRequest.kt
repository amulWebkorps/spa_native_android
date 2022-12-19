package com.example.spa.data.request

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize


@Parcelize
data class AddBankDetailRequest(

    @SerializedName("account_holder_name")
    val account_holder_name: String,

    @SerializedName("account_number")
    val account_number: String,

    @SerializedName("account_type")
    val account_type: String,

    @SerializedName("bank_name")
    val bank_name: String,

    @SerializedName("ifsc_code")
    val ifsc_code: String
): Parcelable