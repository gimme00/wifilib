package com.selpic.wifilib.sample.ui

import androidx.ui.graphics.Color
import androidx.ui.material.ColorPalette

fun SimpleColorPalette(
    isDark: Boolean = false,
    primary: Color = Color(0xFF6200EE),
    primaryVariant: Color = primary,
    onPrimary: Color = Color.White,
    secondary: Color = primary,
    secondaryVariant: Color = primaryVariant,
    onSecondary: Color = onPrimary,
    background: Color = if (isDark) Color.Black else Color.White,
    onBackground: Color = if (isDark) Color.White else Color.Black,
    surface: Color = background,
    onSurface: Color = onBackground,
    error: Color = Color(0xFFB00020),
    onError: Color = Color.White
) = ColorPalette(
    primary = primary,
    primaryVariant = primaryVariant,
    secondary = secondary,
    secondaryVariant = secondaryVariant,
    background = background,
    surface = surface,
    error = error,
    onPrimary = onPrimary,
    onSecondary = onSecondary,
    onBackground = onBackground,
    onSurface = onSurface,
    onError = onError
)

private val primaryColor = Color(0xff149cd3)
val lightThemeColors = SimpleColorPalette(
    isDark = false,
    primary = primaryColor
)
val darkThemeColors = SimpleColorPalette(
    isDark = true,
    primary = primaryColor
)

val emphasisHighTypeOpacity = 0.87f
val emphasisMediumOpacity = 0.60f
//val emphasisLowTypeOpacity = 0.45f
val emphasisDisabledOpacity = 0.38f
val dividerOpacity = 0.1f