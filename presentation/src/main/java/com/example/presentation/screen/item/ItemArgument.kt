package com.example.presentation.screen.item

import com.example.presentation.utils.error.ErrorEvent
import kotlinx.coroutines.flow.Flow

data class ItemArgument(
    val intent: (ItemIntent) -> Unit,
    val state: ItemState,
    val event: Flow<ItemEvent>,
    val selectedItemDetail: com.example.domain.model.feature.inventory.Item?,
    val selectedEquipDetail: EquipDetail? = null
)

sealed class ItemState {
    data object Init : ItemState()
    data object OnProgress : ItemState()
}

enum class ItemFilterType(val displayName: String) {
    ALL("전체"),
    CONSUMABLE("소비"),
    BUFF("버프"),
    INGREDIENT("재료"),
    ETC("기타")
}

enum class ItemSortType(val displayName: String) {
    DEFAULT("기본값"),
    NAME("이름순"),
    PRICE("가격순"),
    QUANTITY("개수순")
}

enum class EquipFilterType(val displayName: String) {
    ALL("전체"),
    WEAPON("무기"),
    ARMOR("방어구"),
    ACCESSORY("장신구")
}

enum class EquipSortType(val displayName: String) {
    DEFAULT("기본값"),
    REINFORCEMENT("강화순"),
    RANK("등급순")
}

sealed interface ItemIntent {
    data class OnItemClick(val itemId: Long) : ItemIntent
    data class OnFilterChange(val filterType: ItemFilterType) : ItemIntent
    data class OnSortChange(val sortType: ItemSortType) : ItemIntent

    data class OnEquipFilterChange(val filterType: EquipFilterType) : ItemIntent
    data class OnEquipSortChange(val sortType: EquipSortType) : ItemIntent
    data class OnEquipClick(val equipId: Long) : ItemIntent

    data class OnSellItem(val itemId: Long, val quantity: Int) : ItemIntent
    data class OnSellEquip(val equipId: Long) : ItemIntent
}

sealed class ItemEvent {
    sealed class DataFetch : ItemEvent() {
        data class Error(
            override val userMessage: String = "문제가 발생했습니다.",
            override val exceptionMessage: String?
        ) : DataFetch(), ErrorEvent
    }
}
