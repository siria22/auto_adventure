package com.example.domain.usecase.feature.actor

import com.example.domain.model.feature.actor.base.BaseActor
import com.example.domain.repository.feature.actor.ActorRepository
import javax.inject.Inject

class GetAvailableActorsUseCase @Inject constructor(
    private val actorRepository: ActorRepository
) {
    suspend operator fun invoke(): Result<List<BaseActor>> = runCatching {
        actorRepository.getAvailableActors()
    }
}