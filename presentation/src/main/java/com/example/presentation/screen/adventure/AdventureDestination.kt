package com.example.presentation.screen.adventure

import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.example.presentation.utils.nav.ScreenDestinations

fun NavGraphBuilder.adventureDestination(navController: NavController) {
    composable(
        route = ScreenDestinations.Adventure.route
    ) {
        val viewModel: AdventureViewModel = hiltViewModel()

        val argument: AdventureArgument = let {
            val state by viewModel.state.collectAsStateWithLifecycle()

            AdventureArgument(
                state = state,
                intent = viewModel::onIntent,
                event = viewModel.eventFlow
            )
        }

        val data: List<com.example.domain.model.feature.party.PartyDetail> = let {
            val list by viewModel.partyList.collectAsStateWithLifecycle()
            list
        }

        AdventureScreen(
            navController = navController,
            argument = argument,
            data = data
        )
    }
}