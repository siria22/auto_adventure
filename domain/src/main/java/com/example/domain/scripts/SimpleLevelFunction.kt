package com.example.domain.scripts

import kotlin.math.pow

// --- 1. 튜닝 상수 (이 값을 조절하세요) ---
const val BASE_EXP = 100.0

const val GROWTH_RATE = 20.0

const val POWER = 1.55

const val MAX_LEVEL = 200

/**
 * 가장 간단하고 보편적인 'Power' 함수 기반 경험치 곡선입니다.
 * 공식: Cost = Base + (Level ^ Power) * GrowthRate
 *
 * @param currentLevel 현재 레벨 (1 ~ 99)
 * @return 다음 레벨 (예: 2)로 가기 위해 필요한 경험치
 */
fun getExpForNextLevel(currentLevel: Long): Long {
    if (currentLevel >= MAX_LEVEL) return 0L
    return (BASE_EXP + (currentLevel.toDouble().pow(POWER) * GROWTH_RATE)).toLong()
}

/**
 * [신규 함수]
 * 누적 경험치(totalExp)로부터 현재 레벨을 역산합니다.
 *
 * @param totalExp 현재까지 쌓은 총 경험치
 * @return 현재 레벨 (1 ~ MAX_LEVEL)
 */
fun getLevelFromTotalExp(totalExp: Long): Long {
    var currentLevel = 1L
    var remainingExp = totalExp

    while (currentLevel < MAX_LEVEL) {
        val expToNext = getExpForNextLevel(currentLevel)

        if (remainingExp >= expToNext) {
            remainingExp -= expToNext
            currentLevel++
        } else {
            break
        }
    }

    return currentLevel
}
