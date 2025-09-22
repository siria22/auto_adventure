package com.example.domain.repository

import com.example.domain.model.ExampleModel

interface ExampleRepository {
    suspend fun createExampleEntity(exampleModel: ExampleModel): Result<Unit>

}