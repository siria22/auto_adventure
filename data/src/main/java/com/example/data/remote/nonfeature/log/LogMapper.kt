package com.example.data.remote.nonfeature.log

import com.example.data.remote.nonfeature.log.entity.LogEntity
import com.example.domain.model.feature.types.toLogCategory
import com.example.domain.model.nonfeature.log.DomainLog

fun LogEntity.toDomain() = DomainLog(
    id = this.id,
    depth = this.depth,
    parentId = this.parentId,
    category = this.category.toLogCategory(),
    title = this.title,
    description = this.description,
    revealTime = this.revealTime,
    creationTime = this.creationTime
)

fun DomainLog.toEntity() = LogEntity(
    id = this.id,
    depth = this.depth,
    parentId = this.parentId,
    category = this.category.name,
    title = this.title,
    description = this.description,
    revealTime = this.revealTime,
    creationTime = this.creationTime
)
