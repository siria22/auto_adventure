package com.example.presentation.screen.test

data class TestData(
    val data: String
) {
    companion object {
        fun empty() = TestData(
            data = ""
        )
    }
}