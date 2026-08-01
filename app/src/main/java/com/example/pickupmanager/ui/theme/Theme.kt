package com.example.pickupmanager.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val DarkColorScheme = darkColorScheme(
    primary = DarkIndigo,
    onPrimary = Color(0xFF20205A),
    primaryContainer = Color(0xFF33336F),
    secondary = DarkMint,
    secondaryContainer = Color(0xFF174E43),
    background = DarkBackground,
    surface = DarkSurface,
    surfaceVariant = Color(0xFF292832),
    outline = Color(0xFF908F9A)
)

private val LightColorScheme = lightColorScheme(
    primary = Indigo,
    onPrimary = Color.White,
    primaryContainer = IndigoSoft,
    onPrimaryContainer = IndigoDark,
    secondary = Mint,
    onSecondary = Color.White,
    secondaryContainer = MintSoft,
    onSecondaryContainer = Color(0xFF075F50),
    background = WarmBackground,
    onBackground = Ink,
    surface = CardWhite,
    onSurface = Ink,
    surfaceVariant = Color(0xFFF0EFF6),
    onSurfaceVariant = MutedInk,
    outline = Color(0xFF797783),
    outlineVariant = OutlineSoft,
    error = Color(0xFFBA1A1A)
)

@Composable
fun PickupManagerTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme,
        typography = Typography,
        content = content
    )
}
