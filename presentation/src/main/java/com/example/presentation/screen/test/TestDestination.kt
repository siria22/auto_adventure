package com.example.presentation.screen.test

import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.example.presentation.utils.nav.ScreenDestinations

fun NavGraphBuilder.testDestination(navController: NavController) {
    composable(
        route = ScreenDestinations.Test.route,
//        arguments = listOf(
//            navArgument(name = "") {
//                type = NavType.LongType
//                defaultValue = 0L
//            }
//        ) -> if route contains arguments
    ) {
        val viewModel: TestViewModel = hiltViewModel()

        val argument: TestArgument = let {
            val state by viewModel.state.collectAsStateWithLifecycle()

            TestArgument(
                state = state,
                intent = viewModel::onIntent,
                event = viewModel.eventFlow
            )
        }

        val data: TestData = let {
            val someData by viewModel.someData.collectAsStateWithLifecycle()

            TestData(
                data = someData
            )
        }

        TestScreen(
            navController = navController,
            argument = argument,
            data = data
        )
    }
}