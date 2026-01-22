package com.example.presentation.screen.shop

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.example.presentation.utils.nav.ScreenDestinations

fun NavGraphBuilder.shopDestination(navController: NavController) {
    composable(
        route = ScreenDestinations.Shop.route
    ) {
        ShopScreen(navController = navController)
    }
}