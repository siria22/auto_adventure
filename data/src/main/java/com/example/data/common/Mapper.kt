package com.example.data.common

import com.example.data.remote.local.entity.ExampleEntity
import com.example.domain.model.ExampleModel

fun ExampleModel.toEntity(): ExampleEntity {
    return ExampleEntity(id = id, name = name)
}

fun ExampleEntity.toDomain(): ExampleModel {
    return ExampleModel(id = id, name = name)
}