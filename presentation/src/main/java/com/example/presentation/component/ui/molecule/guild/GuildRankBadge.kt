package com.example.presentation.component.ui.molecule.guild

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.domain.scripts.guild.GuildRank
import com.example.presentation.R
import com.example.presentation.component.theme.AutoAdventureColorScheme
import com.example.presentation.component.theme.AutoAdventureTheme
import com.example.presentation.component.theme.DNFTypography

/**
 * Guild rank badge
 * rank = E3~1, D3~1, C3~1, B3~1, A3~1, S3~1, LG
 * TODO : Guild Rank Badge - 리소스 완성되면 나머지 매핑
 */
@Composable
fun GuildRankBadge(
    rank: GuildRank,
    onClicked: () -> Unit = {}
) {
    val guildBadgeBackground = when (rank.mainRank) {
        'E' -> R.drawable.badge_guild_tier_1
        'D' -> R.drawable.badge_guild_tier_2
        'C' -> R.drawable.badge_guild_tier_3
        else -> R.drawable.badge_guild_tier_1
    }

    Box(
        modifier = Modifier.clickable { onClicked() }
    ) {
        Image(
            painter = painterResource(guildBadgeBackground),
            contentDescription = "guild_image"
        )
        Column(
            verticalArrangement = Arrangement.spacedBy(2.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.align(Alignment.Center)
        ) {
            Text(
                text = rank.name,
                style = DNFTypography.titleLarge,
                color = AutoAdventureColorScheme.commonText
            )
        }
    }
}

@Preview(apiLevel = 34)
@Composable
private fun GuildRankBadgePreview() {
    AutoAdventureTheme {
        GuildRankBadge(
            rank = GuildRank.E3,
            onClicked = {}
        )
    }
}
