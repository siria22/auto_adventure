package com.example.presentation.utils.error

interface ErrorEvent {
    val userMessage: String
    val exceptionMessage: String?
}