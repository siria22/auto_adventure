package com.example.domain.model.feature.actor.base

data class BaseActorAction(
    val characterId: Long,
    val index: Long,
    val instruction: String,
    val isActivated: Boolean
)