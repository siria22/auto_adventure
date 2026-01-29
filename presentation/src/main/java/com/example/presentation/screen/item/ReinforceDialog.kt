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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.presentation.R
import com.example.presentation.component.theme.AutoAdventureTheme
import com.example.presentation.component.ui.molecule.item.EquipCard
import com.example.presentation.component.ui.molecule.item.MaterialItem
import com.example.presentation.component.ui.molecule.item.ReinforceMaterial

@Composable
fun ReinforceDialog(
    equipName: String,
    currentReinforcement: Long,
    imageUrl: Int,
    materials: List<ReinforceMaterial>,
    onDismiss: () -> Unit,
    onReinforce: () -> Unit
) {
    val isMaxLevel = currentReinforcement >= 10
    val isMaterialSufficient = materials.all { it.currentAmount >= it.requiredAmount }
    val isReinforceAvailable = !isMaxLevel && isMaterialSufficient

    val nextReinforcement = currentReinforcement + 1

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
                Text(
                    text = "장비 강화",
                    style = MaterialTheme.typography.titleMedium
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = if (isMaxLevel) {
                        "이미 최대 레벨에 도달했습니다."
                    } else {
                        "강화 재료를 소모하여\n'$equipName'(을)를 강화할 수 있습니다."
                    },
                    style = MaterialTheme.typography.bodyMedium,
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(24.dp))

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    EquipCard(
                        imageUrl = imageUrl,
                        reinforcement = currentReinforcement,
                        modifier = Modifier.size(80.dp)
                    )

                    if (!isMaxLevel) {
                        Spacer(modifier = Modifier.width(16.dp))
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                            contentDescription = "Arrow",
                            modifier = Modifier.size(24.dp)
                        )
                        Spacer(modifier = Modifier.width(16.dp))

                        EquipCard(
                            imageUrl = imageUrl,
                            reinforcement = nextReinforcement,
                            modifier = Modifier.size(80.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                if (!isMaxLevel) {
                    Text(
                        text = "소모 재료",
                        style = MaterialTheme.typography.labelLarge
                    )
                    Spacer(modifier = Modifier.height(12.dp))

                    Row(
                        horizontalArrangement = Arrangement.Center,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        materials.forEachIndexed { index, material ->
                            MaterialItem(material)
                            if (index < materials.lastIndex) {
                                Spacer(modifier = Modifier.width(16.dp))
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    if (!isReinforceAvailable) {
                        Text(
                            text = "강화 재료가 부족합니다!",
                            color = Color.Red,
                            style = MaterialTheme.typography.labelMedium
                        )
                    } else {
                        Spacer(modifier = Modifier.height(17.dp))
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Button(
                        onClick = onDismiss,
                        modifier = Modifier.weight(1f),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF5C5C5C)),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Text("닫기")
                    }
                    Button(
                        onClick = onReinforce,
                        modifier = Modifier.weight(1f),
                        enabled = isReinforceAvailable,
                        colors = ButtonDefaults.buttonColors(
                            containerColor = if (isReinforceAvailable) Color(0xFF8B8B8B) else Color(
                                0xFFC3C3C3
                            ),
                            contentColor = Color.White,
                            disabledContainerColor = Color(0xFFC3C3C3),
                            disabledContentColor = Color.White
                        ),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Text(if (isMaxLevel) "최대 레벨" else "장비 강화")
                    }
                }

            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun ReinforceDialogPreview() {
    AutoAdventureTheme {
        ReinforceDialog(
            equipName = "숏소드",
            currentReinforcement = 3,
            imageUrl = R.drawable.ic_ironsword,
            materials = listOf(
                ReinforceMaterial("Iron", R.drawable.ic_ironsword, 16, 4), // 임시 아이콘
                ReinforceMaterial("Stone", R.drawable.ic_ironsword, 8, 10), // 부족한 경우 테스트
                ReinforceMaterial("Gold", R.drawable.ic_ironsword, 1800, 760)
            ),
            onDismiss = {},
            onReinforce = {}
        )
    }
}