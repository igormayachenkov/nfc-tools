package ru.igormayachenkov.nfc_sample

import android.content.Context
import android.nfc.Tag
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import android.nfc.tech.IsoDep
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.CreationExtras
import ru.igormayachenkov.nfc.PassportKey
import ru.igormayachenkov.nfc.PassportReader
import ru.igormayachenkov.nfc.ReadingEngineScuba

private const val TAG = "myapp.ViewModel"

class MainViewModel(passportKey: PassportKey) : ViewModel() {
    //----------------------------------------------------------------------------------------------
    // DATA
    val passportReader = PassportReader()
    val isMockMode = mutableStateOf(false)
    val showDialog = mutableStateOf(false)


    //----------------------------------------------------------------------------------------------
    // IMPLEMENTATION
    init {
        Log.w(TAG, "init, passportKey: $passportKey")
        passportReader.passportKeyFlow.value = passportKey
    }
    override fun onCleared() {
        super.onCleared()
        Log.d(TAG, "destroy")
    }

}

class MainViewModelFactory(val passportKey: PassportKey) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T =
        if(modelClass.isAssignableFrom(MainViewModel::class.java))
            MainViewModel(passportKey) as T
        else throw IllegalArgumentException("Unknown ViewModel class")

}