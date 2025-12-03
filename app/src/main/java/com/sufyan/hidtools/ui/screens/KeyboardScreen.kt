package com.sufyan.hidtools.ui.screens

import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.sufyan.hidtools.viewmodel.KeyboardMode
import com.sufyan.hidtools.viewmodel.KeyboardViewModel

@Composable
fun KeyboardScreen(viewModel: KeyboardViewModel = viewModel()) {

    val mode by viewModel.mode
    val script = viewModel.script

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(8.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceAround,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text = if (mode == KeyboardMode.LIVE) "Live Mode" else "Script Mode")
            Switch(checked = mode == KeyboardMode.SCRIPT, onCheckedChange = { viewModel.toggleMode() })
        }

        if (mode == KeyboardMode.SCRIPT) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .padding(vertical = 8.dp)
            ) {
                LazyColumn(modifier = Modifier.padding(8.dp)) {
                    items(script) { line ->
                        Text(line)
                    }
                }
            }
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End
            ) {
                Button(onClick = { viewModel.clearScript() }) {
                    Text("Clear Script")
                }
            }

        }

        Spacer(modifier = Modifier.height(16.dp))

        OnScreenKeyboard(viewModel = viewModel)
    }
}

@Composable
private fun OnScreenKeyboard(viewModel: KeyboardViewModel) {
    val isShiftPressed by viewModel.isShiftPressed
    val isCtrlPressed by viewModel.isCtrlPressed
    val isAltPressed by viewModel.isAltPressed

    val keyboardRows = listOf(
        "1234567890",
        "QWERTYUIOP",
        "ASDFGHJKL",
        "ZXCVBNM"
    )

    Column {
        // Modifier keys
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            ModifierKey(text = "Shift", isPressed = isShiftPressed, onClick = { viewModel.toggleShift() })
            ModifierKey(text = "Ctrl", isPressed = isCtrlPressed, onClick = { viewModel.toggleCtrl() })
            ModifierKey(text = "Alt", isPressed = isAltPressed, onClick = { viewModel.toggleAlt() })
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Alphanumeric keys
        keyboardRows.forEach { row ->
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center
            ) {
                row.forEach { char ->
                    KeyButton(text = char.toString(), onClick = { viewModel.onKeyClick(char) })
                }
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Special keys
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            SpecialKey(text = "Space", onClick = { viewModel.onKeyClick(' ') })
            SpecialKey(text = "Enter", onClick = { viewModel.onKeyClick('\n') })
            SpecialKey(text = "Bksp", onClick = { viewModel.onKeyClick('\b') })
        }
    }
}

@Composable
private fun KeyButton(text: String, onClick: () -> Unit) {
    val interactionSource = remember { MutableInteractionSource() }
    val haptic = LocalHapticFeedback.current

    Button(
        onClick = {
            onClick()
            haptic.performHapticFeedback(HapticFeedbackType.LongPress)
        },
        modifier = Modifier
            .padding(2.dp),
        interactionSource = interactionSource
    ) {
        Text(text = text)
    }
}

@Composable
private fun SpecialKey(text: String, onClick: () -> Unit) {
    val interactionSource = remember { MutableInteractionSource() }
    val haptic = LocalHapticFeedback.current

    Button(
        onClick = {
            onClick()
            haptic.performHapticFeedback(HapticFeedbackType.LongPress)
        },
        modifier = Modifier
            .padding(2.dp),
        interactionSource = interactionSource
    ) {
        Text(text = text)
    }
}

@Composable
private fun ModifierKey(text: String, isPressed: Boolean, onClick: () -> Unit) {
    val haptic = LocalHapticFeedback.current
    Button(
        onClick = {
            onClick()
            haptic.performHapticFeedback(HapticFeedbackType.LongPress)
        },
        modifier = Modifier.padding(horizontal = 4.dp),
    ) {
        Text(text = text, color = if (isPressed) MaterialTheme.colorScheme.tertiary else MaterialTheme.colorScheme.onPrimary)
    }
}

@Preview(showBackground = true)
@Composable
fun KeyboardScreenPreview() {
    KeyboardScreen()
}
