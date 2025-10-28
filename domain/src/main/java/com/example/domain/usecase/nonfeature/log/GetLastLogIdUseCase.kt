package com.example.domain.usecase.nonfeature.log

import com.example.domain.repository.nonfeature.log.LogRepository
import javax.inject.Inject

class GetLastLogIdUseCase @Inject constructor(
    private val repository: LogRepository
) {
    suspend operator fun invoke(): Result<Long?> {
        return repository.getLastLogId()
    }
}
