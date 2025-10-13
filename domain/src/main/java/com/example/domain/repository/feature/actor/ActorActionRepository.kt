package com.example.domain.repository.feature.actor

import com.example.domain.model.feature.actor.base.BaseActorAction

interface ActorActionRepository {

    suspend fun insertAction(action: BaseActorAction)
    suspend fun getAllActions(): List<BaseActorAction>
    suspend fun getActionsByActorId(characterId: Long): List<BaseActorAction>
    suspend fun getActionById(characterId: Long, index: Long): BaseActorAction?
}