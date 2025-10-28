package com.example.presentation.screen.test

import com.example.presentation.utils.error.ErrorEvent
import kotlinx.coroutines.flow.SharedFlow

data class TestArgument(
    val intent: (TestIntent) -> Unit,
    val state: TestState,
    val event: SharedFlow<TestEvent>
)

sealed class TestState {
    data object Init : TestState()
    data object OnProgress : TestState()
}

sealed class TestIntent {
    data class SomeIntentWithParams(val param: String) : TestIntent()
    data object SomeIntentWithoutParams : TestIntent()
}

sealed class TestEvent {
    sealed class DataFetch : TestEvent() {
        data class Error(
            override val userMessage: String = "문제가 발생했습니다.",
            override val exceptionMessage: String?
        ) : DataFetch(), ErrorEvent
    }
}