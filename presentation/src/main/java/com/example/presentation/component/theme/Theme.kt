package com.example.presentation.component.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext

private val DarkColorScheme = darkColorScheme(
//    primary = Color.White,
//    secondary = Color.White,
//    background = Color.White,
//    surface = Color.White,
//    onPrimary = Color.Black,
//    onSecondary = Color.Black,
//    onTertiary = Color.White,
//    onBackground = Color.Black,
//    onSurface = Color.Black
)

private val LightColorScheme = lightColorScheme(
//    primary = Color.White,
//    secondary = Color.White,
//    background = Color.White,
//    surface = Color.White,
//    onPrimary = Color.Black,
//    onSecondary = Color.Black,
//    onTertiary = Color.White,
//    onBackground = Color.Black,
//    onSurface = Color.Black
)

@Composable
fun AutoAdventureTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }

        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = NanumTypography,
        content = content
    )
}