package com.example.domain.model.feature.types

enum class ActionStatusFlag {
    NONE, PERFORMED
}

/**
 * Create log 이후, 모험 진행 여부 등을 판단하기 위한 Flag
 * `NONE` : 이상 없음, 모험 속행
 * `RETREAT` : 후퇴 기준 충족, 후퇴
 */
enum class MapEventFlag {
    NONE, RETREAT
}

enum class BattleEntityActionFlag {
    NONE, PERFORMED
}