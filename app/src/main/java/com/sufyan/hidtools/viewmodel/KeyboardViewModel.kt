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

    fun onKeyClick(char: Char) {
        when (mode.value) {
            KeyboardMode.LIVE -> sendKey(char)
            KeyboardMode.SCRIPT -> addToScript(char)
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

    fun toggleMode() {
        mode.value = if (mode.value == KeyboardMode.LIVE) KeyboardMode.SCRIPT else KeyboardMode.LIVE
    }

    private fun sendKey(char: Char) {
        viewModelScope.launch {
            val (baseModifier, keyCode) = HidKeyCode.charToHid(char)

            var modifier = baseModifier
            if (isShiftPressed.value) modifier = (modifier.toInt() or HidKeyCode.MOD_L_SHIFT.toInt()).toByte()
            if (isCtrlPressed.value) modifier = (modifier.toInt() or HidKeyCode.MOD_L_CTRL.toInt()).toByte()
            if (isAltPressed.value) modifier = (modifier.toInt() or HidKeyCode.MOD_L_ALT.toInt()).toByte()

            // Send key press
            val pressReport = HidWriter.buildKeyboardReport(modifier, byteArrayOf(keyCode, 0, 0, 0, 0, 0))
            HidWriter.sendKeyboardReport(pressReport)

            // Send key release
            val releaseReport = HidWriter.buildKeyboardReport(HidKeyCode.MOD_NONE, byteArrayOf(0, 0, 0, 0, 0, 0))
            HidWriter.sendKeyboardReport(releaseReport)
        }
    }

    private fun addToScript(char: Char) {
        var command = ""
        if (isCtrlPressed.value) command += "CTRL+"
        if (isAltPressed.value) command += "ALT+"
        if (isShiftPressed.value || char.isUpperCase()) command += "SHIFT+"

        command += "KEY " + char.uppercaseChar()
        script.add(command)
    }

    fun clearScript() {
        script.clear()
    }
}