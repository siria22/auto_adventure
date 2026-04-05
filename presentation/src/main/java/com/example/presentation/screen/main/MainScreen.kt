package com.example.presentation.screen.main

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import com.example.domain.model.feature.party.PartyDetail
import com.example.presentation.screen.adventure.AdventureArgument
import com.example.presentation.screen.adventure.AdventureScreen
import com.example.presentation.screen.home.HomeArgument
import com.example.presentation.screen.home.HomeData
import com.example.presentation.screen.home.HomeScreen

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun MainScreen(
    navController: NavController,
    homeArgument: HomeArgument,
    homeData: HomeData,
    adventureArgument: AdventureArgument,
    adventureData: List<PartyDetail>
) {
    val pages = MainScreenPage.entries
    val pagerState = rememberPagerState(
        initialPage = pages.indexOf(MainScreenPage.HOME),
        pageCount = { pages.size }
    )

    HorizontalPager(
        state = pagerState,
        modifier = Modifier.fillMaxSize()
    ) { pageIndex ->
        when (pages[pageIndex]) {
            MainScreenPage.HOME -> {
                HomeScreen(
                    navController = navController,
                    argument = homeArgument,
                    data = homeData
                )
            }
            MainScreenPage.ADVENTURE -> {
                AdventureScreen(
                    navController = navController,
                    argument = adventureArgument,
                    data = adventureData
                )
            }
        }
    }
}