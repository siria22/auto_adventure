package com.example.presentation.screen.home

import androidx.lifecycle.viewModelScope
import com.example.domain.model.feature.guild.GuildInfoData
import com.example.domain.usecase.feature.guild.GetGuildInfoUseCase
import com.example.domain.usecase.feature.user.GetPlayerMoneyUseCase
import com.example.presentation.utils.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val getGuildInfoUseCase: GetGuildInfoUseCase,
    private val getPlayerMoneyUseCase: GetPlayerMoneyUseCase
) : BaseViewModel() {

    private val _state = MutableStateFlow<HomeState>(HomeState.Init)
    val state: StateFlow<HomeState> = _state

    private val _eventFlow = MutableSharedFlow<HomeEvent>(replay = +1)
    val eventFlow: SharedFlow<HomeEvent> = _eventFlow

    private val _guildInfoData = MutableStateFlow(GuildInfoData.empty())
    val guildInfoData: StateFlow<GuildInfoData> = _guildInfoData

    private val _guildMoney = MutableStateFlow(0L)
    val guildMoney: StateFlow<Long> = _guildMoney

    fun onIntent(intent: HomeIntent) {
        when (intent) {
            is HomeIntent.SomeIntentWithoutParams -> {
                //do sth
            }

            is HomeIntent.SomeIntentWithParams -> {
                //do sth(intent.params)
            }

            is HomeIntent.Refresh -> {
                viewModelScope.launch {
                    initAndRefresh()
                }
            }
        }
    }

    init {
        viewModelScope.launch {
            getPlayerMoneyUseCase().collectLatest { money ->
                _guildMoney.value = money
            }
        }

        viewModelScope.launch {
            initAndRefresh()
        }
    }

    private suspend fun initAndRefresh() {
        _state.value = HomeState.OnProgress
        runCatching {
            getGuildInfo()
        }.onFailure { ex ->
            emitErrorMessage("데이터를 불러오는데 실패했습니다.", ex)
        }
    }

    private suspend fun getGuildInfo() {
        _state.value = HomeState.OnProgress
        runCatching {
            getGuildInfoUseCase()
        }.onSuccess { result ->
            _guildInfoData.value = result.getOrThrow()
        }.onFailure { ex ->
            emitErrorMessage("길드 정보를 불러오는데 실패했습니다.", ex)
        }
        _state.value = HomeState.Init
    }

    private suspend fun emitErrorMessage(userMessage: String, ex: Throwable) {
        _eventFlow.emit(
            HomeEvent.DataFetch.Error(
                userMessage = userMessage,
                exceptionMessage = ex.message
            )
        )
    }
}
