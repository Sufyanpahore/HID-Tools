package com.sufyan.hidtools.viewmodel

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import com.topjohnwu.superuser.Shell
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File

class HidCheckViewModel : ViewModel() {

    private val _isLoading = mutableStateOf(true)
    val isLoading: State<Boolean> = _isLoading

    private val _hasKeyboard = mutableStateOf(false)
    val hasKeyboard: State<Boolean> = _hasKeyboard

    private val _hasMouse = mutableStateOf(false)
    val hasMouse: State<Boolean> = _hasMouse

    private val _isRootAvailable = mutableStateOf(false)
    val isRootAvailable: State<Boolean> = _isRootAvailable

    fun performChecks(navController: NavController) {
        _isLoading.value = true
        viewModelScope.launch {
            val (keyboardExists, mouseExists, rootAvailable) = withContext(Dispatchers.IO) {
                val keyboard = File("/dev/hidg0").exists()
                val mouse = File("/dev/hidg1").exists()
                
                // This is the interactive root request.
                // It will trigger the system dialog asking the user to grant root.
                val root = try {
                    Shell.getShell()
                    Shell.isAppGrantedRoot() == true
                } catch (e: Exception) {
                    false
                }
                Triple(keyboard, mouse, root)
            }

            // Switch back to the main thread to update the UI state safely
            _hasKeyboard.value = keyboardExists
            _hasMouse.value = mouseExists
            _isRootAvailable.value = rootAvailable
            _isLoading.value = false

            if (keyboardExists && mouseExists && rootAvailable) {
                navController.navigate("main") {
                    popUpTo("splash") { inclusive = true }
                }
            }
        }
    }
}
