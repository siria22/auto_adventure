package com.example.presentation.screen.item

import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.example.presentation.utils.nav.ScreenDestinations
import kotlinx.coroutines.flow.merge

fun NavGraphBuilder.itemDestination(navController: NavController) {
    composable(
        route = ScreenDestinations.Item.route
    ) {
        val itemViewModel: ItemViewModel = hiltViewModel()
        val equipViewModel: EquipViewModel = hiltViewModel()

        val itemArgument: ItemArgument = let {
            val state by itemViewModel.state.collectAsStateWithLifecycle()
            val selectedItemDetail by itemViewModel.selectedItemDetail.collectAsStateWithLifecycle()
            val selectedEquipDetail by equipViewModel.selectedEquipDetail.collectAsStateWithLifecycle()

            val mergedEvent = merge(itemViewModel.eventFlow, equipViewModel.eventFlow)

            ItemArgument(
                state = state,
                intent = itemViewModel::onIntent,
                event = mergedEvent,
                selectedItemDetail = selectedItemDetail,
                selectedEquipDetail = selectedEquipDetail
            )
        }
        val itemData: ItemData = let {
            val displayedItems by itemViewModel.displayedItems.collectAsStateWithLifecycle()
            val selectedFilter by itemViewModel.selectedFilter.collectAsStateWithLifecycle()
            val selectedSort by itemViewModel.selectedSort.collectAsStateWithLifecycle()
            ItemData(
                displayedItems = displayedItems,
                selectedFilter = selectedFilter,
                selectedSort = selectedSort,
                totalWeight = 0.0, // TODO
                maxWeight = 0.0    // TODO
            )
        }

        val equipData: EquipData = let {
            val displayedEquipments by equipViewModel.displayedEquipments.collectAsStateWithLifecycle()
            val selectedFilter by equipViewModel.selectedEquipFilter.collectAsStateWithLifecycle()
            val selectedSort by equipViewModel.selectedEquipSort.collectAsStateWithLifecycle()
            EquipData(
                displayedEquipments = displayedEquipments,
                selectedFilter = selectedFilter,
                selectedSort = selectedSort
            )
        }

        ItemScreen(
            navController = navController,
            itemArgument = itemArgument,
            itemData = itemData,
            equipData = equipData,
            onEquipIntent = equipViewModel::onIntent
        )
    }
}