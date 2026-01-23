package com.example.presentation.screen.shop

import com.example.domain.model.feature.types.EquipFilterType
import com.example.domain.model.feature.types.ItemFilterType
import kotlinx.coroutines.flow.SharedFlow

data class ShopArgument(
    val state: ShopState,
    val event: SharedFlow<ShopEvent>,
    val intent: (ShopIntent) -> Unit
)

sealed interface ShopState {
    data object Init : ShopState
    data object Loading : ShopState
    data object Success : ShopState
}

sealed interface ShopEvent {
    data class Error(val message: String) : ShopEvent
    data class ShowToast(val message: String) : ShopEvent
}

enum class ShopSortType(val displayName: String) {
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

    data class OnItemFilterChange(val filter: ItemFilterType) : ShopIntent
    data class OnItemSortChange(val sort: ShopSortType) : ShopIntent
    data class OnEquipFilterChange(val filter: EquipFilterType) : ShopIntent
    data class OnEquipSortChange(val sort: ShopSortType) : ShopIntent
}