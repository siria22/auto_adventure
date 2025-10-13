package com.example.data.remote.feature.actor.repository

import com.example.data.remote.feature.actor.dao.ActorActionDao
import com.example.data.remote.feature.actor.toDomain
import com.example.data.remote.feature.actor.toEntity
import com.example.domain.model.feature.actor.base.BaseActorAction
import com.example.domain.repository.feature.actor.ActorActionRepository
import javax.inject.Inject

class ActorActionRepositoryImpl @Inject constructor(
    private val actorActionDao: ActorActionDao
) : ActorActionRepository {
    override suspend fun insertAction(action: BaseActorAction) {
        actorActionDao.insert(action.toEntity())
    }

    override suspend fun getAllActions(): List<BaseActorAction> {
        return actorActionDao.getAllActions().map { it.toDomain() }
    }

    override suspend fun getActionsByActorId(characterId: Long): List<BaseActorAction> {
        return actorActionDao.getActionsByActorId(characterId).map { it.toDomain() }
    }

    override suspend fun getActionById(characterId: Long, index: Long): BaseActorAction? {
        return actorActionDao.getActionById(characterId, index)?.toDomain()
    }
}
