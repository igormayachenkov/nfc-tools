package ru.igormayachenkov.nfc

import android.util.Size
import java.io.DataInputStream

/**
 * Instead of org.jmrtd.lds.iso19794.FaceImageInfo
 */
class FaceImageData(
    val mimeType : String,
    val size     : Size,
    val buffer   : ByteArray,
){
    constructor(jmrtdFaceImageInfo:org.jmrtd.lds.iso19794.FaceImageInfo):this(
        mimeType    = jmrtdFaceImageInfo.mimeType,
        size        = Size(jmrtdFaceImageInfo.width, jmrtdFaceImageInfo.height),
        buffer = ByteArray(jmrtdFaceImageInfo.imageLength).apply {
            DataInputStream(jmrtdFaceImageInfo.imageInputStream)
                .readFully(this,0,this.size)
        }
    )
}

fun List<org.jmrtd.lds.iso19794.FaceInfo>.toFaceImageDataList() : List<FaceImageData> =
    mutableListOf<FaceImageData>().also {
        forEach { faceInfo->
            faceInfo.faceImageInfos.forEach{ faceImageInfo ->
                it.add(FaceImageData(faceImageInfo))
            }
        }
    }