package com.example.domain.repository.feature.actor

import com.example.domain.model.feature.actor.base.Job

interface JobRepository {

    suspend fun getJobList(): List<Job>

    suspend fun getJobById(jobId: Long) : Result<Job>

}