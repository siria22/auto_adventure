package com.example.presentation.component.ui.organism.dialog.guild

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.domain.model.feature.actor.actor.Actor
import com.example.domain.model.feature.guild.GuildInfoData
import com.example.domain.model.feature.mockActor
import com.example.presentation.component.theme.AutoAdventureColorScheme
import com.example.presentation.component.theme.AutoAdventureTheme
import com.example.presentation.component.ui.DefaultRoundedCorner
import com.example.presentation.component.ui.atom.BasicButton
import com.example.presentation.component.ui.atom.BasicDialog
import com.example.presentation.component.ui.atom.ButtonType
import com.example.presentation.component.ui.molecule.guild.GuildRankBadge

@Composable
fun GuildInfoDialog(
    guildInfoData: GuildInfoData,
    onGuildRankUpButtonClicked: () -> Unit,
    onDismissButtonClicked: () -> Unit
) {
    BasicDialog(
        backHandler = onDismissButtonClicked
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(8.dp),
            horizontalAlignment = Alignment.Start
        ) {
            Text(
                text = "길드 정보",
                style = MaterialTheme.typography.titleMedium,
                color = AutoAdventureColorScheme.commonText,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )

            GuildInfoCard(guildInfoData = guildInfoData)

            Text(
                text = "대표 길드원",
                style = MaterialTheme.typography.titleSmall,
                color = AutoAdventureColorScheme.commonText
            )

            GuildChampions()

            BasicButton(
                text = "길드 승급 심사",
                type = ButtonType.PRIMARY,
                onClicked = onGuildRankUpButtonClicked,
            )

            BasicButton(
                text = "닫기",
                type = ButtonType.SECONDARY,
                onClicked = onDismissButtonClicked,
            )
        }
    }
}

@Composable
private fun GuildInfoCard(guildInfoData: GuildInfoData) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .background(
                color = Color.LightGray, shape = DefaultRoundedCorner
            )
            .padding(16.dp)
    ) {
        GuildRankBadge(
            rank = guildInfoData.guildRank,
            onClicked = {}
        )
        Column(
            verticalArrangement = Arrangement.spacedBy(4.dp),
            horizontalAlignment = Alignment.Start,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = guildInfoData.guildName,
                style = MaterialTheme.typography.labelLarge,
                color = AutoAdventureColorScheme.commonText
            )
            Text(
                text = "누적 경험치 : ${guildInfoData.guildTotalExp}",
                style = MaterialTheme.typography.labelMedium,
                color = AutoAdventureColorScheme.commonText
            )
            Text(
                text = "길드 레벨 : ${guildInfoData.guildLevel}",
                style = MaterialTheme.typography.labelMedium,
                color = AutoAdventureColorScheme.commonText
            )
            Text(
                text = "다음 레벨까지 : ${guildInfoData.expForNextLevel}",
                style = MaterialTheme.typography.labelMedium,
                color = AutoAdventureColorScheme.commonText
            )
            Text(
                text = "소속 길드원 : ${guildInfoData.guildMemberCount}",
                style = MaterialTheme.typography.labelMedium,
                color = AutoAdventureColorScheme.commonText
            )
        }
    }
}

/* TODO : 대표 길드원 표시 영역 (현재 Mocking됨) */
@Composable
private fun GuildChampions() {
    val guildChampions = listOf(mockActor)

    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        guildChampions.forEach { actor ->
            GuildChampionCard(actor)
        }
    }
}

@Composable
private fun GuildChampionCard(actor: Actor) {
    Column(
        verticalArrangement = Arrangement.spacedBy(4.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.padding(16.dp)
    ) {
        Box(
            modifier = Modifier
                .background(color = Color.LightGray)
                .size(64.dp)
        ) /* TODO : 캐릭터 프로필 이미지 */
        Text(
            text = actor.info.name,
            style = MaterialTheme.typography.labelMedium,
            color = AutoAdventureColorScheme.commonText
        )
        Text(
            text = actor.info.job.nameKor,
            style = MaterialTheme.typography.labelSmall,
            color = AutoAdventureColorScheme.commonText
        )
        Text(
            text = actor.info.job.nameEng, /* TODO : 탐사 횟수 -> Actor 객체에 추가 필요 */
            style = MaterialTheme.typography.labelMedium,
            color = AutoAdventureColorScheme.commonText
        )
    }
}

@Preview(apiLevel = 34)
@Composable
private fun GuildInfoDialogPreview() {
    AutoAdventureTheme {
        GuildInfoDialog(
            guildInfoData = GuildInfoData.mock(),
            onGuildRankUpButtonClicked = {},
            onDismissButtonClicked = {}
        )
    }
}