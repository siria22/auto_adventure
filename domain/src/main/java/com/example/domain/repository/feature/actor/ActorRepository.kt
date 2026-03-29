package com.example.domain.repository.feature.actor

import com.example.domain.model.feature.actor.base.BaseActor

interface ActorRepository {
    suspend fun insertActor(actor: BaseActor): Long
    suspend fun getAllActors(): List<BaseActor>
    suspend fun getActorById(id: Long): BaseActor?
    suspend fun getActorByName(name: String): BaseActor?
    suspend fun getRecruitedActorCount(): Int
    suspend fun getAvailableActors(): List<BaseActor>
}