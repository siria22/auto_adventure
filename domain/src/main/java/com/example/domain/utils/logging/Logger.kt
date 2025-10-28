package com.example.domain.utils.logging

import com.example.domain.model.feature.types.LogCategory
import com.example.domain.model.nonfeature.log.DomainLog
import com.example.domain.usecase.nonfeature.log.GetLastLogIdUseCase
import com.example.domain.usecase.nonfeature.log.LogEventUseCase
import java.time.LocalDate
import javax.inject.Inject

class Logger @Inject constructor(
    private val logEventUseCase: LogEventUseCase,
    private val getLastLogIdUseCase: GetLastLogIdUseCase
) {
    private var isInitialized = false

    private val logBuffer = mutableListOf<DomainLog>()
    private var lastLog: DomainLog? = null
    private var idCounter: Long = 0L

    suspend fun initialize() {
        runCatching { getLastLogIdUseCase() }
            .onSuccess {
                idCounter = it.getOrThrow() ?: 0L
                isInitialized = true
            }
            .onFailure { ex ->
                idCounter = -1L
                throw Exception(ex)
            }
    }

    fun appendLog(
        title: String,
        description: String,
        category: LogCategory,
        revealAfter: Int
    ): Logger {
        if (!isInitialized) throw Exception("Logger must be initialized. Call initialize() first.\n" +
                "See details in Logger.md")
        val revealTime = if (revealAfter < 0) 0 else revealAfter.toLong()

        idCounter++
        val newLog = DomainLog(
            id = idCounter,
            depth = 1,
            parentId = -1L,
            category = category,
            title = title,
            description = description,
            revealTime = LocalDate.now().plusDays(revealTime),
            creationTime = LocalDate.now()
        )
        logBuffer.add(newLog)
        lastLog = newLog
        return this
    }

    fun appendChildLog(
        title: String,
        description: String,
        category: LogCategory,
        revealAfter: Int
    ): Logger {
        val parent = lastLog
            ?: throw IllegalStateException("appendChildLog can only be called after appendLog or another appendChildLog.")
        val revealTime = if (revealAfter < 0) 0 else revealAfter.toLong()

        idCounter++
        val childLog = DomainLog(
            id = idCounter,
            depth = parent.depth + 1,
            parentId = parent.id,
            category = category,
            title = title,
            description = description,
            revealTime = LocalDate.now().plusDays(revealTime),
            creationTime = LocalDate.now()
        )
        logBuffer.add(childLog)
        lastLog = childLog
        return this
    }

    fun appendLogAtSameLevel(
        title: String,
        description: String,
        category: LogCategory,
        revealAfter: Int
    ): Logger {
        val parentLogId = lastLog?.parentId ?: throw java.lang.IllegalStateException("appendLogAtSameLevel can only be called after appendLog or appendChildLog.")
        val currentDepth = lastLog?.depth ?: 1

        val revealTime = if (revealAfter < 0) 0 else revealAfter.toLong()

        idCounter++
        val childLog = DomainLog(
            id = idCounter,
            depth = currentDepth,
            parentId = parentLogId,
            category = category,
            title = title,
            description = description,
            revealTime = LocalDate.now().plusDays(revealTime),
            creationTime = LocalDate.now()
        )
        logBuffer.add(childLog)
        lastLog = childLog
        return this
    }

    suspend fun exportLogs() {
        if (logBuffer.isNotEmpty()) {
            logEventUseCase(logBuffer.toList())
        }
        clear()
    }

    private fun clear() {
        logBuffer.clear()
        idCounter = -1L
        lastLog = null
    }
}