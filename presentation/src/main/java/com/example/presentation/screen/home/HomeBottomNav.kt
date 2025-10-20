package com.example.presentation.screen.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.presentation.R
import com.example.presentation.component.theme.AutoAdventureTheme

@Composable
fun BottomNavBar() {
    /* TODO : Background for Bottom Nav Items */
    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.fillMaxWidth()
    ) {
        IconBox(
            iconRes = painterResource(id = R.drawable.home),
            name = "Item",
            onClicked = { /* TODO : Navigate to Item */ }
        )
        IconBox(
            iconRes = painterResource(id = R.drawable.home),
            name = "Shop",
            onClicked = { /* TODO : Navigate to Shop */ }
        )
        IconBox(
            iconRes = painterResource(id = R.drawable.home),
            name = "Inn",
            onClicked = { /* TODO : Navigate to Inn */ }
        )
        IconBox(
            iconRes = painterResource(id = R.drawable.home),
            name = "Quest",
            onClicked = { /* TODO : Navigate to Quest */ }
        )
    }
}

@Composable
private fun IconBox(
    iconRes: Painter,
    name: String,
    onClicked: () -> Unit
){
    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.clickable{
            onClicked()
        }.size(60.dp)
    ) {
        Image(
            painter = iconRes,
            contentDescription = "Icon $iconRes"
        )
        Text(
           text = name
        )
    }
}

@Preview(apiLevel = 34)
@Composable
private fun BottomNavBarPreview() {
    AutoAdventureTheme {
        BottomNavBar()
    }
}