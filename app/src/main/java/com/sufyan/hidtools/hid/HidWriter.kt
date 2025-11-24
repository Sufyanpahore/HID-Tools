package com.sufyan.hidtools.hid

import android.util.Log
import com.topjohnwu.superuser.Shell
import com.topjohnwu.superuser.io.SuFile
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File
import java.io.IOException

object HidWriter {

    private const val KEYBOARD_DEVICE = "/dev/hidg0"
    private const val MOUSE_DEVICE = "/dev/hidg1"
    private const val TAG = "HidWriter"

    var forceSimulateMode = false

    fun isGadgetAvailable(): Boolean {
        if (forceSimulateMode) return true
        return File(KEYBOARD_DEVICE).exists() && File(MOUSE_DEVICE).exists()
    }

    suspend fun sendKeyboardReport(report: ByteArray) {
        sendReport("writeKeyboard", report, KEYBOARD_DEVICE)
    }

    suspend fun sendMouseReport(report: ByteArray) {
        sendReport("writeMouse", report, MOUSE_DEVICE)
    }

    suspend fun sendKeyPress(modifier: Byte, keyCode: Byte) {
        val keyDownReport = buildKeyboardReport(modifier, byteArrayOf(keyCode, 0, 0, 0, 0, 0))
        sendKeyboardReport(keyDownReport)

        val keyUpReport = buildKeyboardReport(HidKeyCode.MOD_NONE, byteArrayOf(0, 0, 0, 0, 0, 0))
        sendKeyboardReport(keyUpReport)
    }

    fun buildKeyboardReport(modifier: Byte, keyCodes: ByteArray): ByteArray {
        val report = ByteArray(8)
        report[0] = modifier
        report[1] = 0 // Reserved
        keyCodes.copyInto(report, 2, 0, minOf(keyCodes.size, 6))
        return report
    }

    fun buildMouseReport(buttons: Byte, dx: Byte, dy: Byte, scroll: Byte): ByteArray {
        return byteArrayOf(buttons, dx, dy, scroll)
    }

    private suspend fun sendReport(logName: String, report: ByteArray, devicePath: String) = withContext(Dispatchers.IO) {
        val isRooted = Shell.isAppGrantedRoot() == true
        if (forceSimulateMode || !isRooted) {
            val reportString = report.joinToString(" ") { "%02x".format(it).uppercase() }
            Log.d(TAG, "SIMULATE: $logName [$reportString]")
            return@withContext
        }

        val deviceFile = SuFile(devicePath)
        if (!deviceFile.exists()) {
            Log.w(TAG, "Device file not found: $devicePath. Cannot write report.")
            return@withContext
        }

        try {
            deviceFile.outputStream().use { stream ->
                stream.write(report)
            }
        } catch (e: IOException) {
            Log.e(TAG, "Failed to write to $devicePath", e)
        }
    }
}
