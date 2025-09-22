package com.example.data.remote.local.repository

import com.example.data.common.toEntity
import com.example.data.remote.local.dao.ExampleDao
import com.example.domain.model.ExampleModel
import com.example.domain.repository.ExampleRepository
import javax.inject.Inject

class ExampleRepositoryImpl @Inject constructor(
    private val exampleDao: ExampleDao
) : ExampleRepository {

    override suspend fun createExampleEntity(exampleModel: ExampleModel) =
        runCatching {
            exampleDao.createExampleEntity(exampleModel.toEntity())
        }.onFailure { ex ->
            throw Exception(ex)
        }
}