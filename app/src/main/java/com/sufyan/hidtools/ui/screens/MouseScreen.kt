package com.sufyan.hidtools.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
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
import com.sufyan.hidtools.viewmodel.MouseMode
import com.sufyan.hidtools.viewmodel.MouseViewModel

@Composable
fun MouseScreen(viewModel: MouseViewModel = viewModel()) {

    val mode by viewModel.mode
    val script = viewModel.script
    val sensitivity by viewModel.sensitivity

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
            Text(text = if (mode == MouseMode.LIVE) "Live Mode" else "Script Mode")
            Switch(checked = mode == MouseMode.SCRIPT, onCheckedChange = { viewModel.toggleMode() })
        }

        if (mode == MouseMode.SCRIPT) {
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
        }

        Touchpad(viewModel = viewModel, modifier = Modifier.weight(2f))

        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = "Sensitivity")
            Slider(
                value = sensitivity,
                onValueChange = { viewModel.setSensitivity(it) },
                valueRange = 0.5f..2.5f
            )
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            Button(onClick = { viewModel.onLeftClick() }) {
                Text("Left Click")
            }
            Button(onClick = { viewModel.onRightClick() }) {
                Text("Right Click")
            }
        }

        if (mode == MouseMode.SCRIPT) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp),
                horizontalArrangement = Arrangement.End
            ) {
                Button(onClick = { viewModel.clearScript() }) {
                    Text("Clear Script")
                }
            }
        }
    }
}

@Composable
private fun Touchpad(viewModel: MouseViewModel, modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.surfaceVariant)
            .pointerInput(Unit) {
                detectDragGestures {
                    change, dragAmount ->
                    change.consume()
                    viewModel.onPointerMove(dragAmount.x, dragAmount.y)
                }
            }
    ) {
        Text("Touchpad", modifier = Modifier.align(Alignment.Center))
    }
}

@Preview(showBackground = true)
@Composable
fun MouseScreenPreview() {
    MouseScreen()
}
