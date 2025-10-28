package com.example.presentation.screen.test

import androidx.lifecycle.SavedStateHandle
import com.example.domain.utils.logging.Logger
import com.example.presentation.utils.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel
class TestViewModel @Inject constructor(
    private val logger: Logger
) : BaseViewModel() {

    private val _state = MutableStateFlow<TestState>(TestState.Init)
    val state: StateFlow<TestState> = _state

    private val _eventFlow = MutableSharedFlow<TestEvent>()
    val eventFlow: SharedFlow<TestEvent> = _eventFlow

    fun onIntent(intent: TestIntent) {
        when (intent) {
            is TestIntent.SomeIntentWithoutParams -> {
                //do sth
            }

            is TestIntent.SomeIntentWithParams -> {
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
        _state.value = TestState.OnProgress

        runCatching {
            // someUseCase()
        }.onSuccess { result ->
            // some.value = result.getOrThrow()
        }.onFailure { exception ->
            // some.value = emptyList()
            _eventFlow.emit(
                TestEvent.DataFetch.Error(
                    userMessage = "Error messages to be shown to users",
                    exceptionMessage = exception.message
                )
            )
        }
        _state.value = TestState.Init
    }
}


/***
 * create events
 * run events -> create logs
 */
