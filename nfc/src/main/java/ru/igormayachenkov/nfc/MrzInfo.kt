package ru.igormayachenkov.nfc

/**
 * Just a copy of org.jmrtd.lds.icao.MRZInfo
 */

data class MrzInfo(
    val fullString          : String, // FULL MRZ STRING
    val documentCode        : String, // document code
    val issuingState        : String, // issuing state
    val primaryIdentifier   : String, // last name
    val secondaryIdentifier : String, // first name
    val documentNumber      : String, // document number
    val nationality         : String, // nationality
    val dateOfBirth         : String, // date of birth
    val gender              : String, // gender
    val dateOfExpiry        : String, // date of expiry
    val personalNumber      : String, // personalNumber
){
    constructor(jmrtdMRZInfo : org.jmrtd.lds.icao.MRZInfo) : this(
        fullString              = jmrtdMRZInfo.toString(),
        documentCode            = jmrtdMRZInfo.documentCode,
        issuingState            = jmrtdMRZInfo.issuingState,
        primaryIdentifier       = jmrtdMRZInfo.primaryIdentifier,
        secondaryIdentifier     = jmrtdMRZInfo.secondaryIdentifier,
        documentNumber          = jmrtdMRZInfo.documentNumber,
        nationality             = jmrtdMRZInfo.nationality,
        dateOfBirth             = jmrtdMRZInfo.dateOfBirth,
        gender                  = jmrtdMRZInfo.gender.toString(),
        dateOfExpiry            = jmrtdMRZInfo.dateOfExpiry,
        personalNumber          = jmrtdMRZInfo.personalNumber
    )
    fun toSingleLine():String =
        fullString.replace("\n", "")
}
