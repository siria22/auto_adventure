package com.example.presentation.screen.party

import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.presentation.utils.nav.ScreenDestinations

fun NavGraphBuilder.partyDetailDestination(navController: NavController) {
    composable(
        route = ScreenDestinations.Party.Test.route,
        arguments = listOf(navArgument("partyId") {
            type = NavType.LongType
        })
    ) {
        val viewModel: PartyDetailViewModel = hiltViewModel()

        val argument: PartyDetailArgument = let {
            val state by viewModel.state.collectAsStateWithLifecycle()

            PartyDetailArgument(
                state = state,
                intent = viewModel::onIntent,
                event = viewModel.eventFlow
            )
        }

        val data: PartyData = let {
            val partyDetail by viewModel.partyDetail.collectAsStateWithLifecycle()
            val availableActors by viewModel.availableActors.collectAsStateWithLifecycle()

            PartyData(
                partyDetail = partyDetail,
                availableActors = availableActors
            )
        }

        PartyDetailScreen(
            navController = navController,
            argument = argument,
            data = data
        )
    }
}