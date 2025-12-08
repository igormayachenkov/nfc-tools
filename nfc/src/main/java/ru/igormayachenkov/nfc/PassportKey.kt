/*
  Copyright (c) 2016-2025, Smart Engines Service LLC
  All rights reserved.
*/

package ru.igormayachenkov.nfc

import android.os.Parcel
import android.os.Parcelable
import org.jmrtd.lds.icao.MRZInfo

/**
 * More convenient copy of BACKey
 */
//import kotlinx.parcelize.Parcelize

//@Parcelize
data class PassportKey(
    val passportNumber : String,
    val expirationDate : String,
    val birthDate      : String
) : Parcelable{
    override fun describeContents(): Int {
        return 0
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(passportNumber)
        parcel.writeString(expirationDate)
        parcel.writeString(birthDate)
    }

    companion object CREATOR: Parcelable.Creator<PassportKey?> {
        override fun createFromParcel(parcel: Parcel): PassportKey? {
            return PassportKey(parcel.readString()!!,parcel.readString()!!,parcel.readString()!!)
        }

        override fun newArray(size: Int): Array<PassportKey?> {
            return arrayOfNulls(size)
        }
    }

//    companion object{
//        fun fromMRZ(mrz:String): PassportKey {
//            try {
//                val mrzInfo = MRZInfo(mrz)
//                return PassportKey(
//                    mrzInfo.documentNumber,
//                    mrzInfo.dateOfExpiry,
//                    mrzInfo.dateOfBirth
//                )
//            }catch (e:Exception){
//                throw Exception("Can't parse MRZ string: $e")
//            }
//        }
//    }
}