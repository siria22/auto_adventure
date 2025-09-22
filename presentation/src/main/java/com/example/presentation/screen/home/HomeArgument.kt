package com.example.presentation.screen.home

import com.example.presentation.utils.error.ErrorEvent
import kotlinx.coroutines.flow.SharedFlow

data class HomeArgument(
    val intent: (HomeIntent) -> Unit,
    val state: HomeState,
    val event: SharedFlow<HomeEvent>
)

sealed class HomeState {
    data object Init : HomeState()
    data object OnProgress : HomeState()
}

sealed class HomeIntent {
    data class SomeIntentWithParams(val param: String) : HomeIntent()
    data object SomeIntentWithoutParams : HomeIntent()
}

sealed class HomeEvent {
    sealed class DataFetch : HomeEvent() {
        data class Error(
            override val userMessage: String = "문제가 발생했습니다.",
            override val exceptionMessage: String?
        ) : DataFetch(), ErrorEvent
    }
}