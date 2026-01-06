package com.example.presentation.screen.item

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
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
import com.example.domain.model.feature.types.EquipCategory
import com.example.presentation.R
import com.example.presentation.component.ui.molecule.item.EquipCard

@Composable
fun EquipDetailDialog(
    equipDetail: EquipDetail,
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
                    EquipCard(
                        imageUrl = R.drawable.ic_ironsword, // TODO: 실제 이미지 매핑 필요 시 변경
                        reinforcement = equipDetail.reinforcement,
                        modifier = Modifier.size(100.dp) // 카드 사이즈
                    )
                    Spacer(modifier = Modifier.width(16.dp))
                    Column {
                        val fullName = if (equipDetail.reinforcement > 0) {
                            "${equipDetail.name} (+${equipDetail.reinforcement})"
                        } else {
                            equipDetail.name
                        }
                        Text(text = fullName, fontWeight = FontWeight.Bold, fontSize = 18.sp)
                        Spacer(modifier = Modifier.height(4.dp))

                        Text(text = "착용자: ${equipDetail.ownerName}", fontSize = 14.sp)

                        Text(text = equipDetail.statDescription, fontSize = 14.sp)

                        Text(text = "획득 방법: ${equipDetail.obtainMethod}", fontSize = 14.sp)
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))
                Divider()
                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = equipDetail.description,
                    fontSize = 14.sp,
                    lineHeight = 20.sp
                )

                Spacer(modifier = Modifier.height(24.dp))

                Column(
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Button(
                        onClick = { /* TODO: 강화 로직 */ },
                        modifier = Modifier.fillMaxWidth(),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF8B8B8B))
                    ) {
                        Text("장비 강화")
                    }

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
                            onClick = onSellClick,
                            modifier = Modifier.weight(1f),
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF8B8B8B))
                        ) {
                            Text("판매하기")
                        }
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun EquipDetailDialogPreview() {
    EquipDetailDialog(
        equipDetail = EquipDetail(
            id = 1L,
            name = "숏소드",
            reinforcement = 3,
            ownerName = "샤이이이이-☆",
            description = "한 손으로 손쉽게 사용할 수 있는 단검입니다. 끈/로프 절단, 식량 채집 등 다양하게 사용 가능합니다.",
            statDescription = "공격력 + 3 (+3)",
            category = EquipCategory.WEAPON,
            sellPrice = 100
        ),
        onDismiss = {},
        onSellClick = {}
    )
}