package com.example.domain.exception

class ExampleException(
    override val message: String = "Example Exception",
    override val cause: Throwable? = null
) : Exception(message, cause)


class PartyFullException(
    override val message: String = "이미 파티가 4개 존재합니다.",
    override val cause: Throwable? = null
) : Exception(message, cause)