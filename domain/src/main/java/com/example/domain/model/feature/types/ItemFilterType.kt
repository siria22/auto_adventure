package com.example.domain.model.feature.types

enum class ItemFilterType(val displayName: String) {
    ALL("전체"),
    CONSUMABLE("소비"),
    BUFF("버프"),
    INGREDIENT("재료"),
    ETC("기타");

    fun matches(category: ItemCategory): Boolean {
        return when (this) {
            ALL -> true
            CONSUMABLE -> category == ItemCategory.HEALING || category == ItemCategory.SCROLL
            BUFF -> category == ItemCategory.BUFF
            INGREDIENT -> category == ItemCategory.INGREDIENT
            ETC -> category == ItemCategory.ETC
        }
    }
}