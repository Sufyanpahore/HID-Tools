package com.sufyan.hidtools.viewmodel

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sufyan.hidtools.hid.HidKeyCode
import com.sufyan.hidtools.hid.HidWriter
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

enum class ComboMode { KEYBOARD, MOUSE }

class ComboViewModel : ViewModel() {

    val mode = mutableStateOf(ComboMode.KEYBOARD)

    // Modifier states
    val isShiftPressed = mutableStateOf(false)
    val isCtrlPressed = mutableStateOf(false)
    val isAltPressed = mutableStateOf(false)

    private var lastReportTime = 0L
    private val reportInterval = 16L // Throttle mouse reports

    fun toggleMode() {
        mode.value = if (mode.value == ComboMode.KEYBOARD) ComboMode.MOUSE else ComboMode.KEYBOARD
    }

    /**
     * Handles a single character key press.
     */
    fun onKeyClick(char: Char) {
        viewModelScope.launch {
            val (baseModifier, keyCode) = HidKeyCode.charToHid(char)

            var modifier = baseModifier
            if (isShiftPressed.value) modifier = (modifier.toInt() or HidKeyCode.MOD_L_SHIFT.toInt()).toByte()
            if (isCtrlPressed.value) modifier = (modifier.toInt() or HidKeyCode.MOD_L_CTRL.toInt()).toByte()
            if (isAltPressed.value) modifier = (modifier.toInt() or HidKeyCode.MOD_L_ALT.toInt()).toByte()

            HidWriter.sendKeyPress(modifier, keyCode)
        }
    }

    /**
     * Handles mouse movement, applying throttling.
     */
    fun onPointerMove(dx: Float, dy: Float) {
        val currentTime = System.currentTimeMillis()
        if (currentTime - lastReportTime < reportInterval) return
        lastReportTime = currentTime

        val adjustedDx = dx.toInt().coerceIn(-127, 127).toByte()
        val adjustedDy = dy.toInt().coerceIn(-127, 127).toByte()

        viewModelScope.launch {
            val report = HidWriter.buildMouseReport(0, adjustedDx, adjustedDy, 0)
            HidWriter.sendMouseReport(report)
        }
    }

    /**
     * Handles mouse clicks.
     */
    fun onMouseClick(button: Byte) {
        viewModelScope.launch {
            val pressReport = HidWriter.buildMouseReport(button, 0, 0, 0)
            HidWriter.sendMouseReport(pressReport)
            delay(50)
            val releaseReport = HidWriter.buildMouseReport(0, 0, 0, 0)
            HidWriter.sendMouseReport(releaseReport)
        }
    }

    /**
     * Sends a combination of a modifier and a key, like Ctrl+C.
     */
    fun sendShortcut(modifier: Byte, char: Char) {
        viewModelScope.launch {
            val (_, keyCode) = HidKeyCode.charToHid(char)
            HidWriter.sendKeyPress(modifier, keyCode)
        }
    }
}
