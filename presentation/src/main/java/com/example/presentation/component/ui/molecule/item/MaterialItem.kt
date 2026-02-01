package com.example.presentation.component.ui.molecule.item

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp

data class ReinforceMaterial(
    val name: String,
    val iconResId: Int,
    val currentAmount: Long,
    val requiredAmount: Long
)

@Composable
fun MaterialItem(material: ReinforceMaterial) {
    val isSufficient = material.currentAmount >= material.requiredAmount
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .size(60.dp)
                .clip(RoundedCornerShape(8.dp))
                .background(Color(0xFFD2D2D2))
                .border(1.dp, Color.Gray, RoundedCornerShape(8.dp))
                .padding(4.dp)
        ) {
            Image(
                painter = painterResource(id = material.iconResId),
                contentDescription = material.name,
                modifier = Modifier
                    .align(Alignment.Center)
                    .fillMaxSize()
            )
        }
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = "${material.currentAmount}/${material.requiredAmount}",
            style = MaterialTheme.typography.labelSmall,
            color = if (isSufficient) Color.Black else Color.Red
        )
    }
}