package com.sufyan.hidtools.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Keyboard
import androidx.compose.material.icons.filled.Mouse
import androidx.compose.material.icons.filled.Security
import androidx.compose.material3.Button
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.sufyan.hidtools.R
import com.sufyan.hidtools.viewmodel.HidCheckViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainMenuScreen(
    onSelect: (String) -> Unit,
    viewModel: HidCheckViewModel = viewModel()
) {
    val hasKeyboard by viewModel.hasKeyboard
    val hasMouse by viewModel.hasMouse
    val isRootAvailable by viewModel.isRootAvailable

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("HID Tools") },
                actions = {
                    Row {
                        if (hasKeyboard) {
                            Icon(
                                Icons.Default.Keyboard,
                                contentDescription = stringResource(R.string.keyboard_enabled_desc)
                            )
                        }
                        if (hasMouse) {
                            Icon(
                                Icons.Default.Mouse,
                                contentDescription = stringResource(R.string.mouse_enabled_desc)
                            )
                        }
                        if (isRootAvailable) {
                            Icon(
                                Icons.Default.Security,
                                contentDescription = stringResource(R.string.root_available_desc)
                            )
                        }
                    }
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer
                )
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .padding(16.dp)
                .fillMaxWidth()
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            MenuButton(text = "Keyboard Only", route = "keyboard", onSelect = onSelect)
            MenuButton(text = "Mouse Only", route = "mouse", onSelect = onSelect)
            MenuButton(text = "Keyboard + Mouse", route = "combo", onSelect = onSelect)
            MenuButton(text = "Record Script", route = "record", onSelect = onSelect)
            MenuButton(text = "Run Script", route = "run", onSelect = onSelect)
            MenuButton(text = "Settings", route = "settings", onSelect = onSelect)
        }
    }
}

@Composable
private fun MenuButton(text: String, route: String, onSelect: (String) -> Unit) {
    Button(
        onClick = { onSelect(route) },
        modifier = Modifier
            .fillMaxWidth()
            .height(56.dp) // Increased height for better touch target
            .padding(vertical = 8.dp)
    ) {
        Text(text, style = MaterialTheme.typography.labelLarge) // Support font scaling
    }
    Spacer(modifier = Modifier.height(8.dp))
}

@Preview(showBackground = true)
@Composable
fun MainMenuScreenPreview() {
    MainMenuScreen(onSelect = {})
}
