package com.example.domain.usecase.example

import com.example.domain.model.ExampleModel
import com.example.domain.repository.ExampleRepository
import javax.inject.Inject

class CreateExampleUseCase @Inject constructor(
    private val exampleRepository: ExampleRepository
) {
    suspend operator fun invoke(newExampleModel: ExampleModel): Result<Unit> {
        return exampleRepository.createExampleEntity(newExampleModel)
    }
}
