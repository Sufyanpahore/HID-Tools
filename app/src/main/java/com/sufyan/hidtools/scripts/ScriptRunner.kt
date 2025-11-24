package com.sufyan.hidtools.scripts

import com.sufyan.hidtools.hid.HidKeyCode
import com.sufyan.hidtools.hid.HidWriter
import com.topjohnwu.superuser.Shell
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext
import java.io.File
import java.io.IOException
import java.util.concurrent.atomic.AtomicBoolean

data class ScriptSafetyResult(val hasLongDelays: Boolean, val totalDelay: Long)

sealed class ScriptAction {
    data class KeyPress(val modifier: Byte, val keyCode: Byte) : ScriptAction()
    data class KeyDown(val modifier: Byte) : ScriptAction()
    data class KeyUp(val modifier: Byte) : ScriptAction()
    data class MouseMove(val dx: Byte, val dy: Byte) : ScriptAction()
    data class MouseClick(val button: Byte) : ScriptAction()
    data class Delay(val ms: Long) : ScriptAction()
    data class Type(val text: String) : ScriptAction()
}

object ScriptRunner {

    private val isRunning = AtomicBoolean(false)

    fun stop() {
        isRunning.set(false)
    }

    suspend fun analyzeScriptForSafety(scriptFile: File): ScriptSafetyResult = withContext(Dispatchers.IO) {
        var totalDelay = 0L
        var hasLongDelays = false
        scriptFile.forEachLine {
            if (it.startsWith("DELAY")) {
                val delayMs = it.split(" ").getOrNull(1)?.toLongOrNull() ?: 0
                if (delayMs > 5000) {
                    hasLongDelays = true
                }
                totalDelay += delayMs
            }
        }
        ScriptSafetyResult(hasLongDelays, totalDelay)
    }

    fun parseLine(line: String, currentModifiers: Byte): Pair<ScriptAction?, Byte> {
        val parts = line.trim().split(" ")
        var newModifiers = currentModifiers
        val action: ScriptAction? = when (parts.getOrNull(0)) {
            "KEY" -> {
                val char = parts.getOrNull(1)?.firstOrNull() ?: return Pair(null, newModifiers)
                val (mod, code) = HidKeyCode.charToHid(char)
                ScriptAction.KeyPress((currentModifiers.toInt() or mod.toInt()).toByte(), code)
            }
            "KEYDOWN" -> {
                val mod = parts.getOrNull(1)?.let { modifierMap[it] } ?: 0
                newModifiers = (currentModifiers.toInt() or mod.toInt()).toByte()
                ScriptAction.KeyDown(newModifiers)
            }
            "KEYUP" -> {
                val mod = parts.getOrNull(1)?.let { modifierMap[it] } ?: 0
                newModifiers = (currentModifiers.toInt() and mod.toInt().inv()).toByte()
                ScriptAction.KeyUp(newModifiers)
            }
            "MOUSE" -> when (parts.getOrNull(1)) {
                "MOVE" -> {
                    val dx = parts.getOrNull(2)?.toIntOrNull()?.toByte() ?: 0
                    val dy = parts.getOrNull(3)?.toIntOrNull()?.toByte() ?: 0
                    ScriptAction.MouseMove(dx, dy)
                }
                "CLICK" -> {
                    val button = when (parts.getOrNull(2)) {
                        "LEFT" -> 0x01.toByte()
                        "RIGHT" -> 0x02.toByte()
                        "MIDDLE" -> 0x04.toByte()
                        else -> 0
                    }
                    ScriptAction.MouseClick(button)
                }
                else -> null
            }
            "DELAY" -> {
                val ms = parts.getOrNull(1)?.toLongOrNull() ?: 0
                ScriptAction.Delay(ms)
            }
            "TYPE" -> {
                val text = line.substringAfter("TYPE ").trim()
                ScriptAction.Type(text)
            }
            else -> null
        }
        return Pair(action, newModifiers)
    }

    suspend fun runScript(
        scriptFile: File,
        onProgress: (lineIndex: Int, line: String) -> Unit,
        onComplete: (success: Boolean) -> Unit
    ) = withContext(Dispatchers.IO) {
        val isRooted = Shell.isAppGrantedRoot() == true
        if (!isRooted || !HidWriter.isGadgetAvailable()) {
            onComplete(false)
            return@withContext
        }

        isRunning.set(true)
        var success = true

        try {
            val lines = scriptFile.readLines()
            var currentModifiers: Byte = 0

            for ((index, line) in lines.withIndex()) {
                if (!isRunning.get()) break

                onProgress(index, line)
                val (action, newModifiers) = parseLine(line, currentModifiers)
                currentModifiers = newModifiers

                when(action) {
                    is ScriptAction.KeyPress -> HidWriter.sendKeyPress(action.modifier, action.keyCode)
                    is ScriptAction.KeyDown -> { /* Modifier state is handled by the loop */ }
                    is ScriptAction.KeyUp -> { /* Modifier state is handled by the loop */ }
                    is ScriptAction.MouseMove -> HidWriter.sendMouseReport(HidWriter.buildMouseReport(0, action.dx, action.dy, 0))
                    is ScriptAction.MouseClick -> {
                        HidWriter.sendMouseReport(HidWriter.buildMouseReport(action.button, 0, 0, 0))
                        delay(50)
                        HidWriter.sendMouseReport(HidWriter.buildMouseReport(0, 0, 0, 0))
                    }
                    is ScriptAction.Delay -> delay(action.ms)
                    is ScriptAction.Type -> {
                        for (char in action.text) {
                            if (!isRunning.get()) break
                            val (mod, code) = HidKeyCode.charToHid(char)
                            HidWriter.sendKeyPress(mod, code)
                            delay(50)
                        }
                    }
                    null -> { /* Comment or empty line */ }
                }
            }
        } catch (e: IOException) {
            success = false
        } finally {
            isRunning.set(false)
            onComplete(success)
        }
    }

    private val modifierMap = mapOf(
        "SHIFT" to HidKeyCode.MOD_L_SHIFT,
        "CTRL" to HidKeyCode.MOD_L_CTRL,
        "ALT" to HidKeyCode.MOD_L_ALT,
        "META" to HidKeyCode.MOD_L_META
    )
}
