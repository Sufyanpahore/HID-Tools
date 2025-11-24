package com.sufyan.hidtools.ui.components

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable

/**
 * A composable that shows a confirmation dialog before running a script.
 *
 * @param scriptName The name of the script to be run.
 * @param estimatedRuntime The estimated runtime of the script in seconds.
 * @param onConfirm The action to perform when the user confirms.
 * @param onDismiss The action to perform when the user dismisses the dialog.
 */
@Composable
fun ConfirmScriptRunDialog(
    scriptName: String,
    estimatedRuntime: Long,
    onConfirm: () -> Unit,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Run Script?") },
        text = { Text("Are you sure you want to run '$scriptName'?\nEstimated runtime: $estimatedRuntime seconds.") },
        confirmButton = {
            TextButton(onClick = onConfirm) { Text("Run") }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text("Cancel") }
        }
    )
}

/**
 * A composable that shows a dialog to request root access for the first time.
 *
 * @param onConfirm The action to perform when the user allows root access.
 * @param onDismiss The action to perform when the user denies root access.
 */
@Composable
fun RootRequestDialog(onConfirm: () -> Unit, onDismiss: () -> Unit) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Root Access Required") },
        text = { Text("This app requires root access to send HID commands to the kernel. Please grant root access to continue.") },
        confirmButton = {
            TextButton(onClick = onConfirm) { Text("Allow") }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text("Deny") }
        }
    )
}
