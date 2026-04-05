package com.example.presentation.screen.adventure

import androidx.lifecycle.SavedStateHandle
import com.example.domain.model.feature.party.PartyDetail
import com.example.domain.usecase.feature.party.GetPartyListUseCase
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
class AdventureViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val getPartyListUseCase: GetPartyListUseCase
) : BaseViewModel() {

    private val _state = MutableStateFlow<AdventureState>(AdventureState.Init)
    val state: StateFlow<AdventureState> = _state.asStateFlow()

    private val _eventFlow = MutableSharedFlow<AdventureEvent>()
    val eventFlow: SharedFlow<AdventureEvent> = _eventFlow.asSharedFlow()

    private val _partyList = MutableStateFlow<List<PartyDetail>>(emptyList())
    val partyList: StateFlow<List<PartyDetail>> = _partyList.asStateFlow()

    fun onIntent(intent: AdventureIntent) {
        when (intent) {
            is AdventureIntent.LoadPartyList -> {
                launch {
                    loadPartyList()
                }
            }
            is AdventureIntent.OnPartyClick -> {
                launch {
                    _eventFlow.emit(AdventureEvent.NavigateToPartyDetail(intent.partyId))
                }
            }
        }
    }

    init {
        launch {
            loadPartyList()
        }
    }

    private suspend fun loadPartyList() {
        _state.value = AdventureState.OnProgress

        getPartyListUseCase()
            .onSuccess { result ->
                _partyList.value = result
            }.onFailure { exception ->
                _eventFlow.emit(
                    AdventureEvent.DataFetch.Error(
                        userMessage = "파티 목록을 불러오는데 실패했습니다.",
                        exceptionMessage = exception.message
                    )
                )
            }

        _state.value = AdventureState.Init
    }
}