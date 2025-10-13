package com.example.data.remote.feature.actor.repository

import com.example.data.remote.feature.actor.model.job.JobProvider
import com.example.data.remote.feature.actor.toDomain
import com.example.domain.model.feature.actor.base.Job
import com.example.domain.repository.feature.actor.JobRepository
import javax.inject.Inject

class JobRepositoryImpl @Inject constructor(
    private val jobProvider: JobProvider
) : JobRepository {
    override suspend fun getJobList(): List<Job> {
        return jobProvider.getAllJobs().map { it.toDomain() }
    }

    override suspend fun getJobById(jobId: Long): Result<Job> {
        return jobProvider.getJob(jobId).map { it.toDomain() }
    }
}