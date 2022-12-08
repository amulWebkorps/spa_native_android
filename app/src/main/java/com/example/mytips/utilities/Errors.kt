package com.example.mytips.utilities

import android.os.Parcel
import android.os.Parcelable

class Errors() : Parcelable {

    var errors: String? = null
    var message: String? = null

//    var status: String? = null
//
//    var next: String? = null

    constructor(parcel: Parcel) : this() {
        errors = parcel.readString()
        message = parcel.readString()
//        status = parcel.readString()
//        next = parcel.readString()
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(errors)
        parcel.writeString(message)
//        parcel.writeString(status)
//        parcel.writeString(next)
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