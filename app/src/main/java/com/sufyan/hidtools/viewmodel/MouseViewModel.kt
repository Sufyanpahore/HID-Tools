package com.sufyan.hidtools.viewmodel

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sufyan.hidtools.hid.HidWriter
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

enum class MouseMode { LIVE, SCRIPT }

class MouseViewModel : ViewModel() {

    val mode = mutableStateOf(MouseMode.LIVE)
    val script = mutableStateListOf<String>()
    val sensitivity = mutableStateOf(1.0f)

    private var lastReportTime = 0L
    private val reportInterval = 16L // ~60 reports per second

    private var movementJob: Job? = null

    fun onPointerMove(dx: Float, dy: Float) {
        val currentTime = System.currentTimeMillis()
        if (currentTime - lastReportTime < reportInterval) {
            return // Throttle reports
        }
        lastReportTime = currentTime

        val adjustedDx = (dx * sensitivity.value).toInt().coerceIn(-127, 127)
        val adjustedDy = (dy * sensitivity.value).toInt().coerceIn(-127, 127)

        when (mode.value) {
            MouseMode.LIVE -> sendMoveReport(adjustedDx.toByte(), adjustedDy.toByte())
            MouseMode.SCRIPT -> script.add("MOUSE MOVE $adjustedDx $adjustedDy")
        }
    }

    fun onLeftClick() {
        when (mode.value) {
            MouseMode.LIVE -> sendClickReport(0x01)
            MouseMode.SCRIPT -> script.add("MOUSE CLICK LEFT")
        }
    }

    fun onRightClick() {
        when (mode.value) {
            MouseMode.LIVE -> sendClickReport(0x02)
            MouseMode.SCRIPT -> script.add("MOUSE CLICK RIGHT")
        }
    }

    private fun sendMoveReport(dx: Byte, dy: Byte) {
        movementJob?.cancel() // Cancel previous job to send a new one
        movementJob = viewModelScope.launch {
            val report = HidWriter.buildMouseReport(0, dx, dy, 0)
            HidWriter.sendMouseReport(report)
        }
    }

    private fun sendClickReport(button: Byte) {
        viewModelScope.launch {
            // Button press
            val pressReport = HidWriter.buildMouseReport(button, 0, 0, 0)
            HidWriter.sendMouseReport(pressReport)
            delay(50) // Short delay between press and release
            // Button release
            val releaseReport = HidWriter.buildMouseReport(0, 0, 0, 0)
            HidWriter.sendMouseReport(releaseReport)
        }
    }

    fun setSensitivity(value: Float) {
        sensitivity.value = value
    }

    fun toggleMode() {
        mode.value = if (mode.value == MouseMode.LIVE) MouseMode.SCRIPT else MouseMode.LIVE
    }

    fun clearScript() {
        script.clear()
    }
}
