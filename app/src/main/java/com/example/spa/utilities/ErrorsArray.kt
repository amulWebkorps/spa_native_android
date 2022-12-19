package com.example.spa.utilities

import android.os.Parcel
import android.os.Parcelable

class ErrorsArray() : Parcelable {

    lateinit var errors: Array<String?>


    constructor(parcel: Parcel) : this() {
        errors = readStringArray(parcel,errors)
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeStringArray(errors);
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<ErrorsArray> {
        override fun createFromParcel(parcel: Parcel): ErrorsArray {
            return ErrorsArray(parcel)
        }

        override fun newArray(size: Int): Array<ErrorsArray?> {
            return arrayOfNulls(size)
        }
    }


}

fun readStringArray(parcel: Parcel,`val`: Array<String?>):Array<String?>{
    val N: Int =parcel.readInt()
    if (N == `val`.size) {
        for (i in 0 until N) {
            `val`[i] = parcel.readString()
        }
        return `val`
    } else {
        throw RuntimeException("bad array lengths")
    }
}