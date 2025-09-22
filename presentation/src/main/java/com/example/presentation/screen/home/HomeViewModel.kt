package com.example.presentation.screen.home

import androidx.lifecycle.SavedStateHandle
import com.example.presentation.utils.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    // private val someUseCase: SomeUseCase
) : BaseViewModel() {

    private val _state = MutableStateFlow<HomeState>(HomeState.Init)
    val state: StateFlow<HomeState> = _state

    private val _eventFlow = MutableSharedFlow<HomeEvent>()
    val eventFlow: SharedFlow<HomeEvent> = _eventFlow

    private val _someData = MutableStateFlow("")
    val someData: StateFlow<String> = _someData

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

        }
    }

    // some function
    private suspend fun someFunction() {
        _state.value = HomeState.OnProgress

        runCatching {
            // someUseCase()
        }.onSuccess { result ->
            // some.value = result.getOrThrow()
        }.onFailure { exception ->
            // some.value = emptyList()
            _eventFlow.emit(
                HomeEvent.DataFetch.Error(
                    userMessage = "Error messages to be shown to users",
                    exceptionMessage = exception.message
                )
            )
        }
        _state.value = HomeState.Init
    }
}
