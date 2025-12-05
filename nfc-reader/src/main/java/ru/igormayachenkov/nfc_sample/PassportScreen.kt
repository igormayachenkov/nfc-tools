package ru.igormayachenkov.nfc_sample

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import ru.igormayachenkov.nfc_sample.ImageDecoder.decodeFaceImage
import ru.igormayachenkov.nfc.FaceImageBitmap
import ru.igormayachenkov.nfc.FaceImageDecoded
import ru.igormayachenkov.nfc.MrzInfo
import ru.igormayachenkov.nfc.PassportData
// JMRTD library


private const val TAG = "myapp.PassportDataView"

@Composable
fun PassportDataView(data: PassportData){
    // DECODE FACE IMAGES
    val context = LocalContext.current
    val faceImages = remember { data.facesData.map {
            FaceImageDecoded(
                data   = it,
                bitmap = decodeFaceImage(it)
            )
        }
    }

    // AUTH RESULT
    Text(text = "AUTH RESULT",    style = MaterialTheme.typography.headlineMedium)
    DataRow("chip auth",   data.chipAuth.toString())
    DataRow("data auth",   data.dataAuth.toString())
    // MRZ
    Text(text = "MRZ",    style = MaterialTheme.typography.headlineMedium)
    with(data.mrzInfo) {
        DataRow("document code",    documentCode)
        DataRow("issuing state",    issuingState)
        DataRow("last name",        primaryIdentifier)
        DataRow("first name",       secondaryIdentifier)
        DataRow("document number",  documentNumber)
        DataRow("nationality",      nationality)
        DataRow("date of birth",    dateOfBirth)
        DataRow("gender",           gender)
        DataRow("date of expiry",   dateOfExpiry)
        DataRow("personalNumber",   personalNumber)
    }

    // FACES
    Text(text = "FACES",    style = MaterialTheme.typography.headlineMedium)
    faceImages.forEach {
        // Decoded Bitmap
        it.bitmap.let {
            when(it){
                is FaceImageBitmap.Success -> {
                    Image(
                        bitmap = it.bitmap.asImageBitmap(),
                        contentDescription = "photo"
                    )
                }
                is FaceImageBitmap.Error -> {
                    Text(text = it.error)
                }
            }
        }
        // Info
        Text("mime: ${it.data.mimeType}  size: ${it.data.size}",style = MaterialTheme.typography.labelSmall)
    }
}

@Composable
private fun DataRow(label:String, value:String){
    Row(Modifier
        .fillMaxWidth()
        .padding(bottom = 10.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(modifier = Modifier.weight(1f),
            text = label,  style = MaterialTheme.typography.labelSmall)
        Text(modifier = Modifier.weight(2f),
            text = value,  style = MaterialTheme.typography.bodyMedium)

    }

}


//--------------------------------------------------------------------------------------------------
// PREVIEW
@Preview(showBackground = true, showSystemUi = false)
@Composable
private fun PassportDataView_Preview() {
    val mrz = MrzInfo(
        "line1\nline2",
        "P",//documentCode
        "RUS",//issuingState
        "KRYUCHKOVA",//primaryIdentifier
        "KSENIA<<<<<<<<<<<<<<<<<<<<<",//secondaryIdentifier
        "715599956",//documentNumber
        "RUS",//nationality
        "971010",//dateOfBirth
        "FEMALE",//gender
        "210802",//dateOfExpiry
        ""//personalNumber
    )

    //AiEnginesTheme(darkTheme = false) {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            Column(Modifier.fillMaxSize()) {
//                PassportDataView(
//                    PassportData(
//                        mrzInfo = mrz,
//                        faceImages = ArrayList<FaceImage>(),
//                        chipAuth = AuthResult.Passed,
//                        dataAuth = AuthResult.Failed("the error description")
//                    )
//                )
            }
        }
    //}
}
