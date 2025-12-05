/*
  Copyright (c) 2016-2025, Smart Engines Service LLC
  All rights reserved.
*/

package ru.igormayachenkov.nfc

// JMRTD library

data class PassportData(
    // Passport data fields
    val mrzInfo     : MrzInfo,         // from DG1File
    val facesData   : List<FaceImageData>,  // from DG2File
    // Checks
    val chipAuth    : AuthResult?,
    val dataAuth    : AuthResult?
)

