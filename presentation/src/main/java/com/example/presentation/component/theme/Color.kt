package com.example.presentation.component.theme

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

private object ColorPalette {
    val primary = Color(0xFF407ABF)
    val secondary = Color(0xFF637387)
    val darkSecondary = Color(0xFF121417)
    val white = Color.White

    val posColor = Color(0xFFA2EFC5)
    val negColor = Color(0xFFFFADA2)
}

object AutoAdventureColorScheme {
    val primary = ColorPalette.primary
    val secondary = ColorPalette.secondary

    val commonText = ColorPalette.darkSecondary
    val descriptionText = ColorPalette.secondary
    val errorText = Color(0xFFFFADA2)

    val iconTint = ColorPalette.darkSecondary
    val inactivatedColor = Color(0xFFDEDEDE)

    val bottomNavIconTint = ColorPalette.secondary

    val surface = Color(0xFFC8C8C8)
    val background = ColorPalette.white

    val primaryButtonColor = ColorPalette.primary
    val onPrimaryButtonColor = ColorPalette.white
    val secondaryButtonColor = ColorPalette.secondary
    val onSecondaryButtonColor = ColorPalette.white
}


@Composable
private fun ColorPreviewBox(colorName: String, color: Color) {
    Box(
        modifier = Modifier
            .padding(8.dp)
            .fillMaxWidth()
    ) {
        Text(text = colorName, color = color)
    }
}

@Preview(apiLevel = 34)
@Composable
private fun ColorPreview() {
    AutoAdventureTheme {
        Column {
            ColorPreviewBox("Primary", AutoAdventureColorScheme.primary)
            ColorPreviewBox("Secondary", AutoAdventureColorScheme.secondary)
            ColorPreviewBox("Background", AutoAdventureColorScheme.background)
            ColorPreviewBox("Surface", AutoAdventureColorScheme.surface)
            ColorPreviewBox("Common Text", AutoAdventureColorScheme.commonText)
            ColorPreviewBox("Description Text", AutoAdventureColorScheme.descriptionText)
            ColorPreviewBox("Icon Tint", AutoAdventureColorScheme.iconTint)
            ColorPreviewBox("Bottom Nav Icon Tint", AutoAdventureColorScheme.bottomNavIconTint)
        }
    }
}