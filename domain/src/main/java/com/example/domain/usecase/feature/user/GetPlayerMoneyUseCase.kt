package com.example.domain.usecase.feature.user

import com.example.domain.repository.feature.guild.GuildRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetPlayerMoneyUseCase @Inject constructor(
    private val repository: GuildRepository
) {
    operator fun invoke(): Flow<Long> = repository.observeGold()
}
