package com.sufyan.hidtools.viewmodel

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sufyan.hidtools.hid.HidKeyCode
import com.sufyan.hidtools.hid.HidWriter
import kotlinx.coroutines.launch

enum class KeyboardMode { LIVE, SCRIPT }

class KeyboardViewModel : ViewModel() {

    val mode = mutableStateOf(KeyboardMode.LIVE)
    val script = mutableStateListOf<String>()

    val isShiftPressed = mutableStateOf(false)
    val isCtrlPressed = mutableStateOf(false)
    val isAltPressed = mutableStateOf(false)
    val isMetaPressed = mutableStateOf(false)

    fun onKeyClick(char: Char) {
        when (mode.value) {
            KeyboardMode.LIVE -> sendChar(char)
            KeyboardMode.SCRIPT -> addCharToScript(char)
        }
    }

    fun onSpecialKeyClick(keyCode: Byte) {
        when (mode.value) {
            KeyboardMode.LIVE -> sendKeyCode(keyCode)
            KeyboardMode.SCRIPT -> addKeyCodeToScript(keyCode)
        }
    }

    fun toggleShift() {
        isShiftPressed.value = !isShiftPressed.value
    }

    fun toggleCtrl() {
        isCtrlPressed.value = !isCtrlPressed.value
    }

    fun toggleAlt() {
        isAltPressed.value = !isAltPressed.value
    }

    fun toggleMeta() {
        isMetaPressed.value = !isMetaPressed.value
    }

    fun toggleMode() {
        mode.value = if (mode.value == KeyboardMode.LIVE) KeyboardMode.SCRIPT else KeyboardMode.LIVE
    }

    private fun sendChar(char: Char) {
        viewModelScope.launch {
            val (baseModifier, keyCode) = HidKeyCode.charToHid(char)
            sendKeyWithModifiers(baseModifier, keyCode)
        }
    }

    private fun sendKeyCode(keyCode: Byte) {
        viewModelScope.launch {
            sendKeyWithModifiers(0, keyCode)
        }
    }

    private suspend fun sendKeyWithModifiers(baseModifier: Byte, keyCode: Byte) {
        var modifier = baseModifier
        if (isShiftPressed.value) modifier = (modifier.toInt() or HidKeyCode.MOD_L_SHIFT.toInt()).toByte()
        if (isCtrlPressed.value) modifier = (modifier.toInt() or HidKeyCode.MOD_L_CTRL.toInt()).toByte()
        if (isAltPressed.value) modifier = (modifier.toInt() or HidKeyCode.MOD_L_ALT.toInt()).toByte()
        if (isMetaPressed.value) modifier = (modifier.toInt() or HidKeyCode.MOD_L_META.toInt()).toByte()

        // Send key press
        val pressReport = HidWriter.buildKeyboardReport(modifier, byteArrayOf(keyCode, 0, 0, 0, 0, 0))
        HidWriter.sendKeyboardReport(pressReport)

        // Send key release
        val releaseReport = HidWriter.buildKeyboardReport(HidKeyCode.MOD_NONE, byteArrayOf(0, 0, 0, 0, 0, 0))
        HidWriter.sendKeyboardReport(releaseReport)
    }

    private fun addCharToScript(char: Char) {
        var command = buildModifierString()
        // Simple heuristic for script readability: if it's a regular char, use KEY <char>
        command += "KEY " + char.uppercaseChar()
        script.add(command)
    }

    private fun addKeyCodeToScript(keyCode: Byte) {
        var command = buildModifierString()
        // Convert keyCode back to a readable string for the script if possible, or use a numeric code
        // For simplicity, we might need a reverse lookup or just store KEYCODE <byte>
        // But the current script parser expects KEY <char>.
        // We might need to enhance the script parser later to support raw keycodes or named keys (KEY ENTER, KEY F1).
        // For now, let's assume we log it as a hex code if it's not a char.
        command += "KEYCODE $keyCode"
        script.add(command)
    }

    private fun buildModifierString(): String {
        var command = ""
        if (isCtrlPressed.value) command += "CTRL+"
        if (isAltPressed.value) command += "ALT+"
        if (isMetaPressed.value) command += "META+"
        if (isShiftPressed.value) command += "SHIFT+"
        return command
    }

    fun clearScript() {
        script.clear()
    }
}
