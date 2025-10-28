package com.example.data.remote.nonfeature.log.repository

import com.example.data.remote.nonfeature.log.dao.LogDao
import com.example.data.remote.nonfeature.log.toDomain
import com.example.data.remote.nonfeature.log.toEntity
import com.example.domain.model.feature.types.LogCategory
import com.example.domain.model.nonfeature.log.DomainLog
import com.example.domain.repository.nonfeature.log.LogRepository
import java.time.LocalDate
import javax.inject.Inject

class LogRepositoryImpl @Inject constructor(
    private val dao: LogDao
) : LogRepository {

    override suspend fun createLogs(logs: List<DomainLog>): Result<Unit> = runCatching {
        dao.insert(logs.map { it.toEntity() })
    }

    override suspend fun getLastLogId(): Result<Long?> = runCatching {
        dao.getLastLogId()
    }

    override suspend fun getAllLog(): Result<List<DomainLog>> = runCatching {
        dao.getAllLog().map { it.toDomain() }
    }

    override suspend fun getChildrenLogByParentId(parentId: Long): Result<List<DomainLog>> =
        runCatching {
            dao.getChildrenLogByParentId(parentId).map { it.toDomain() }
        }

    override suspend fun getLogByCategory(category: LogCategory): Result<List<DomainLog>> =
        runCatching {
            dao.getLogByCategory(category.name).map { it.toDomain() }
        }

    override suspend fun getAllLogWithTimeConstraint(revealTime: LocalDate): Result<List<DomainLog>> =
        runCatching {
            dao.getAllLogWithTimeConstraint(revealTime).map { it.toDomain() }
        }

    override suspend fun getLogByCategoryWithTimeConstraint(
        category: LogCategory,
        revealTime: LocalDate
    ): Result<List<DomainLog>> = runCatching {
        dao.getLogByCategoryWithTimeConstraint(category.name, revealTime).map { it.toDomain() }
    }


}
