package com.example.domain.model.feature.types

enum class ItemCategory {
    HEALING,        // 회복 (상태이상 회복 포함)
    SCROLL,         // 스크롤
    BUFF,           // 버프 아이템
    INGREDIENT,     // 재료 아이템
    ETC             // 기타
}

enum class ObtainMethod {
    MONSTER_DROP,
    QUEST_REWARD,
    SHOP
}
