package com.example.data.remote.feature.actor.repository

import com.example.data.remote.feature.actor.dao.ActorDao
import com.example.data.remote.feature.actor.toDomain
import com.example.data.remote.feature.actor.toEntity
import com.example.domain.model.feature.actor.base.BaseActor
import com.example.domain.repository.feature.actor.ActorRepository
import javax.inject.Inject

class ActorRepositoryImpl @Inject constructor(
    private val actorDao: ActorDao
) : ActorRepository {
    override suspend fun insertActor(actor: BaseActor) {
        actorDao.insert(actor.toEntity())
    }

    override suspend fun getAllActors(): List<BaseActor> {
        return actorDao.getAllActors().map { it.toDomain() }
    }

    override suspend fun getActorById(id: Long): BaseActor? {
        return actorDao.getActorById(id)?.toDomain()
    }

    override suspend fun getActorByName(name: String): BaseActor? {
        return actorDao.getActorByName(name)?.toDomain()
    }

    override suspend fun getRecruitedActorCount(): Int {
        return actorDao.getRecruitedActorCount()
    }
}
