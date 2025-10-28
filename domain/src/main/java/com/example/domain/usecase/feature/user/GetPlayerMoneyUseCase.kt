package com.example.domain.usecase.feature.user

import com.example.domain.repository.feature.guild.GuildRepository
import javax.inject.Inject

class GetPlayerMoneyUseCase @Inject constructor(
    private val repository: GuildRepository
) {
    suspend operator fun invoke(): Result<Long> = runCatching {
        repository.getGold()
    }
}
