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


object SiriaTemplateColorScheme {
    val primary = ColorPalette.primary
    val secondary = ColorPalette.secondary

    val commonText = ColorPalette.darkSecondary
    val descriptionText = ColorPalette.secondary

    val iconTint = ColorPalette.darkSecondary
    val inactivatedColor = Color(0xFFDEDEDE)

    val bottomNavIconTint = ColorPalette.secondary

    val surface = Color(0xFFF2F2F5)
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
    SiriaTemplateTheme {
        Column {
            ColorPreviewBox("Primary", SiriaTemplateColorScheme.primary)
            ColorPreviewBox("Secondary", SiriaTemplateColorScheme.secondary)
            ColorPreviewBox("Background", SiriaTemplateColorScheme.background)
            ColorPreviewBox("Surface", SiriaTemplateColorScheme.surface)
            ColorPreviewBox("Common Text", SiriaTemplateColorScheme.commonText)
            ColorPreviewBox("Description Text", SiriaTemplateColorScheme.descriptionText)
            ColorPreviewBox("Icon Tint", SiriaTemplateColorScheme.iconTint)
            ColorPreviewBox("Bottom Nav Icon Tint", SiriaTemplateColorScheme.bottomNavIconTint)
        }
    }
}