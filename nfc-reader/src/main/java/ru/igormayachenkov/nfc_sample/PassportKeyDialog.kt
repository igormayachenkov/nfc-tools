package ru.igormayachenkov.nfc_sample

import android.util.Log
import android.widget.EditText
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import ru.igormayachenkov.nfc.PassportKey

private const val TAG = "myapp.PassportKeyDialog"

@Composable
fun PassportKeyDialog(model : MainViewModel){
    Dialog(onDismissRequest = {
        Log.d(TAG,"onDismissRequest")
        model.showDialog.value = false
    }) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                //.height(200.dp)
                .padding(16.dp),
            shape = RoundedCornerShape(16.dp),
        ) {
            Column(Modifier.padding(16.dp)) {
                Text("Passport key data")
            }

            // Passport key data
            var data by remember { mutableStateOf(model.passportReader.passportKeyFlow.value?: PassportKey("","","")) }

            Text("Passport Number:", Modifier.padding(top = 20.dp))
            TextField(value = data.passportNumber, onValueChange = { data=data.copy(passportNumber = it)})

            Text("Birth Date:", Modifier.padding(top = 20.dp))
            TextField(value = data.birthDate, onValueChange = { data=data.copy(birthDate = it)})

            Text("Expiration Date:", Modifier.padding(top = 30.dp))
            TextField(value = data.expirationDate, onValueChange = { data=data.copy(expirationDate = it)})

            Button( modifier = Modifier.padding(20.dp),
                onClick = {
                    model.passportReader.passportKeyFlow.value = data
                    model.showDialog.value  = false
                }
            ){
                Text("Save")
            }
        }
    }
}