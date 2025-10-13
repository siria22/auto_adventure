package com.example.data.remote.feature.actor.model.job

import android.content.Context
import com.example.data.remote.feature.ACTOR_ASSET_ROUTE
import kotlinx.serialization.json.Json
import javax.inject.Inject

class JobProvider @Inject constructor(
    private val context: Context
) {
    private val jobs: List<JobEntity> by lazy {
        parseJobs()
    }

    private fun parseJobs(): List<JobEntity> {
        val jsonString =
            context.assets.open("${ACTOR_ASSET_ROUTE}/Jobs.json").bufferedReader()
                .use { it.readText() }
        return Json.decodeFromString<List<JobEntity>>(jsonString)
    }

    fun getAllJobs(): List<JobEntity> {
        return jobs
    }

    fun getJob(id: Long): Result<JobEntity> {
        return jobs.find { it.jobId == id }?.let {
            Result.success(it)
        } ?: Result.failure(NoSuchElementException("Job with id $id not found"))
    }
}