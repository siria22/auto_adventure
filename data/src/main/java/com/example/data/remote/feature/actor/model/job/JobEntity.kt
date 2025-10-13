package com.example.data.remote.feature.actor.model.job

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class JobEntity(
    @SerialName("job_id")
    val jobId: Long,

    @SerialName("name_kor")
    val nameKor: String,

    @SerialName("name_eng")
    val nameEng: String,

    @SerialName("description")
    val description: String
)