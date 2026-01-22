package com.example.presentation.screen.shop

import com.example.domain.model.feature.types.EquipCategory
import com.example.domain.model.feature.types.ItemCategory

sealed interface ShopState {
    data object Init : ShopState
    data object Loading : ShopState
    data object Success : ShopState
}

sealed interface ShopEvent {
    data class Error(val message: String) : ShopEvent
    data class ShowToast(val message: String) : ShopEvent
}

enum class ShopItemFilterType(val displayName: String) {
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

enum class ShopItemSortType(val displayName: String) {
    DEFAULT("기본값"),
    NAME("이름순"),
    PRICE_LOW("가격 낮은순"),
    PRICE_HIGH("가격 높은순")
}

enum class ShopEquipFilterType(val displayName: String) {
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

enum class ShopEquipSortType(val displayName: String) {
    DEFAULT("기본값"),
    NAME("이름순"),
    PRICE_LOW("가격 낮은순"),
    PRICE_HIGH("가격 높은순")
}

sealed interface ShopIntent {
    data class OnBuyItemClick(val itemId: Long) : ShopIntent
    data class OnBuyEquipClick(val equipId: Long) : ShopIntent
    data class OnConfirmBuyItem(val itemId: Long, val quantity: Int) : ShopIntent
    data class OnConfirmBuyEquip(val equipId: Long) : ShopIntent
    data object Refresh : ShopIntent

    data class OnItemFilterChange(val filter: ShopItemFilterType) : ShopIntent
    data class OnItemSortChange(val sort: ShopItemSortType) : ShopIntent
    data class OnEquipFilterChange(val filter: ShopEquipFilterType) : ShopIntent
    data class OnEquipSortChange(val sort: ShopEquipSortType) : ShopIntent
}