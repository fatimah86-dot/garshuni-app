package com.example.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

private val GarshuniLightColorScheme = lightColorScheme(
    primary = EmeraldDeep,
    onPrimary = ParchmentSurface,
    primaryContainer = EmeraldLight,
    onPrimaryContainer = ParchmentSurface,
    secondary = GoldAccent,
    onSecondary = TextPrimaryDark,
    secondaryContainer = GoldLight,
    onSecondaryContainer = TextPrimaryDark,
    tertiary = GoldDark,
    onTertiary = ParchmentSurface,
    background = ParchmentBg,
    onBackground = TextPrimaryDark,
    surface = ParchmentSurface,
    onSurface = TextPrimaryDark,
    surfaceVariant = ParchmentVariant,
    onSurfaceVariant = TextSecondaryDark,
    outline = ParchmentBorder
)

private val GarshuniDarkColorScheme = darkColorScheme(
    primary = GoldAccent,
    onPrimary = DarkEmeraldBg,
    primaryContainer = EmeraldDeep,
    onPrimaryContainer = GoldLight,
    secondary = GoldLight,
    onSecondary = DarkEmeraldBg,
    secondaryContainer = DarkEmeraldVariant,
    onSecondaryContainer = DarkTextPrimary,
    tertiary = EmeraldLight,
    onTertiary = DarkTextPrimary,
    background = DarkEmeraldBg,
    onBackground = DarkTextPrimary,
    surface = DarkEmeraldSurface,
    onSurface = DarkTextPrimary,
    surfaceVariant = DarkEmeraldVariant,
    onSurfaceVariant = DarkTextSecondary,
    outline = GoldDark
)

@Composable
fun GarshuniTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) GarshuniDarkColorScheme else GarshuniLightColorScheme

    MaterialTheme(
        colorScheme = colorScheme,
        typography = GarshuniTypography,
        content = content
    )
}
