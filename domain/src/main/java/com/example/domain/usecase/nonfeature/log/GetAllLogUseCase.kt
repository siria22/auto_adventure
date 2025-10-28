package com.example.domain.usecase.nonfeature.log

import com.example.domain.model.nonfeature.log.DomainLog
import com.example.domain.repository.nonfeature.log.LogRepository
import javax.inject.Inject

class GetAllLogUseCase @Inject constructor(
    private val repository: LogRepository
) {
    suspend operator fun invoke(): Result<List<DomainLog>> {
        return repository.getAllLog()
    }
}
