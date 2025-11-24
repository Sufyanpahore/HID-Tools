package com.sufyan.hidtools.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.sufyan.hidtools.hid.HidKeyCode
import com.sufyan.hidtools.viewmodel.ComboMode
import com.sufyan.hidtools.viewmodel.ComboViewModel

@Composable
fun ComboScreen(viewModel: ComboViewModel = viewModel()) {

    val mode by viewModel.mode

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
            Text(text = if (mode == ComboMode.KEYBOARD) "Keyboard Mode" else "Mouse Mode")
            Switch(checked = mode == ComboMode.MOUSE, onCheckedChange = { viewModel.toggleMode() })
        }

        Box(modifier = Modifier
            .fillMaxWidth()
            .weight(1f)
            .padding(vertical = 8.dp)
        ) {
            if (mode == ComboMode.KEYBOARD) {
                OnScreenKeyboard(viewModel)
            } else {
                Touchpad(viewModel)
            }
        }

        ShortcutButtons(viewModel)
    }
}

@Composable
private fun OnScreenKeyboard(viewModel: ComboViewModel) {
    val keyboardRows = listOf(
        "QWERTYUIOP",
        "ASDFGHJKL",
        "ZXCVBNM"
    )

    Column {
        keyboardRows.forEach { row ->
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center
            ) {
                row.forEach { char ->
                    Button(
                        onClick = { viewModel.onKeyClick(char) },
                        modifier = Modifier.padding(2.dp)
                    ) {
                        Text(text = char.toString())
                    }
                }
            }
        }
    }
}

@Composable
private fun Touchpad(viewModel: ComboViewModel) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.surfaceVariant)
            .pointerInput(Unit) {
                detectDragGestures { change, dragAmount ->
                    change.consume()
                    viewModel.onPointerMove(dragAmount.x, dragAmount.y)
                }
            }
    ) {
        Text("Touchpad", modifier = Modifier.align(Alignment.Center))
    }
}

@Composable
private fun ShortcutButtons(viewModel: ComboViewModel) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        Button(onClick = { viewModel.sendShortcut(HidKeyCode.MOD_L_CTRL, 'c') }) {
            Text("Ctrl+C")
        }
        Button(onClick = { viewModel.sendShortcut(HidKeyCode.MOD_L_CTRL, 'v') }) {
            Text("Ctrl+V")
        }
        Button(onClick = { viewModel.sendShortcut(HidKeyCode.MOD_L_CTRL, 'x') }) {
            Text("Ctrl+X")
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ComboScreenPreview() {
    ComboScreen()
}
