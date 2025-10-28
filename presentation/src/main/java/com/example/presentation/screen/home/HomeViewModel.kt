package com.example.presentation.screen.home

import com.example.domain.model.feature.guild.GuildInfoData
import com.example.domain.usecase.feature.guild.GetGuildInfoUseCase
import com.example.domain.usecase.feature.user.GetPlayerMoneyUseCase
import com.example.presentation.utils.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val getGuildInfoUseCase: GetGuildInfoUseCase,
    private val getPlayerMoneyUseCase: GetPlayerMoneyUseCase
) : BaseViewModel() {

    private val _state = MutableStateFlow<HomeState>(HomeState.Init)
    val state: StateFlow<HomeState> = _state

    private val _eventFlow = MutableSharedFlow<HomeEvent>()
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
        }
    }

    init {
        launch {
            initAndRefresh()
        }
    }

    private suspend fun initAndRefresh() {
        _state.value = HomeState.OnProgress
        runCatching {
            getGuildInfo()
            getPlayerMoney()
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

    private suspend fun getPlayerMoney() {
        _state.value = HomeState.OnProgress
        runCatching {
            getPlayerMoneyUseCase()
        }.onSuccess { result ->
            _guildMoney.value = result.getOrThrow()
        }.onFailure { ex ->
            emitErrorMessage("소지 재화를 불러오는데 실패했습니다.", ex)
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
