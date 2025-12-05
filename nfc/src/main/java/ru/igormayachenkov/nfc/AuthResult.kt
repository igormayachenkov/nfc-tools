/*
  Copyright (c) 2016-2025, Smart Engines Service LLC
  All rights reserved.
*/

package ru.igormayachenkov.nfc

sealed interface AuthResult {
    data object Passed                   : AuthResult
    data class  Failed(val error:String) : AuthResult
}