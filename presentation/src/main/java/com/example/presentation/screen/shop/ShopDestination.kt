package com.example.presentation.screen.shop

import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.example.presentation.utils.nav.ScreenDestinations

fun NavGraphBuilder.shopDestination(navController: NavController) {
    composable(
        route = ScreenDestinations.Shop.route
    ) {
        val viewModel: ShopViewModel = hiltViewModel()

        val argument = let {
            val state by viewModel.shopState.collectAsStateWithLifecycle()

            ShopArgument(
                state = state,
                event = viewModel.eventFlow,
                intent = viewModel::onIntent
            )
        }

        val data = let {
            val userGold by viewModel.userGold.collectAsStateWithLifecycle()
            val items by viewModel.items.collectAsStateWithLifecycle()
            val baseEquips by viewModel.baseEquips.collectAsStateWithLifecycle()
            val selectedItemToBuy by viewModel.selectedItemToBuy.collectAsStateWithLifecycle()
            val selectedEquipToBuy by viewModel.selectedEquipToBuy.collectAsStateWithLifecycle()

            val itemFilter by viewModel.itemFilter.collectAsStateWithLifecycle()
            val itemSort by viewModel.itemSort.collectAsStateWithLifecycle()
            val equipFilter by viewModel.equipFilter.collectAsStateWithLifecycle()
            val equipSort by viewModel.equipSort.collectAsStateWithLifecycle()

            ShopData(
                userGold = userGold,
                items = items,
                baseEquips = baseEquips,
                selectedItemToBuy = selectedItemToBuy,
                selectedEquipToBuy = selectedEquipToBuy,
                itemFilter = itemFilter,
                itemSort = itemSort,
                equipFilter = equipFilter,
                equipSort = equipSort
            )
        }

        ShopScreen(
            navController = navController,
            argument = argument,
            data = data
        )
    }
}