package com.example.domain.model.nonfeature.log

import com.example.domain.model.feature.types.LogCategory
import java.time.LocalDate

data class DomainLog(
    val id: Long,
    val depth: Int,
    val parentId: Long,
    val category: LogCategory,
    val title: String,
    val description: String,
    val revealTime: LocalDate,
    val creationTime: LocalDate
)