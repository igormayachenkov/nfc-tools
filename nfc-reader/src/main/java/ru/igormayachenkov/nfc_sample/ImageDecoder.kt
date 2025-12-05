package ru.igormayachenkov.nfc_sample

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Log
import ru.igormayachenkov.nfc.FaceImageDecoded
import ru.igormayachenkov.nfc.FaceImageBitmap
import com.gemalto.jp2.JP2Decoder
import ru.igormayachenkov.nfc.FaceImageData
import org.jnbis.WsqDecoder
import java.io.ByteArrayInputStream
import java.io.InputStream


object ImageDecoder {
    private val TAG = "myapp.ImageDecoder"

    fun decodeImage(mimeType: String, inputStream: InputStream): Bitmap {
        Log.d(TAG,"decodeImage, mime: $mimeType" )

        //return BitmapFactory.decodeStream(inputStream) // --- Failed to create image decoder with message 'unimplemented'
        // TODO try android.graphics.ImageDecoder

        return if (mimeType.equals("image/jp2", ignoreCase = true)
            || mimeType.equals("image/jpeg2000", ignoreCase = true)) {
            JP2Decoder(inputStream).decode()
        } else if (mimeType.equals("image/x-wsq", ignoreCase = true)) {
            val wsqDecoder = WsqDecoder()
            val bitmap = wsqDecoder.decode(inputStream)
            val byteData = bitmap.pixels
            val intData = IntArray(byteData.size)
            for (j in byteData.indices) {
                intData[j] = 0xFF000000.toInt() or
                        (byteData[j].toInt() and 0xFF shl 16) or
                        (byteData[j].toInt() and 0xFF shl 8) or
                        (byteData[j].toInt() and 0xFF)
            }
            Bitmap.createBitmap(
                intData,
                0,
                bitmap.width,
                bitmap.width,
                bitmap.height,
                Bitmap.Config.ARGB_8888
            )
        } else {
            BitmapFactory.decodeStream(inputStream)
        }
    }

    //--------------------------------------------------------------------------------------------------
    // Return bitmap or error
    fun decodeFaceImage(
        faceImageData: FaceImageData
    ): FaceImageBitmap {
        return try {

            // SAVE DATA
//            try {
//                ExternalStorageSaver.saveImage(
//                    context = context,
//                    buffer = buffer,
//                    directory = ExternalStorageSaver.ImageDirectories.Pictures,
//                    filename = "nfc_photo_igor.jp2",
//                    mimeType = "image/jp2"
//                )
//            }catch (e:Exception){
//                Log.e(TAG,"saveFile",e)
//            }

            val inputStream: InputStream = ByteArrayInputStream(faceImageData.buffer, 0, faceImageData.buffer.size)
            val bitmap = ImageDecoder.decodeImage(faceImageData.mimeType, inputStream)
            // android.graphics.ImageDecoder
            Log.w(TAG,"FACE IMAGE DECODED, bitmap:${bitmap.width}x${bitmap.height}, mime: ${faceImageData.mimeType}"
            )
            //imageBase64 = Base64.encodeToString(buffer, Base64.DEFAULT)
            //Log.w(TAG,"imageBase64:$imageBase64")
            FaceImageBitmap.Success(bitmap)
        } catch (e: Exception) {
            Log.e(TAG, "face image decode error", e)
            FaceImageBitmap.Error(e.toString())
        }
    }


}