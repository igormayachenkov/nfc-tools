
package ru.igormayachenkov.nfc_sample

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Checkbox
import androidx.compose.material3.OutlinedButton
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModelProvider
import ru.igormayachenkov.nfc.PassportKey
import ru.igormayachenkov.nfc.PassportReaderState
import ru.igormayachenkov.nfc_sample.ui.theme.AiEnginesTheme


private const val TAG = "myapp.MainActivity"

class MainActivity : ComponentActivity() {
    private lateinit var model : MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d(TAG, "onCreate")

        // Get saved passport key
        val passportKey = PassportKey(
            // My Old
            passportNumber = "711423874",
            expirationDate = "200722",
            birthDate = "711027"
            // My New
            //        val passportNumber = "762863213"
            //        val expirationDate = "300320"
            //        val birthDate      = "711027"
        )

        // Create a ViewModel the first time the system calls an activity's onCreate() method.
        // Re-created activities receive the same MyViewModel instance created by the first activity.
        //model = ViewModelProvider(this).get(MainViewModel::class.java)
        model = ViewModelProvider(owner = this, factory = MainViewModelFactory(passportKey))
            .get(MainViewModel::class.java)

        setContent {
            AiEnginesTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    // DATA
                    val activity = this
                    val state by model.passportReader.stateFlow.collectAsState() // PASSPORT READER STATE
                    val passportKey by model.passportReader.passportKeyFlow.collectAsState()
                    val isMockMode by model.isMockMode
                    var showDialog by model.showDialog
                    Log.d(TAG,"----- nfc state: $state")

                    // Passport key input dialog
                    if(showDialog)
                        PassportKeyDialog(model = model)

                    // CONTENT
                    Column(
                        Modifier
                            .fillMaxSize()
                            .padding(16.dp)
                            .verticalScroll(rememberScrollState()),
                            horizontalAlignment = Alignment.CenterHorizontally
                    ) {

                        // Passport key data
                        passportKey?.let{
                            Row(Modifier.fillMaxWidth()) {
                                Text("Passport Number:", Modifier.weight(1f))
                                Text(it.passportNumber)
                            }
                           Row(Modifier.fillMaxWidth()) {
                               Text("Birth Date:", Modifier.weight(1f))
                               Text(it.birthDate)
                           }
                           Row(Modifier.fillMaxWidth()) {
                                Text("Expiration Date:", Modifier.weight(1f))
                                Text(it.expirationDate)
                            }
                        }
                        OutlinedButton(onClick = { showDialog=true }) {
                            Text("Edit passport key")
                        }

                        // PASSPORT READER STATE
                        Divider(Modifier.padding(vertical = 16.dp), thickness = 1.dp, color = MaterialTheme.colorScheme.onSurface)

                        when(state){
                            is PassportReaderState.Data -> {
                                PassportDataView( (state as PassportReaderState.Data).data )
                                OutlinedButton({model.passportReader.reset(activity)}) { Text("Reset") }
                            }
                            is PassportReaderState.Error -> {
                                val error = (state as PassportReaderState.Error)
                                Text(text = "ERROR")
                                Text(text = error.message)
                                // Try again
                                OutlinedButton( onClick = {model.passportReader.activate(activity)} ){
                                    Text(text = "Try again")
                                }

                            }
                            PassportReaderState.Disabled -> {
                                // Activate button
                                OutlinedButton( onClick = {model.passportReader.activate(activity)} ){
                                    Text(text = "Activate NFC")
                                }
                            }
                            PassportReaderState.Reading -> {
                                Text(text = "Chip data reading...")
                            }

                            PassportReaderState.Waiting -> {
                                Text(text = "Move your device to NFC chip")
                                OutlinedButton( onClick = {model.passportReader.reset(activity)} ){
                                    Text(text = "Cancel")
                                }
                            }
                            PassportReaderState.NotSupported -> {
                                Text(text = "Your device doesn't support nfc-reading")
                            }
                        }
                    }
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        Log.d(TAG, "onResume")
        // ENABLE/DISABLE NFC INTENT RECEIVING
        model.passportReader.onResumeActivity(this)
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        Log.w(TAG, "onNewIntent $intent")
        model.passportReader.onNewIntent(intent,this)
    }
}

