package ru.igormayachenkov.nfc_sample

import android.content.Context
import android.nfc.Tag
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import android.nfc.tech.IsoDep
import androidx.compose.runtime.mutableStateOf
import ru.igormayachenkov.nfc.PassportKey
import ru.igormayachenkov.nfc.PassportReader
import ru.igormayachenkov.nfc.ReadingEngineScuba

private const val TAG = "myapp.ViewModel"

class MainViewModel : ViewModel() {
    //----------------------------------------------------------------------------------------------
    // DATA
    val passportReader = PassportReader()
    val isMockMode = mutableStateOf(false)
    val showDialog = mutableStateOf(false)


    //----------------------------------------------------------------------------------------------
    // IMPLEMENTATION
    init {
        Log.d(TAG, "init")
        passportReader.passportKeyFlow.value = PassportKey(
            // From Julia
//        passportNumber = "715599956",
//        expirationDate = "210802",
//        birthDate      = "971010"
            // My Old
            passportNumber = "711423874",
            expirationDate = "200722",
            birthDate = "711027"
            // My New
            //        val passportNumber = "762863213"
            //        val expirationDate = "300320"
            //        val birthDate      = "711027"
        )

    }
    override fun onCleared() {
        super.onCleared()
        Log.d(TAG, "destroy")
    }

}