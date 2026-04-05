package com.example.presentation.screen.adventure

import com.example.presentation.utils.error.ErrorEvent
import kotlinx.coroutines.flow.SharedFlow

data class AdventureArgument(
    val intent: (AdventureIntent) -> Unit,
    val state: AdventureState,
    val event: SharedFlow<AdventureEvent>
)

sealed interface AdventureState {
    data object Init : AdventureState
    data object OnProgress : AdventureState
}

sealed interface AdventureIntent {
    data object LoadPartyList : AdventureIntent
    data class OnPartyClick(val partyId: Long) : AdventureIntent
}

sealed interface AdventureEvent {
    sealed class DataFetch : AdventureEvent {
        data class Error(
            override val userMessage: String = "문제가 발생했습니다.",
            override val exceptionMessage: String?
        ) : DataFetch(), ErrorEvent
    }

    data class ShowToast(val message: String) : AdventureEvent
    data class NavigateToPartyDetail(val partyId: Long) : AdventureEvent
}
