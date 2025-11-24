package com.sufyan.hidtools

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.sufyan.hidtools.ui.screens.ComboScreen
import com.sufyan.hidtools.ui.screens.KeyboardScreen
import com.sufyan.hidtools.ui.screens.MainMenuScreen
import com.sufyan.hidtools.ui.screens.MouseScreen
import com.sufyan.hidtools.ui.screens.RecordScreen
import com.sufyan.hidtools.ui.screens.SettingsScreen
import com.sufyan.hidtools.ui.screens.SplashScreen

@Composable
fun NavGraph() {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = "splash") {
        composable("splash") { SplashScreen(navController) }
        composable("main") { MainMenuScreen(onSelect = { route -> navController.navigate(route) }) }
        composable("keyboard") { KeyboardScreen() }
        composable("mouse") { MouseScreen() }
        composable("combo") { ComboScreen() }
        composable("record") { RecordScreen() }
        composable("run") { /* TODO: Implement Run Screen */ }
        composable("settings") { SettingsScreen() }
    }
}
