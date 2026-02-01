package com.example.presentation.component.ui.molecule.item

import android.graphics.Paint
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.presentation.R
import com.example.presentation.component.theme.AutoAdventureTheme
import com.example.presentation.component.ui.atom.text.StrokedText

@Composable
fun ItemCard(
    modifier: Modifier = Modifier,
    imageUrl: Int,
    quantity: Int
) {
    Box(
        modifier = modifier
            .size(80.dp)
            .clip(RoundedCornerShape(8.dp))
            .background(Color(0xFFD2D2D2))
            .border(1.dp, Color.Gray, RoundedCornerShape(8.dp))
            .padding(4.dp)
    ) {
        Image(
            painter = painterResource(id = imageUrl),
            contentDescription = "item image",
            modifier = Modifier
                .fillMaxSize()
                .align(Alignment.Center)
        )

        if (quantity > 1) {
            Box(
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .offset(x = (-3).dp, y = (-9).dp)
            ) {
                StrokedText(
                    text = quantity.toString(),
                    textColor = Color.Black,
                    strokeColor = Color.White,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    textAlign = Paint.Align.RIGHT
                )
            }
        }
    }
}

@Preview
@Composable
private fun ItemCardPreview() {
    AutoAdventureTheme {
        ItemCard(
            imageUrl = R.drawable.ic_potion_red,
            quantity = 67
        )
    }
}