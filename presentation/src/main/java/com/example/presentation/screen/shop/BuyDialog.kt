package com.example.presentation.screen.shop

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog

@Composable
fun BuyDialog(
    imageUrl: Int,
    name: String,
    price: Long,
    maxQuantity: Int = 99,
    onDismiss: () -> Unit,
    onBuy: (Int) -> Unit,
    disableBuyReason: String? = null
) {
    var quantity by remember { mutableIntStateOf(1) }
    val totalPrice = price * quantity

    Dialog(onDismissRequest = onDismiss) {
        Card(
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Box(
                    modifier = Modifier
                        .size(100.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .background(Color(0xFFD2D2D2))
                        .border(1.dp, Color.Gray, RoundedCornerShape(8.dp))
                        .padding(8.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Image(
                        painter = painterResource(id = imageUrl),
                        contentDescription = name,
                        modifier = Modifier.size(64.dp)
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = name,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black,
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(16.dp))

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .background(Color(0xFFE0E0E0), RoundedCornerShape(4.dp))
                        .border(1.dp, Color.Gray, RoundedCornerShape(4.dp))
                ) {
                    Box(
                        modifier = Modifier
                            .size(width = 40.dp, height = 32.dp)
                            .clickable(enabled = quantity > 1 && disableBuyReason == null) {
                                if (quantity > 1) quantity--
                            },
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "-",
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                            color = if (quantity > 1 && disableBuyReason == null) Color.Black else Color.Gray
                        )
                    }

                    Box(
                        modifier = Modifier
                            .size(width = 60.dp, height = 32.dp)
                            .background(Color.White)
                            .border(1.dp, Color.LightGray),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = quantity.toString(),
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    Box(
                        modifier = Modifier
                            .size(width = 40.dp, height = 32.dp)
                            .clickable(enabled = quantity < maxQuantity && disableBuyReason == null) {
                                if (quantity < maxQuantity) quantity++
                            },
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "+",
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                            color = if (quantity < maxQuantity && disableBuyReason == null) Color.Black else Color.Gray
                        )
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = "총 구매 가격: $totalPrice G",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium,
                    color = Color.Black
                )

                if (disableBuyReason != null) {
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = disableBuyReason,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.Red,
                        textAlign = TextAlign.Center
                    )
                }

                Spacer(modifier = Modifier.height(24.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Button(
                        onClick = onDismiss,
                        modifier = Modifier.weight(1f),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF5C5C5C))
                    ) {
                        Text("닫기")
                    }

                    Button(
                        onClick = { onBuy(quantity) },
                        modifier = Modifier.weight(1f),
                        enabled = disableBuyReason == null,
                        colors = ButtonDefaults.buttonColors(
                            containerColor = if (disableBuyReason == null) Color(0xFF8B8B8B) else Color(0xFF5D4037)
                        )
                    ) {
                        Text("구매")
                    }
                }
            }
        }
    }
}