/*
  Copyright (c) 2016-2025, Smart Engines Service LLC
  All rights reserved.
*/

package ru.igormayachenkov.nfc

/**
 * Decoded FaceImageData
 */
data class FaceImageDecoded(
    val data  : FaceImageData,
    val bitmap: FaceImageBitmap
)
