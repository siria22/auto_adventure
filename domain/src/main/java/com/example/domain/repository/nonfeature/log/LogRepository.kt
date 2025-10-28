package com.example.domain.repository.nonfeature.log

import com.example.domain.model.feature.types.LogCategory
import com.example.domain.model.nonfeature.log.DomainLog
import java.time.LocalDate

interface LogRepository {

    suspend fun createLogs(logs: List<DomainLog>): Result<Unit>
    suspend fun getLastLogId(): Result<Long?>

    suspend fun getAllLog(): Result<List<DomainLog>>
    suspend fun getChildrenLogByParentId(parentId: Long): Result<List<DomainLog>>
    suspend fun getLogByCategory(category: LogCategory): Result<List<DomainLog>>

    suspend fun getAllLogWithTimeConstraint(revealTime: LocalDate = LocalDate.now()): Result<List<DomainLog>>
    suspend fun getLogByCategoryWithTimeConstraint(
        category: LogCategory,
        revealTime: LocalDate = LocalDate.now()
    ): Result<List<DomainLog>>
}