/*
  Copyright (c) 2016-2025, Smart Engines Service LLC
  All rights reserved.
*/

package ru.igormayachenkov.nfc

import android.app.Activity
import android.content.Intent
import android.nfc.tech.IsoDep
import android.util.Log
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import org.jmrtd.BACKey

private const val TAG = "myapp.PassportReader"

/**
 * PASSPORT READING PROCESS IMPLEMENTATION
 */
class PassportReader(
    var readingEngine: ReadingEngine = ReadingEngineScuba()
) {
    private val coroutineScope = CoroutineScope(Dispatchers.IO)

    // PASSPORT KEY as a flow
    var passportKeyFlow = MutableStateFlow<PassportKey?>(null)

    // READING STATE
    private val _stateFlow: MutableStateFlow<PassportReaderState> = MutableStateFlow(PassportReaderState.Disabled)
    val stateFlow: StateFlow<PassportReaderState>
        get() = _stateFlow

    val isActive: Boolean
        get() = stateFlow.value in setOf(PassportReaderState.Waiting, PassportReaderState.Reading)

    //----------------------------------------------------------------------------------------------
    fun changeReadingEngine(newReadingEngine: ReadingEngine) {
        Log.w(TAG, "changeReadingEngine $newReadingEngine")
        readingEngine = newReadingEngine
    }

    private fun setNotSupported() {
        _stateFlow.value = PassportReaderState.NotSupported
    }

    fun activate(activity: Activity) {
        Log.d(TAG, "activate")
        if (!isActive) {
            // Enable NFC
            val result = NfcAdapterWrapper.enableNfcReceiving(activity, true)
            // Change state
            _stateFlow.value = when(result){
                NfcAdapterWrapper.ActivationResult.SUCCESS              -> PassportReaderState.Waiting
                NfcAdapterWrapper.ActivationResult.ERROR_DISABLED       -> PassportReaderState.Error("NFC adapter is disabled")
                NfcAdapterWrapper.ActivationResult.ERROR_NOT_SUPPORTED  -> PassportReaderState.NotSupported
            }
        }
    }

    fun reset(activity: Activity?) {
        Log.d(TAG, "reset")
        _stateFlow.value = PassportReaderState.Disabled
        // Disable NFC
        activity?.let {
            NfcAdapterWrapper.enableNfcReceiving(activity, false)
        }
    }

    private fun readPassportData(
        isoDep: IsoDep,
        activity: Activity,
        checkChip: Boolean = false,
        checkData: Boolean = false
    ) {
        val passportKey = passportKeyFlow.value
        Log.d(TAG, "readPassportData, passportKey: $passportKey")
        coroutineScope.launch {
            try {
                // Update state
                _stateFlow.value = PassportReaderState.Reading
                // Open the engine
                if (passportKey == null) throw Exception("passport key is null")
                val bacKey =
                    with(passportKey) { BACKey(passportNumber, birthDate, expirationDate) }
                readingEngine.open(
                    isoDep = isoDep,
                )
                val data = readingEngine.readPassportData(
                    bacKey = bacKey
                )
//                    // Do chip verification
//                    if(checkChip)
//                        data = data.copy(chipAuth = readingEngine.doChipAuth())
//                    // Do "passive" data verification
//                    if(checkData)
//                        data = data.copy(dataAuth = readingEngine.doDataAuth(context))

                // Update state
                Log.w(TAG, "Success")
                _stateFlow.value = PassportReaderState.Data(data)
            } catch (e: Exception) {
                Log.e(TAG, "readPassportData", e)
                // Set error state
                _stateFlow.value = PassportReaderState.Error(e.message ?: e.toString())
            }
            readingEngine.close()

            // Disable NFC
            NfcAdapterWrapper.enableNfcReceiving(activity, false)
        }
    }


    //--------------------------------------------------------------------------------------------------
    // INTEGRATION WITH NfcAdapter and Activity
    fun onResumeActivity(activity: Activity) {
        Log.d(TAG, "onResumeActivity nfcEnabled:$isActive")
        // Check not supported state
        if (stateFlow.value is PassportReaderState.NotSupported) return // nothing to do
        // ENABLE/DISABLE NFC INTENT RECEIVING
        if (NfcAdapterWrapper.enableNfcReceiving(activity, isActive)== NfcAdapterWrapper.ActivationResult.ERROR_NOT_SUPPORTED)
            setNotSupported()
    }

    fun onNewIntent(intent: Intent, activity: Activity){
        Log.d(TAG, "onNewIntent intent:$intent")
        if (isActive) {
            // Try to get passport tag
            NfcAdapterWrapper.getIsoDepTag(intent)?.let { tag ->
                // Read data
                readPassportData( IsoDep.get(tag), activity )
            }
        }
    }
}