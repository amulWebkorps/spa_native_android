package com.example.spa.utilities

import android.os.Parcel
import android.os.Parcelable

class Errors() : Parcelable {

    var errors: String? = null

    constructor(parcel: Parcel) : this() {
        errors = parcel.readString()
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(errors)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Errors> {
        override fun createFromParcel(parcel: Parcel): Errors {
            return Errors(parcel)
        }

        override fun newArray(size: Int): Array<Errors?> {
            return arrayOfNulls(size)
        }
    }


}