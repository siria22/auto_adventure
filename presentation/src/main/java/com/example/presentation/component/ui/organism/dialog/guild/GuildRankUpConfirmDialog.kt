package com.example.presentation.component.ui.organism.dialog.guild

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DoubleArrow
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.domain.model.feature.guild.GuildInfoData
import com.example.domain.scripts.guild.GuildRank
import com.example.domain.scripts.guild.GuildStats
import com.example.presentation.component.theme.AutoAdventureColorScheme
import com.example.presentation.component.theme.AutoAdventureTheme
import com.example.presentation.component.ui.atom.BasicButton
import com.example.presentation.component.ui.atom.BasicDialog
import com.example.presentation.component.ui.atom.ButtonType
import com.example.presentation.component.ui.molecule.guild.GuildRankBadge

@Composable
fun GuildRankUpConfirmDialog(
    guildInfoData: GuildInfoData,
    money: Long,
    onDismissButtonClicked: () -> Unit,
    onConfirmButtonClicked: () -> Unit
) {
    val isGuildRankUpAvailable = GuildStats.isGuildRankUpAvailable(
        currentRank = guildInfoData.guildRank,
        totalExp = guildInfoData.guildTotalExp,
        money = money
    )

    val requiredGold = GuildStats.getRequiredGoldForNextRank(
        targetRank = GuildStats.getNextGuildRank(guildInfoData.guildRank)
    )

    BasicDialog(backHandler = onDismissButtonClicked) {
        Column(
            verticalArrangement = Arrangement.spacedBy(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "길드 승급 심사",
                style = MaterialTheme.typography.titleMedium,
                color = AutoAdventureColorScheme.commonText,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )

            Text(
                text = "충분한 실적을 쌓으면\n" +
                        "다음 등급으로 승급할 수 있어요.",
                style = MaterialTheme.typography.labelMedium,
                color = AutoAdventureColorScheme.commonText,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )

            GuildRankUpInfo(currentGuildRank = guildInfoData.guildRank)

            Text(
                text = "승급 심사를 요청할까요?",
                style = MaterialTheme.typography.labelMedium,
                color = AutoAdventureColorScheme.commonText,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )

            Text(
                text = "심사 비용 : $requiredGold G",
                style = MaterialTheme.typography.titleSmall,
                color = AutoAdventureColorScheme.commonText
            )

            if (isGuildRankUpAvailable.isFailure) {
                val exception = isGuildRankUpAvailable.exceptionOrNull()
                Text(
                    text = "${exception?.message}",
                    style = MaterialTheme.typography.labelMedium,
                    color = AutoAdventureColorScheme.errorText
                )
            }

            Row(
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                BasicButton(
                    text = "닫기",
                    type = ButtonType.SECONDARY,
                    onClicked = onDismissButtonClicked,
                    modifier = Modifier.weight(1f)
                )
                if (isGuildRankUpAvailable.isSuccess) {
                    BasicButton(
                        text = "길드 승급 심사",
                        type = ButtonType.PRIMARY,
                        onClicked = onConfirmButtonClicked,
                        modifier = Modifier.weight(1f)
                    )
                } else {
                    BasicButton(
                        text = "길드 승급 심사",
                        type = ButtonType.DEFAULT,
                        onClicked = { },
                        modifier = Modifier.weight(1f)
                    )
                }
            }
        }
    }
}

@Composable
private fun GuildRankUpInfo(
    currentGuildRank: GuildRank
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        GuildRankBadge(rank = currentGuildRank)
        Icon(
            imageVector = Icons.Default.DoubleArrow,
            contentDescription = "upgrade to",
            tint = AutoAdventureColorScheme.iconTint,
            modifier = Modifier.size(40.dp)
        )
        GuildRankBadge(rank = GuildStats.getNextGuildRank(currentGuildRank))
    }
}

@Preview(apiLevel = 34)
@Composable
private fun GuildRankUpConfirmDialogPreview() {
    AutoAdventureTheme {
        GuildRankUpConfirmDialog(
            guildInfoData = GuildInfoData.mock(),
            money = 7200L,
            onDismissButtonClicked = {},
            onConfirmButtonClicked = {}
        )
    }
}
