package com.sufyan.hidtools.ui.screens

import androidx.compose.foundation.clickable
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
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.sufyan.hidtools.viewmodel.RunScriptViewModel

@Composable
fun RunScriptScreen(viewModel: RunScriptViewModel = viewModel()) {
    val context = LocalContext.current
    val scripts = viewModel.scripts
    val selectedScript by viewModel.selectedScript
    val scriptContent = viewModel.scriptContent
    val executionStatus by viewModel.executionStatus
    val isRunning by viewModel.isRunning
    val progress by viewModel.progress

    LaunchedEffect(Unit) {
        viewModel.loadScripts(context.filesDir)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(
            text = "Select a Script",
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(8.dp))

        // Script List
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .weight(0.4f),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
        ) {
            if (scripts.isEmpty()) {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text("No scripts found.")
                }
            } else {
                LazyColumn(modifier = Modifier.padding(8.dp)) {
                    items(scripts) { script ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable { viewModel.selectScript(context.filesDir, script) }
                                .padding(vertical = 8.dp, horizontal = 4.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = script,
                                style = MaterialTheme.typography.bodyLarge,
                                fontWeight = if (selectedScript == script) FontWeight.Bold else FontWeight.Normal,
                                color = if (selectedScript == script) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface
                            )
                        }
                        Divider()
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Script Preview & Controls
        if (selectedScript != null) {
            Text(
                text = "Preview: $selectedScript",
                style = MaterialTheme.typography.titleMedium
            )

            Spacer(modifier = Modifier.height(8.dp))

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(0.6f),
                 elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                LazyColumn(modifier = Modifier.padding(8.dp)) {
                    items(scriptContent) { line ->
                        Text(
                            text = line,
                            style = MaterialTheme.typography.bodyMedium,
                            color = if (isRunning && executionStatus.contains(line)) MaterialTheme.colorScheme.tertiary else MaterialTheme.colorScheme.onSurface
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            if (isRunning) {
                LinearProgressIndicator(
                    progress = progress,
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(8.dp))
            }

            Text(
                text = "Status: $executionStatus",
                style = MaterialTheme.typography.bodySmall
            )

            Spacer(modifier = Modifier.height(8.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                Button(
                    onClick = { viewModel.runSelectedScript(context.filesDir) },
                    enabled = !isRunning
                ) {
                    Text("Run Script")
                }

                Button(
                    onClick = { viewModel.stopScript() },
                    enabled = isRunning,
                    colors = androidx.compose.material3.ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error)
                ) {
                    Text("Stop")
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun RunScriptScreenPreview() {
    RunScriptScreen()
}
