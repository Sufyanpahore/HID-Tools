package com.sufyan.hidtools.ui.screens

import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.sufyan.hidtools.hid.HidKeyCode
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
    val isMetaPressed by viewModel.isMetaPressed

    Column(
        verticalArrangement = Arrangement.spacedBy(6.dp)
    ) {
        // Function Keys Row
        KeyboardRow {
            KeyButton(text = "Esc", weight = 1f, onClick = { viewModel.onSpecialKeyClick(HidKeyCode.KEY_ESCAPE) })
            KeyButton(text = "F1", weight = 1f, onClick = { viewModel.onSpecialKeyClick(HidKeyCode.KEY_F1) })
            KeyButton(text = "F2", weight = 1f, onClick = { viewModel.onSpecialKeyClick(HidKeyCode.KEY_F2) })
            KeyButton(text = "F3", weight = 1f, onClick = { viewModel.onSpecialKeyClick(HidKeyCode.KEY_F3) })
            KeyButton(text = "F4", weight = 1f, onClick = { viewModel.onSpecialKeyClick(HidKeyCode.KEY_F4) })
            KeyButton(text = "F5", weight = 1f, onClick = { viewModel.onSpecialKeyClick(HidKeyCode.KEY_F5) })
            KeyButton(text = "F6", weight = 1f, onClick = { viewModel.onSpecialKeyClick(HidKeyCode.KEY_F6) })
            KeyButton(text = "F7", weight = 1f, onClick = { viewModel.onSpecialKeyClick(HidKeyCode.KEY_F7) })
            KeyButton(text = "F8", weight = 1f, onClick = { viewModel.onSpecialKeyClick(HidKeyCode.KEY_F8) })
            KeyButton(text = "F9", weight = 1f, onClick = { viewModel.onSpecialKeyClick(HidKeyCode.KEY_F9) })
            KeyButton(text = "F10", weight = 1f, onClick = { viewModel.onSpecialKeyClick(HidKeyCode.KEY_F10) })
            KeyButton(text = "F11", weight = 1f, onClick = { viewModel.onSpecialKeyClick(HidKeyCode.KEY_F11) })
            KeyButton(text = "F12", weight = 1f, onClick = { viewModel.onSpecialKeyClick(HidKeyCode.KEY_F12) })
            KeyButton(text = "Del", weight = 1f, onClick = { viewModel.onSpecialKeyClick(HidKeyCode.KEY_DELETE) })
        }

        // Number Row
        KeyboardRow {
            KeyButton(text = "`", weight = 1f, onClick = { viewModel.onKeyClick('`') })
            KeyButton(text = "1", weight = 1f, onClick = { viewModel.onKeyClick('1') })
            KeyButton(text = "2", weight = 1f, onClick = { viewModel.onKeyClick('2') })
            KeyButton(text = "3", weight = 1f, onClick = { viewModel.onKeyClick('3') })
            KeyButton(text = "4", weight = 1f, onClick = { viewModel.onKeyClick('4') })
            KeyButton(text = "5", weight = 1f, onClick = { viewModel.onKeyClick('5') })
            KeyButton(text = "6", weight = 1f, onClick = { viewModel.onKeyClick('6') })
            KeyButton(text = "7", weight = 1f, onClick = { viewModel.onKeyClick('7') })
            KeyButton(text = "8", weight = 1f, onClick = { viewModel.onKeyClick('8') })
            KeyButton(text = "9", weight = 1f, onClick = { viewModel.onKeyClick('9') })
            KeyButton(text = "0", weight = 1f, onClick = { viewModel.onKeyClick('0') })
            KeyButton(text = "-", weight = 1f, onClick = { viewModel.onKeyClick('-') })
            KeyButton(text = "=", weight = 1f, onClick = { viewModel.onKeyClick('=') })
            KeyButton(text = "Bksp", weight = 2f, onClick = { viewModel.onSpecialKeyClick(HidKeyCode.KEY_BACKSPACE) })
        }

        // QWERTY Row
        KeyboardRow {
            KeyButton(text = "Tab", weight = 1.5f, onClick = { viewModel.onSpecialKeyClick(HidKeyCode.KEY_TAB) })
            KeyButton(text = "Q", weight = 1f, onClick = { viewModel.onKeyClick('q') })
            KeyButton(text = "W", weight = 1f, onClick = { viewModel.onKeyClick('w') })
            KeyButton(text = "E", weight = 1f, onClick = { viewModel.onKeyClick('e') })
            KeyButton(text = "R", weight = 1f, onClick = { viewModel.onKeyClick('r') })
            KeyButton(text = "T", weight = 1f, onClick = { viewModel.onKeyClick('t') })
            KeyButton(text = "Y", weight = 1f, onClick = { viewModel.onKeyClick('y') })
            KeyButton(text = "U", weight = 1f, onClick = { viewModel.onKeyClick('u') })
            KeyButton(text = "I", weight = 1f, onClick = { viewModel.onKeyClick('i') })
            KeyButton(text = "O", weight = 1f, onClick = { viewModel.onKeyClick('o') })
            KeyButton(text = "P", weight = 1f, onClick = { viewModel.onKeyClick('p') })
            KeyButton(text = "[", weight = 1f, onClick = { viewModel.onKeyClick('[') })
            KeyButton(text = "]", weight = 1f, onClick = { viewModel.onKeyClick(']') })
            KeyButton(text = "\\", weight = 1.5f, onClick = { viewModel.onKeyClick('\\') })
        }

        // ASDF Row
        KeyboardRow {
            KeyButton(text = "Caps", weight = 1.8f, onClick = { viewModel.onSpecialKeyClick(HidKeyCode.KEY_CAPSLOCK) })
            KeyButton(text = "A", weight = 1f, onClick = { viewModel.onKeyClick('a') })
            KeyButton(text = "S", weight = 1f, onClick = { viewModel.onKeyClick('s') })
            KeyButton(text = "D", weight = 1f, onClick = { viewModel.onKeyClick('d') })
            KeyButton(text = "F", weight = 1f, onClick = { viewModel.onKeyClick('f') })
            KeyButton(text = "G", weight = 1f, onClick = { viewModel.onKeyClick('g') })
            KeyButton(text = "H", weight = 1f, onClick = { viewModel.onKeyClick('h') })
            KeyButton(text = "J", weight = 1f, onClick = { viewModel.onKeyClick('j') })
            KeyButton(text = "K", weight = 1f, onClick = { viewModel.onKeyClick('k') })
            KeyButton(text = "L", weight = 1f, onClick = { viewModel.onKeyClick('l') })
            KeyButton(text = ";", weight = 1f, onClick = { viewModel.onKeyClick(';') })
            KeyButton(text = "'", weight = 1f, onClick = { viewModel.onKeyClick('\'') })
            KeyButton(text = "Enter", weight = 2.2f, onClick = { viewModel.onSpecialKeyClick(HidKeyCode.KEY_ENTER) })
        }

        // ZXCV Row
        KeyboardRow {
            ModifierKey(text = "Shift", isPressed = isShiftPressed, weight = 2.4f, onClick = { viewModel.toggleShift() })
            KeyButton(text = "Z", weight = 1f, onClick = { viewModel.onKeyClick('z') })
            KeyButton(text = "X", weight = 1f, onClick = { viewModel.onKeyClick('x') })
            KeyButton(text = "C", weight = 1f, onClick = { viewModel.onKeyClick('c') })
            KeyButton(text = "V", weight = 1f, onClick = { viewModel.onKeyClick('v') })
            KeyButton(text = "B", weight = 1f, onClick = { viewModel.onKeyClick('b') })
            KeyButton(text = "N", weight = 1f, onClick = { viewModel.onKeyClick('n') })
            KeyButton(text = "M", weight = 1f, onClick = { viewModel.onKeyClick('m') })
            KeyButton(text = ",", weight = 1f, onClick = { viewModel.onKeyClick(',') })
            KeyButton(text = ".", weight = 1f, onClick = { viewModel.onKeyClick('.') })
            KeyButton(text = "/", weight = 1f, onClick = { viewModel.onKeyClick('/') })
            // Right Shift acts same as Left Shift for simplicity in this app
            ModifierKey(text = "Shift", isPressed = isShiftPressed, weight = 2.4f, onClick = { viewModel.toggleShift() })
        }

        // Bottom Row
        KeyboardRow {
            ModifierKey(text = "Ctrl", isPressed = isCtrlPressed, weight = 1.2f, onClick = { viewModel.toggleCtrl() })
            ModifierKey(text = "Win", isPressed = isMetaPressed, weight = 1.2f, onClick = { viewModel.toggleMeta() })
            ModifierKey(text = "Alt", isPressed = isAltPressed, weight = 1.2f, onClick = { viewModel.toggleAlt() })
            KeyButton(text = "Space", weight = 6f, onClick = { viewModel.onSpecialKeyClick(HidKeyCode.KEY_SPACE) })
            ModifierKey(text = "Alt", isPressed = isAltPressed, weight = 1.2f, onClick = { viewModel.toggleAlt() })
            ModifierKey(text = "Fn", isPressed = false, weight = 1.2f, onClick = { /* TODO: Toggle Fn layer */ })
            ModifierKey(text = "Ctrl", isPressed = isCtrlPressed, weight = 1.2f, onClick = { viewModel.toggleCtrl() })
        }

        // Navigation Row
        KeyboardRow {
           Spacer(modifier = Modifier.weight(1f))
           KeyButton(text = "Ins", weight = 1f, onClick = { viewModel.onSpecialKeyClick(HidKeyCode.KEY_INSERT) })
           KeyButton(text = "Home", weight = 1f, onClick = { viewModel.onSpecialKeyClick(HidKeyCode.KEY_HOME) })
           KeyButton(text = "PgUp", weight = 1f, onClick = { viewModel.onSpecialKeyClick(HidKeyCode.KEY_PAGEUP) })
           Spacer(modifier = Modifier.weight(0.5f))
           KeyButton(text = "▲", weight = 1f, onClick = { viewModel.onSpecialKeyClick(HidKeyCode.KEY_ARROW_UP) })
           Spacer(modifier = Modifier.weight(2.5f)) // Balance the row
        }

         KeyboardRow {
           Spacer(modifier = Modifier.weight(1f))
           KeyButton(text = "Del", weight = 1f, onClick = { viewModel.onSpecialKeyClick(HidKeyCode.KEY_DELETE) })
           KeyButton(text = "End", weight = 1f, onClick = { viewModel.onSpecialKeyClick(HidKeyCode.KEY_END) })
           KeyButton(text = "PgDn", weight = 1f, onClick = { viewModel.onSpecialKeyClick(HidKeyCode.KEY_PAGEDOWN) })
           Spacer(modifier = Modifier.weight(0.5f))
           KeyButton(text = "◄", weight = 1f, onClick = { viewModel.onSpecialKeyClick(HidKeyCode.KEY_ARROW_LEFT) })
           KeyButton(text = "▼", weight = 1f, onClick = { viewModel.onSpecialKeyClick(HidKeyCode.KEY_ARROW_DOWN) })
           KeyButton(text = "►", weight = 1f, onClick = { viewModel.onSpecialKeyClick(HidKeyCode.KEY_ARROW_RIGHT) })
        }
    }
}

@Composable
fun KeyboardRow(content: @Composable RowScope.() -> Unit) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(4.dp),
        content = content
    )
}

@Composable
fun RowScope.KeyButton(text: String, weight: Float = 1f, onClick: () -> Unit) {
    val interactionSource = remember { MutableInteractionSource() }
    val haptic = LocalHapticFeedback.current

    Button(
        onClick = {
            onClick()
            haptic.performHapticFeedback(HapticFeedbackType.LongPress)
        },
        modifier = Modifier
            .weight(weight)
            .height(42.dp),
        shape = MaterialTheme.shapes.small,
        contentPadding = androidx.compose.foundation.layout.PaddingValues(1.dp),
        interactionSource = interactionSource
    ) {
        Text(text = text, fontSize = 11.sp, maxLines = 1)
    }
}

@Composable
fun RowScope.ModifierKey(text: String, isPressed: Boolean, weight: Float = 1f, onClick: () -> Unit) {
    val haptic = LocalHapticFeedback.current
    Button(
        onClick = {
            onClick()
            haptic.performHapticFeedback(HapticFeedbackType.LongPress)
        },
        modifier = Modifier
            .weight(weight)
            .height(42.dp),
        shape = MaterialTheme.shapes.small,
        contentPadding = androidx.compose.foundation.layout.PaddingValues(1.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = if (isPressed) MaterialTheme.colorScheme.tertiary else MaterialTheme.colorScheme.secondary
        )
    ) {
        Text(text = text, fontSize = 11.sp, maxLines = 1)
    }
}

@Preview(showBackground = true)
@Composable
fun KeyboardScreenPreview() {
    KeyboardScreen()
}
