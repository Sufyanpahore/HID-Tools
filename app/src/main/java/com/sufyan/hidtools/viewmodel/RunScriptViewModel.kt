package com.sufyan.hidtools.viewmodel

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sufyan.hidtools.scripts.ScriptRunner
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File

class RunScriptViewModel : ViewModel() {

    private val _scripts = mutableStateListOf<String>()
    val scripts: List<String> get() = _scripts

    private val _selectedScript = mutableStateOf<String?>(null)
    val selectedScript: State<String?> = _selectedScript

    private val _scriptContent = mutableStateListOf<String>()
    val scriptContent: List<String> get() = _scriptContent

    private val _executionStatus = mutableStateOf("Idle")
    val executionStatus: State<String> = _executionStatus

    private val _isRunning = mutableStateOf(false)
    val isRunning: State<Boolean> = _isRunning

    private val _progress = mutableStateOf(0f)
    val progress: State<Float> = _progress

    fun loadScripts(filesDir: File) {
        viewModelScope.launch(Dispatchers.IO) {
            val scriptsDir = File(filesDir, "scripts")
            if (scriptsDir.exists()) {
                val scriptFiles = scriptsDir.listFiles { _, name -> name.endsWith(".txt") }
                    ?.map { it.nameWithoutExtension }
                    ?.sorted() ?: emptyList()

                withContext(Dispatchers.Main) {
                    _scripts.clear()
                    _scripts.addAll(scriptFiles)
                }
            }
        }
    }

    fun selectScript(filesDir: File, scriptName: String) {
        _selectedScript.value = scriptName
        viewModelScope.launch(Dispatchers.IO) {
            val scriptsDir = File(filesDir, "scripts")
            val file = File(scriptsDir, "$scriptName.txt")
            if (file.exists()) {
                val lines = file.readLines()
                withContext(Dispatchers.Main) {
                    _scriptContent.clear()
                    _scriptContent.addAll(lines)
                }
            }
        }
    }

    fun runSelectedScript(filesDir: File) {
        val scriptName = _selectedScript.value ?: return
        if (_isRunning.value) return

        _isRunning.value = true
        _executionStatus.value = "Running..."
        _progress.value = 0f

        viewModelScope.launch {
            val scriptsDir = File(filesDir, "scripts")
            val file = File(scriptsDir, "$scriptName.txt")

            // Get total lines for progress calculation
            val totalLines = _scriptContent.size

            ScriptRunner.runScript(
                scriptFile = file,
                onProgress = { index, line ->
                    withContext(Dispatchers.Main) {
                        _executionStatus.value = "Executing: $line"
                        if (totalLines > 0) {
                            _progress.value = (index + 1).toFloat() / totalLines
                        }
                    }
                },
                onComplete = { success ->
                    withContext(Dispatchers.Main) {
                        _isRunning.value = false
                        _executionStatus.value = if (success) "Completed Successfully" else "Execution Failed or Stopped"
                        _progress.value = if (success) 1f else 0f
                    }
                }
            )
        }
    }

    fun stopScript() {
        ScriptRunner.stop()
        _isRunning.value = false
        _executionStatus.value = "Stopped"
    }
}
