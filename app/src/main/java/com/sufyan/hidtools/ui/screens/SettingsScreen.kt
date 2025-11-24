package com.sufyan.hidtools.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.sufyan.hidtools.hid.HidWriter
import com.sufyan.hidtools.viewmodel.SettingsViewModel

@Composable
fun SettingsScreen(viewModel: SettingsViewModel = viewModel()) {

    var isLiveModeDefault by remember { mutableStateOf(true) }
    var autoCopyAssets by remember { mutableStateOf(true) }
    var mouseSensitivity by remember { mutableStateOf(1.0f) }
    var throttleMs by remember { mutableStateOf(16f) }
    var forceSimulate by remember { mutableStateOf(HidWriter.forceSimulateMode) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        // Preference Toggles
        PreferenceRow("Default to Live Mode", isLiveModeDefault) { isLiveModeDefault = it }
        PreferenceRow("Auto-copy assets on start", autoCopyAssets) { autoCopyAssets = it }
        PreferenceRow("Force Simulate Mode", forceSimulate) {
            forceSimulate = it
            HidWriter.forceSimulateMode = it
        }

        // Sliders
        SliderPreference("Mouse Sensitivity", mouseSensitivity, 0.5f..2.5f) { mouseSensitivity = it }
        SliderPreference("Mouse Throttle (ms)", throttleMs, 8f..64f) { throttleMs = it }

        Spacer(modifier = Modifier.height(24.dp))

        // Action Buttons
        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly) {
            Button(onClick = { viewModel.exportScripts() }) { Text("Export Scripts") }
            Button(onClick = { viewModel.clearScripts() }) { Text("Clear Scripts") }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Security Warning
        Card(
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.errorContainer
            ),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "Security Warning",
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onErrorContainer
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "This app requires root access to function. Running scripts with root privileges can be dangerous. Only run scripts from trusted sources. The developer is not responsible for any damage caused by the use of this application.",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onErrorContainer
                )
            }
        }
    }
}

@Composable
private fun PreferenceRow(text: String, isChecked: Boolean, onCheckedChange: (Boolean) -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(text)
        Switch(checked = isChecked, onCheckedChange = onCheckedChange)
    }
}

@Composable
private fun SliderPreference(text: String, value: Float, valueRange: ClosedFloatingPointRange<Float>, onValueChange: (Float) -> Unit) {
    Column(modifier = Modifier.padding(vertical = 8.dp)) {
        Text(text)
        Slider(value = value, onValueChange = onValueChange, valueRange = valueRange)
    }
}

@Preview(showBackground = true)
@Composable
fun SettingsScreenPreview() {
    SettingsScreen()
}
