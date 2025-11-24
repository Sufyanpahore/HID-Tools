package com.sufyan.hidtools.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.sufyan.hidtools.viewmodel.HidCheckViewModel

@Composable
fun SplashScreen(
    navController: NavController,
    viewModel: HidCheckViewModel = viewModel()
) {
    val isLoading by viewModel.isLoading
    val hasKeyboard by viewModel.hasKeyboard
    val hasMouse by viewModel.hasMouse
    val isRootAvailable by viewModel.isRootAvailable

    // Trigger the checks when the splash screen is first composed.
    LaunchedEffect(Unit) {
        viewModel.performChecks(navController)
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        contentAlignment = Alignment.Center
    ) {
        if (isLoading) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                CircularProgressIndicator()
                Spacer(modifier = Modifier.height(16.dp))
                Text(text = "Checking device status...")
            }
        } else {
            if (!hasKeyboard || !hasMouse) {
                ErrorCard(viewModel, navController)
            } else if (!isRootAvailable) {
                Text(
                    text = "Root access is required to use this application.",
                    style = MaterialTheme.typography.titleMedium,
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}

@Composable
private fun ErrorCard(viewModel: HidCheckViewModel, navController: NavController) {
    Card(modifier = Modifier.padding(16.dp)) {
        Column(
            modifier = Modifier.padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Enable HID Gadget in NetHunter USB Arsenal",
                style = MaterialTheme.typography.titleMedium,
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "This app requires /dev/hidg0 and /dev/hidg1. Please enable them in your device settings.",
                style = MaterialTheme.typography.bodySmall,
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(16.dp))
            Button(onClick = { viewModel.performChecks(navController) }) {
                Text("Retry")
            }
        }
    }
}
