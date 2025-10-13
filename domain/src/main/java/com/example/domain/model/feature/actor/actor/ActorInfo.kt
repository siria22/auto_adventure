package com.example.domain.model.feature.actor.actor

import com.example.domain.model.feature.actor.base.Job
import com.example.domain.model.feature.actor.base.Personality
import com.example.domain.model.feature.types.ActorCategory
import kotlin.math.pow

data class ActorInfo(
    val name: String,
    val race: String,
    val job: Job,
    val actorCategory: ActorCategory,
    val elementType: String,
    val personality: Personality,
    val rank: String,
    val level: Long,
    val totalExp: Long
) {
    fun updateLevelFromTotalExp(): ActorInfo =
        this.copy(level = someExpToLevelConverterFunction(totalExp))
}

//TODO : 실제 경험치 -> level 변환 함수
// 우선은 Mocking 함수 사용
fun someExpToLevelConverterFunction(totalExp: Long): Long {
    var level = 1L
    var accumulatedExp = 0L

    while (true) {
        val nextExp = (100 * level.toDouble().pow(1.5)).toLong()
        if (accumulatedExp + nextExp > totalExp) {
            break
        }
        accumulatedExp += nextExp
        level++
    }

    return level
}
