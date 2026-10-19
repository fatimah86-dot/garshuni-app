package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.example.ui.GarshuniViewModel
import com.example.ui.components.GarshuniBottomBar
import com.example.ui.components.NavItem
import com.example.ui.screens.HistoryScreen
import com.example.ui.screens.HomeScreen
import com.example.ui.screens.LibraryScreen
import com.example.ui.screens.ResultScreen
import com.example.ui.screens.ScannerScreen
import com.example.ui.screens.SettingsScreen
import com.example.ui.theme.GarshuniTheme

class MainActivity : ComponentActivity() {
    private val viewModel: GarshuniViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            GarshuniTheme {
                GarshuniApp(viewModel = viewModel)
            }
        }
    }
}

@Composable
fun GarshuniApp(viewModel: GarshuniViewModel) {
    var currentRoute by remember { mutableStateOf(NavItem.Home.route) }
    var previousRoute by remember { mutableStateOf(NavItem.Home.route) }

    val navigateTo: (String) -> Unit = { targetRoute ->
        if (currentRoute != targetRoute) {
            previousRoute = currentRoute
            currentRoute = targetRoute
        }
    }

    val navigateToResult: () -> Unit = {
        previousRoute = currentRoute
        currentRoute = "result"
    }

    // Predictive Back Handling
    BackHandler(enabled = currentRoute != NavItem.Home.route) {
        if (currentRoute == "result") {
            currentRoute = previousRoute
        } else {
            currentRoute = NavItem.Home.route
        }
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        bottomBar = {
            if (currentRoute != "result") {
                GarshuniBottomBar(
                    currentRoute = currentRoute,
                    onNavigate = { route -> navigateTo(route) }
                )
            }
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            when (currentRoute) {
                NavItem.Home.route -> {
                    HomeScreen(
                        viewModel = viewModel,
                        onNavigateToScanner = { navigateTo(NavItem.Scanner.route) },
                        onNavigateToResult = navigateToResult,
                        onNavigateToLibrary = { navigateTo(NavItem.Library.route) },
                        onNavigateToHistory = { navigateTo(NavItem.History.route) }
                    )
                }

                NavItem.Scanner.route -> {
                    ScannerScreen(
                        viewModel = viewModel,
                        onNavigateToResult = navigateToResult,
                        onBackClick = { navigateTo(NavItem.Home.route) }
                    )
                }

                NavItem.Library.route -> {
                    LibraryScreen(
                        viewModel = viewModel,
                        onNavigateToResult = navigateToResult
                    )
                }

                NavItem.History.route -> {
                    HistoryScreen(
                        viewModel = viewModel,
                        onNavigateToResult = navigateToResult
                    )
                }

                NavItem.Settings.route -> {
                    SettingsScreen(viewModel = viewModel)
                }

                "result" -> {
                    ResultScreen(
                        viewModel = viewModel,
                        onBackClick = {
                            currentRoute = previousRoute
                        }
                    )
                }
            }
        }
    }
}
