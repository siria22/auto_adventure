package com.example.presentation.utils.nav

sealed class ScreenDestinations(val route: String) {

    data object Home : ScreenDestinations("home") {
        data object Test : ScreenDestinations("home/test")
    }

    data object Item : ScreenDestinations("item") {
        data object Test : ScreenDestinations("item/test")
    }

    data object Shop : ScreenDestinations("shop")

    data object Party : ScreenDestinations("party") {
        data object Test : ScreenDestinations("party_detail/{partyId}")
    }

    data object Adventure : ScreenDestinations("adventure")
}