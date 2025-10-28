package com.example.presentation.component.test

import android.util.Log
import com.example.domain.model.nonfeature.log.DomainLog
import com.example.domain.usecase.nonfeature.log.GetAllLogUseCase
import com.example.presentation.utils.BaseViewModel
import com.example.presentation.utils.error.ErrorEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel
class TestLogViewModel @Inject constructor(
    private val getAllLogUseCase: GetAllLogUseCase
) : BaseViewModel() {

    private val _eventFlow = MutableSharedFlow<TestLogEvent>()
    val eventFlow: SharedFlow<TestLogEvent> = _eventFlow

    private val _logs = MutableStateFlow<List<DomainLog>>(emptyList())
    val logs: StateFlow<List<DomainLog>> = _logs

    fun onIntent(intent: TestLogIntent) {
        when (intent) {
            is TestLogIntent.Refresh -> {
                launch { getAllLogs() }
            }
        }
    }

    init {
        launch {
            getAllLogs()
        }
    }

    suspend fun getAllLogs() {
        runCatching {
            getAllLogUseCase()
        }.onSuccess { result ->
            _logs.value = result.getOrThrow()
            Log.d("AutoAdventure - TestLog", "getAllLogs : ${_logs.value}")
        }.onFailure { ex ->
            _eventFlow.emit(
                TestLogEvent.DataFetch.Error(
                    userMessage = "로그를 가져오는데 실패했습니다.",
                    exceptionMessage = ex.message
                )
            )
        }
    }
}

sealed class TestLogIntent {
    data object Refresh : TestLogIntent()
}

sealed class TestLogEvent {
    sealed class DataFetch : TestLogEvent() {
        data class Error(
            override val userMessage: String = "문제가 발생했습니다.",
            override val exceptionMessage: String?
        ) : DataFetch(), ErrorEvent
    }
}