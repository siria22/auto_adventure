package com.example.presentation.screen.item

import com.example.domain.model.feature.types.EquipFilterType
import com.example.domain.model.feature.types.ItemFilterType
import com.example.presentation.utils.error.ErrorEvent
import kotlinx.coroutines.flow.Flow

data class ItemArgument(
    val intent: (ItemIntent) -> Unit,
    val state: ItemState,
    val event: Flow<ItemEvent>,
    val selectedItemDetail: com.example.domain.model.feature.inventory.Item?,
    val selectedEquipDetail: EquipDetail? = null,
    val reinforceUiState: ReinforceUiState? = null
)

sealed class ItemState {
    data object Init : ItemState()
    data object OnProgress : ItemState()
}

enum class ItemSortType(val displayName: String) {
    DEFAULT("기본값"),
    NAME("이름순"),
    PRICE("가격순"),
    QUANTITY("개수순")
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

    data class OnRequestReinforce(val equipId: Long) : ItemIntent
    data class OnExecuteReinforce(val equipId: Long) : ItemIntent
    data object OnDismissReinforce : ItemIntent

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

    // [추가] 강화 성공/실패 메시지용 이벤트 등도 필요하다면 여기에 추가
    data class ReinforceResult(val isSuccess: Boolean, val message: String) : ItemEvent()
}
