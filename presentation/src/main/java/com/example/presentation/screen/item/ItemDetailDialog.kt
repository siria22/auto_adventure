package com.example.presentation.screen.item

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.presentation.R
import com.example.presentation.component.ui.molecule.item.ItemCard

@Composable
fun ItemDetailDialog(
    itemName: String,
    itemShortDesc: String,
    itemFullDesc: String,
    itemObtainMethod: String,
    quantity: Int,
    onDismiss: () -> Unit,
    onSellClick: () -> Unit
) {
    Dialog(onDismissRequest = onDismiss) {
        Card(
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    ItemCard(imageUrl = R.drawable.ic_potion_red, quantity = quantity)
                    Spacer(modifier = Modifier.width(16.dp))
                    Column {
                        Text(text = itemName, fontWeight = FontWeight.Bold, fontSize = 18.sp)
                        Text(text = "보유 수량 : $quantity", fontSize = 14.sp, color = Color.Gray)
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(text = itemShortDesc, fontSize = 14.sp)
                        Text(
                            text = "획득 방법: $itemObtainMethod",
                            fontSize = 12.sp,
                            color = Color.Gray
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))
                Divider()
                Spacer(modifier = Modifier.height(16.dp))

                Text(text = itemFullDesc, fontSize = 14.sp)

                Spacer(modifier = Modifier.height(24.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Button(
                        onClick = onDismiss,
                        modifier = Modifier.weight(1f),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF5C5C5C)) // 닫기 버튼 색상 수정
                    ) {
                        Text("닫기")
                    }
                    Button(
                        onClick = onSellClick,
                        modifier = Modifier.weight(1f),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF8B8B8B)) // 판매하기 버튼 색상
                    ) {
                        Text("판매하기")
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun ItemDetailDialogPreview() {
    ItemDetailDialog(
        itemName = "초보자용 포션",
        itemShortDesc = "HP를 약간 회복시켜주는 포션",
        itemFullDesc = "가장 기본적인 회복 포션으로, 누구나 쉽게 사용할 수 있습니다. HP를 30만큼 회복시킵니다.",
        itemObtainMethod = "상점 구매, 몬스터 드랍, 퀘스트 보상",
        quantity = 67,
        onDismiss = {},
        onSellClick = {}
    )
}