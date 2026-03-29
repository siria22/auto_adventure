package com.example.presentation.screen.home

import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.example.domain.model.feature.party.PartyDetail
import com.example.presentation.screen.adventure.AdventureArgument
import com.example.presentation.screen.adventure.AdventureEvent
import com.example.presentation.screen.adventure.AdventureViewModel
import com.example.presentation.screen.main.MainScreen
import com.example.presentation.utils.nav.ScreenDestinations

fun NavGraphBuilder.homeDestination(navController: NavController) {
    composable(
        route = ScreenDestinations.Home.route
    ) {
        val homeViewModel: HomeViewModel = hiltViewModel()

        val homeArgument: HomeArgument = let {
            val state by homeViewModel.state.collectAsStateWithLifecycle()

            HomeArgument(
                state = state,
                intent = homeViewModel::onIntent,
                event = homeViewModel.eventFlow
            )
        }

        val homeData: HomeData = let {
            val guildInfoData by homeViewModel.guildInfoData.collectAsStateWithLifecycle()
            val guildMoney by homeViewModel.guildMoney.collectAsStateWithLifecycle()

            HomeData(
                guildInfoData = guildInfoData,
                guildMoney = guildMoney
            )
        }

        val adventureViewModel: AdventureViewModel = hiltViewModel()

        LaunchedEffect(adventureViewModel.eventFlow) {
            adventureViewModel.eventFlow.collect { event ->
                when (event) {
                    is AdventureEvent.NavigateToPartyDetail -> {
                        navController.navigate("party_detail/${event.partyId}")
                    }

                    else -> {}
                }
            }
        }

        val adventureArgument: AdventureArgument = let {
            val state by adventureViewModel.state.collectAsStateWithLifecycle()
            AdventureArgument(
                state = state,
                intent = adventureViewModel::onIntent,
                event = adventureViewModel.eventFlow
            )
        }

        val adventureData: List<PartyDetail> = let {
            val list by adventureViewModel.partyList.collectAsStateWithLifecycle()
            list
        }

        MainScreen(
            navController = navController,
            homeArgument = homeArgument,
            homeData = homeData,
            adventureArgument = adventureArgument,
            adventureData = adventureData
        )
    }
}