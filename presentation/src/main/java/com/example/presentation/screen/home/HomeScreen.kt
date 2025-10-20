package com.example.presentation.screen.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.domain.model.feature.guild.GuildInfoData
import com.example.domain.scripts.guild.GuildRank
import com.example.presentation.R
import com.example.presentation.component.theme.AutoAdventureTheme
import com.example.presentation.component.ui.molecule.guild.GuildMoneyCard
import com.example.presentation.component.ui.molecule.guild.GuildRankBadge
import com.example.presentation.component.ui.organism.dialog.guild.GuildInfoDialog
import com.example.presentation.component.ui.organism.dialog.guild.GuildRankUpConfirmDialog
import com.example.presentation.utils.error.ErrorDialog
import com.example.presentation.utils.error.ErrorDialogState
import com.example.presentation.utils.nav.ScreenDestinations
import com.example.presentation.utils.nav.safeNavigate
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableSharedFlow

@Composable
fun HomeScreen(
    navController: NavController,
    argument: HomeArgument,
    data: HomeData
) {
    var errorDialogState by remember { mutableStateOf(ErrorDialogState.idle()) }

    val guildInfoData = data.guildInfoData
    val guildMoney = data.guildMoney

    var isGuildInfoDialogVisible by remember { mutableStateOf(false) }
    var isGuildRankUpConfirmDialogVisible by remember { mutableStateOf(false) }

    LaunchedEffect(argument.event) {
        argument.event.collect { event ->
            when (event) {
                else -> {}
            }
        }
    }

    Scaffold { innerPadding ->
        Box(modifier = Modifier.padding(innerPadding)) {
            Image(
                modifier = Modifier.fillMaxSize(),
                painter = painterResource(id = R.drawable.bg_guild),
                contentDescription = "Banner",
                contentScale = ContentScale.Crop
            )
            HomeScreenContents(
                guildInfoData = guildInfoData,
                money = guildMoney,
                onGuildBadgeClicked = { isGuildInfoDialogVisible = true }
            )
        }
    }

    if (isGuildInfoDialogVisible) {
        GuildInfoDialog(
            guildInfoData = guildInfoData,
            onGuildRankUpButtonClicked = { isGuildRankUpConfirmDialogVisible = true },
            onDismissButtonClicked = { isGuildInfoDialogVisible = false }
        )
    }

    if (isGuildRankUpConfirmDialogVisible) {
        GuildRankUpConfirmDialog(
            guildInfoData = guildInfoData,
            money = guildMoney,
            onDismissButtonClicked = { isGuildRankUpConfirmDialogVisible = false },
            onConfirmButtonClicked = {
                /* TODO : guild rank up complete dialog */
            }
        )
    }

    if (errorDialogState.isErrorDialogVisible) {
        ErrorDialog(
            errorDialogState = errorDialogState,
            errorHandler = {
                errorDialogState = errorDialogState.toggleVisibility()
                navController.safeNavigate(ScreenDestinations.Home.route)
            }
        )
    }
}

@Composable
private fun HomeScreenContents(
    guildInfoData: GuildInfoData,
    money: Long,
    onGuildBadgeClicked: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxHeight()
            .padding(16.dp),
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        GuildStatusHeader(
            guildRank = guildInfoData.guildRank,
            money = money,
            onGuildBadgeClicked = onGuildBadgeClicked
        )

        BottomNavBar()
    }
}

@Composable
private fun GuildStatusHeader(
    guildRank: GuildRank,
    money: Long,
    onGuildBadgeClicked: () -> Unit
) {
    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.Top,
        modifier = Modifier.fillMaxWidth()
    ) {
        GuildRankBadge(
            rank = guildRank,
            onClicked = onGuildBadgeClicked
        )
        GuildMoneyCard(money)
    }
}

@Preview(apiLevel = 34)
@Composable
private fun HomeScreenPreview() {
    AutoAdventureTheme {
        HomeScreen(
            navController = rememberNavController(),
            argument = HomeArgument(
                intent = { },
                state = HomeState.Init,
                event = MutableSharedFlow()
            ),
            data = HomeData.empty()
        )
    }
}
