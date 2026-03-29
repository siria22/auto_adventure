package com.example.presentation.screen.party

import com.example.presentation.utils.error.ErrorEvent
import kotlinx.coroutines.flow.SharedFlow

data class PartyDetailArgument(
    val intent: (PartyDetailIntent) -> Unit,
    val state: PartyDetailState,
    val event: SharedFlow<PartyDetailEvent>
)

sealed class PartyDetailState {
    data object Init : PartyDetailState()
    data object OnProgress : PartyDetailState()
}

sealed class PartyDetailIntent {
    data class LoadPartyDetail(val partyId: Long) : PartyDetailIntent()
    data object OnClose : PartyDetailIntent()
    data class OnSlotClick(val slotIndex: Int) : PartyDetailIntent()
    data class OnMemberSelected(val actorId: Long) : PartyDetailIntent()
    data object OnRemoveMember : PartyDetailIntent()
}

sealed class PartyDetailEvent {
    sealed class DataFetch : PartyDetailEvent() {
        data class Error(
            override val userMessage: String = "문제가 발생했습니다.",
            override val exceptionMessage: String?
        ) : DataFetch(), ErrorEvent
    }

    data object ShowAddMemberDialog : PartyDetailEvent()
}