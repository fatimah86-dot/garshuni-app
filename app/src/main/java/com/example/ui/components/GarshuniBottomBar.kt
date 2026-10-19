package com.example.ui.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.MenuBook
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import com.example.ui.theme.EmeraldDark
import com.example.ui.theme.EmeraldDeep
import com.example.ui.theme.GoldAccent
import com.example.ui.theme.GoldLight
import com.example.ui.theme.ParchmentSurface

sealed class NavItem(
    val route: String,
    val title: String,
    val icon: ImageVector,
    val testTag: String
) {
    data object Home : NavItem("home", "Beranda", Icons.Default.Home, "bottom_nav_home")
    data object Scanner : NavItem("scanner", "Pindai", Icons.Default.CameraAlt, "bottom_nav_scanner")
    data object Library : NavItem("library", "Perpustakaan", Icons.AutoMirrored.Filled.MenuBook, "bottom_nav_library")
    data object History : NavItem("history", "Riwayat", Icons.Default.History, "bottom_nav_history")
    data object Settings : NavItem("settings", "Pengaturan", Icons.Default.Settings, "bottom_nav_settings")
}

@Composable
fun GarshuniBottomBar(
    currentRoute: String,
    onNavigate: (String) -> Unit
) {
    val items = listOf(
        NavItem.Home,
        NavItem.Scanner,
        NavItem.Library,
        NavItem.History,
        NavItem.Settings
    )

    NavigationBar(
        containerColor = EmeraldDeep,
        tonalElevation = 8.dp
    ) {
        items.forEach { item ->
            val isSelected = currentRoute == item.route
            NavigationBarItem(
                selected = isSelected,
                onClick = { onNavigate(item.route) },
                icon = {
                    Icon(
                        imageVector = item.icon,
                        contentDescription = item.title
                    )
                },
                label = {
                    Text(
                        text = item.title,
                        style = MaterialTheme.typography.labelSmall
                    )
                },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = EmeraldDeep,
                    unselectedIconColor = ParchmentSurface.copy(alpha = 0.7f),
                    selectedTextColor = GoldLight,
                    unselectedTextColor = ParchmentSurface.copy(alpha = 0.7f),
                    indicatorColor = GoldAccent
                ),
                modifier = Modifier.testTag(item.testTag)
            )
        }
    }
}
