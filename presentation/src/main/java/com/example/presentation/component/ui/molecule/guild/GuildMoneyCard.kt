package com.example.presentation.component.ui.molecule.guild

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.presentation.component.theme.AutoAdventureTheme

@Composable
fun GuildMoneyCard(
    money: Long
) {
    Box(modifier = Modifier
        .background(Color.White)
        .padding(8.dp)
        .fillMaxWidth(0.3f)
    ) {
        Text(
            text = "$money g",
            modifier = Modifier.align(Alignment.CenterEnd)
        )
    }
}

@Preview(apiLevel = 34)
@Composable
private fun GuildMoneyCardPreview() {
    AutoAdventureTheme {
        GuildMoneyCard(150)
    }
}