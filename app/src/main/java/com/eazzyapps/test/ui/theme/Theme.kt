package com.eazzyapps.test.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.MaterialTheme
import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable

private val DarkColorPalette = darkColors(
    primary = blue500,
    primaryVariant = lightGreen500,
    onPrimary = white,

    secondary = darkPink800,
    onSecondary = white,

    background = black,
    onBackground = white,

    surface = grey800,
    onSurface = grey200,
)

private val LightColorPalette = lightColors(
    primary = blue500,
    primaryVariant = lightGreen500,
    onPrimary = black,

    secondary = darkPink800,
    secondaryVariant = blue200,
    onSecondary = black,

    background = white,
    onBackground = black,

    surface = grey200,
    onSurface = grey800,
)

@Composable
fun AppTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {

    MaterialTheme(
        colors = if (darkTheme) DarkColorPalette else LightColorPalette,
        typography = typography,
        content = content
    )
}
