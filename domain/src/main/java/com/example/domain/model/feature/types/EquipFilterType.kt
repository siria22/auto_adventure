package com.example.domain.model.feature.types

enum class EquipFilterType(val displayName: String) {
    ALL("전체"),
    WEAPON("무기"),
    ARMOR("방어구"),
    ACCESSORY("장신구"),
    GLOVES("장갑"),
    SHOES("신발");

    fun matches(category: EquipCategory): Boolean {
        return when (this) {
            ALL -> true
            WEAPON -> category == EquipCategory.WEAPON || category == EquipCategory.SIDEARM
            ARMOR -> category == EquipCategory.ARMOR
            ACCESSORY -> category == EquipCategory.ACCESSORY
            GLOVES -> category == EquipCategory.GLOVES
            SHOES -> category == EquipCategory.SHOES
        }
    }
}