package com.example.presentation.screen.party

import androidx.lifecycle.SavedStateHandle
import com.example.domain.model.feature.actor.base.BaseActor
import com.example.domain.model.feature.party.PartyDetail
import com.example.domain.model.feature.party.PartyMember
import com.example.domain.model.feature.types.PartyPosition
import com.example.domain.usecase.feature.actor.GetAvailableActorsUseCase
import com.example.domain.usecase.feature.party.AddPartyMemberUseCase
import com.example.domain.usecase.feature.party.GetPartyDetailUseCase
import com.example.domain.usecase.feature.party.RemoveMemberFromPartyUseCase
import com.example.domain.usecase.feature.party.ReplacePartyMemberUseCase
import com.example.presentation.utils.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class PartyDetailViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val getPartyDetailUseCase: GetPartyDetailUseCase,
    private val getAvailableActorsUseCase: GetAvailableActorsUseCase,
    private val addPartyMemberUseCase: AddPartyMemberUseCase,
    private val replacePartyMemberUseCase: ReplacePartyMemberUseCase,
    private val removeMemberFromPartyUseCase: RemoveMemberFromPartyUseCase
) : BaseViewModel() {

    private val _state = MutableStateFlow<PartyDetailState>(PartyDetailState.Init)
    val state: StateFlow<PartyDetailState> = _state.asStateFlow()

    private val _eventFlow = MutableSharedFlow<PartyDetailEvent>()
    val eventFlow: SharedFlow<PartyDetailEvent> = _eventFlow.asSharedFlow()

    private val _partyDetail = MutableStateFlow<PartyDetail?>(null)
    val partyDetail: StateFlow<PartyDetail?> = _partyDetail.asStateFlow()

    private val _availableActors = MutableStateFlow<List<BaseActor>>(emptyList())
    val availableActors: StateFlow<List<BaseActor>> = _availableActors.asStateFlow()

    private var selectedSlotIndex: Int = -1

    fun onIntent(intent: PartyDetailIntent) {
        when (intent) {
            is PartyDetailIntent.LoadPartyDetail -> {
                launch {
                    loadPartyDetail(intent.partyId)
                }
            }

            is PartyDetailIntent.OnClose -> {

            }

            is PartyDetailIntent.OnSlotClick -> {
                selectedSlotIndex = intent.slotIndex
                launch {
                    loadAvailableActors()
                    _eventFlow.emit(PartyDetailEvent.ShowAddMemberDialog)
                }
            }

            is PartyDetailIntent.OnMemberSelected -> {
                launch {
                    handleMemberSelection(intent.actorId)
                }
            }

            is PartyDetailIntent.OnRemoveMember -> {
                launch {
                    removeMemberAtSlot()
                }
            }
        }
    }

    private suspend fun handleMemberSelection(newActorId: Long) {
        val partyDetail = partyDetail.value ?: return
        val partyId = partyDetail.party.id

        val currentMember = partyDetail.members.find { it.slotIndex == selectedSlotIndex }

        if (currentMember != null) {
            replacePartyMemberUseCase(partyId, currentMember.characterId, newActorId).onSuccess {
                loadPartyDetail(partyId)
            }.onFailure {

            }
        } else {
            val position = if (selectedSlotIndex < 2) PartyPosition.FRONT else PartyPosition.BACK
            val isLeader = (selectedSlotIndex == 0)

            val newMember = PartyMember(
                characterId = newActorId,
                partyId = partyId,
                isPartyLeader = isLeader,
                position = position,
                slotIndex = selectedSlotIndex
            )

            addPartyMemberUseCase(newMember).onSuccess {
                loadPartyDetail(partyId)
            }.onFailure {

            }
        }
    }

    private suspend fun removeMemberAtSlot() {
        val partyDetail = partyDetail.value ?: return
        val partyId = partyDetail.party.id
        val currentMember = partyDetail.members.find { it.slotIndex == selectedSlotIndex } ?: return

        removeMemberFromPartyUseCase(partyId, currentMember.characterId).onSuccess {
            loadPartyDetail(partyId)
        }.onFailure {

        }
    }

    private suspend fun loadAvailableActors() {
        getAvailableActorsUseCase().onSuccess { actors ->
            _availableActors.value = actors
        }
    }

    init {
        launch {
            val partyId = savedStateHandle.get<Long>("partyId") ?: -1L
            if (partyId != -1L) {
                loadPartyDetail(partyId)
            }
        }
    }

    private suspend fun loadPartyDetail(partyId: Long) {
        _state.value = PartyDetailState.OnProgress

        runCatching {
            getPartyDetailUseCase(partyId).getOrThrow()
        }.onSuccess { result ->
            _partyDetail.value = result
        }.onFailure { exception ->
            _eventFlow.emit(
                PartyDetailEvent.DataFetch.Error(
                    userMessage = "파티 정보를 불러오는데 실패했습니다.",
                    exceptionMessage = exception.message
                )
            )
        }
        _state.value = PartyDetailState.Init
    }
}
