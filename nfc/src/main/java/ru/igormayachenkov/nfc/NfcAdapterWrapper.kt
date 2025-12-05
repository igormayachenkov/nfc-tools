/*
  Copyright (c) 2016-2025, Smart Engines Service LLC
  All rights reserved.
*/

package ru.igormayachenkov.nfc

import android.app.PendingIntent
import android.app.Activity
import android.content.Intent
import android.nfc.NfcAdapter
import android.nfc.Tag
import android.util.Log

private const val TAG = "myapp.NfcAdapterWrapper"

internal object NfcAdapterWrapper {
    enum class ActivationResult{
        SUCCESS, ERROR_NOT_SUPPORTED, ERROR_DISABLED
    }

    // ENABLE/DISABLE NFC INTENT RECEIVING
    // instead of intent filter in manifest
    fun enableNfcReceiving(activity: Activity, enable:Boolean) : ActivationResult {
        NfcAdapter.getDefaultAdapter(activity)?.let { adapter ->
            if(!adapter.isEnabled) return ActivationResult.ERROR_DISABLED
            if (enable) {
                Log.w(TAG, ">>> enable NFC Receiving")
                val intent = Intent(activity.applicationContext, activity.javaClass)
                intent.flags = Intent.FLAG_ACTIVITY_SINGLE_TOP
                val pendingIntent =
                    PendingIntent.getActivity(activity, 0, intent, PendingIntent.FLAG_MUTABLE)
                val filter = arrayOf(arrayOf("android.nfc.tech.IsoDep"))
                adapter.enableForegroundDispatch(activity, pendingIntent, null, filter)
            } else {
                Log.w(TAG, "<<< disable NFC Receiving")
                adapter.disableForegroundDispatch(activity)
            }
            return ActivationResult.SUCCESS
        }
        return ActivationResult.ERROR_NOT_SUPPORTED
    }

    fun getIsoDepTag(intent: Intent):Tag? {
        Log.w(TAG, "getPassportTag $intent")
        if (NfcAdapter.ACTION_TECH_DISCOVERED == intent.action) {
            val tag: Tag? = intent.extras?.getParcelable(NfcAdapter.EXTRA_TAG)
            if (tag?.techList?.contains("android.nfc.tech.IsoDep") == true) {
                return tag
            }
        }
        return null
    }
}