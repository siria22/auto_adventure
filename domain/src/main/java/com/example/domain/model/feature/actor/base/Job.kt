package com.example.domain.model.feature.actor.base

data class Job(
    val jobId: Long,
    val nameKor: String,
    val nameEng: String,
    val description: String
) {
    fun getHpBonus(): Long = when (jobId){
        1L -> 10L
        2L -> 15L
        3L -> 20L
        4L -> 0L
        5L -> 0L
        6L -> 0L
        else -> 0L
    }

    fun getAttackBonus(): Long = when (jobId){
        1L -> 2L
        2L -> 4L
        3L -> 4L
        4L -> 2L
        5L -> 3L
        6L -> 5L
        else -> 0L
    }

    // todo : 직업 별 스탯 보너스 get 함수
}