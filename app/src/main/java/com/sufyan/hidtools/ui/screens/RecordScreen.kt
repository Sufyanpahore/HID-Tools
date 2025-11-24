package com.sufyan.hidtools.ui.screens

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
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.sufyan.hidtools.scripts.ScriptRecorder
import kotlinx.coroutines.launch

@Composable
fun RecordScreen() {
    var scriptName by remember { mutableStateOf("") }
    var showOverwriteDialog by remember { mutableStateOf(false) }
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val script by remember { mutableStateOf(ScriptRecorder.getScript()) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        // Recording Controls
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            Button(onClick = { ScriptRecorder.start() }) { Text("Start") }
            Button(onClick = { ScriptRecorder.pause() }) { Text("Pause") }
            Button(onClick = { ScriptRecorder.stop() }) { Text("Stop") }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Script Name and Save
        OutlinedTextField(
            value = scriptName,
            onValueChange = { scriptName = it },
            label = { Text("Script Name") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(8.dp))
        Button(
            onClick = {
                if (ScriptRecorder.isFileExists(context, scriptName)) {
                    showOverwriteDialog = true
                } else {
                    scope.launch { ScriptRecorder.save(context, scriptName) }
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Save Script")
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Script Preview
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
        ) {
            LazyColumn(modifier = Modifier.padding(8.dp)) {
                items(script) { line ->
                    Text(line)
                }
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Other controls
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Button(onClick = { ScriptRecorder.addSampleTemplate() }) { Text("Sample Template") }
            Button(onClick = { ScriptRecorder.clear() }) { Text("Clear") }
        }

        // Overwrite Confirmation Dialog
        if (showOverwriteDialog) {
            AlertDialog(
                onDismissRequest = { showOverwriteDialog = false },
                title = { Text("Overwrite File?") },
                text = { Text("A script with this name already exists. Do you want to overwrite it?") },
                confirmButton = {
                    TextButton(
                        onClick = {
                            scope.launch { ScriptRecorder.save(context, scriptName) }
                            showOverwriteDialog = false
                        }
                    ) {
                        Text("Overwrite")
                    }
                },
                dismissButton = {
                    TextButton(onClick = { showOverwriteDialog = false }) {
                        Text("Cancel")
                    }
                }
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun RecordScreenPreview() {
    RecordScreen()
}
