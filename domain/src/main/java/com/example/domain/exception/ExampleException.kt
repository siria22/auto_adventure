package com.example.domain.exception

class ExampleException(
    override val message: String = "Example Exception",
    override val cause: Throwable? = null
) : Exception(message, cause)
