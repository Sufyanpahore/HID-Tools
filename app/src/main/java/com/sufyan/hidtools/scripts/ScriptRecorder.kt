package com.sufyan.hidtools.scripts

import android.content.Context
import android.util.Log
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File
import java.io.IOException

object ScriptRecorder {

    private val script = mutableListOf<String>()
    private var isRecording = false

    fun start() {
        isRecording = true
    }

    fun pause() {
        isRecording = false
    }

    fun stop() {
        isRecording = false
    }

    fun clear() {
        script.clear()
    }

    fun getScript(): List<String> {
        return script.toList()
    }

    fun addAction(action: String) {
        if (isRecording) {
            script.add(action)
        }
    }

    suspend fun save(context: Context, filename: String): Boolean = withContext(Dispatchers.IO) {
        if (filename.isBlank()) {
            return@withContext false
        }

        val scriptsDir = File(context.filesDir, "scripts")
        if (!scriptsDir.exists()) {
            scriptsDir.mkdirs()
        }

        val file = File(scriptsDir, "$filename.txt")

        try {
            file.writeText(script.joinToString("\n"))
            Log.d("ScriptRecorder", "Script saved to ${file.absolutePath}")
            true
        } catch (e: IOException) {
            Log.e("ScriptRecorder", "Failed to save script", e)
            false
        }
    }

    fun isFileExists(context: Context, filename: String): Boolean {
        val scriptsDir = File(context.filesDir, "scripts")
        val file = File(scriptsDir, "$filename.txt")
        return file.exists()
    }

    fun addSampleTemplate() {
        if (isRecording) {
            script.add("// Opens a terminal and lists files")
            script.add("KEYDOWN META")
            script.add("KEY T")
            script.add("KEYUP META")
            script.add("DELAY 1000")
            script.add("TYPE ls -l /sdcard/")
            script.add("KEY ENTER")
        }
    }
}