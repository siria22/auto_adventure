package com.example.presentation.screen.shop

import com.example.domain.model.feature.inventory.BaseEquip
import com.example.domain.model.feature.inventory.Item
import com.example.domain.model.feature.types.EquipFilterType
import com.example.domain.model.feature.types.ItemFilterType

data class ShopData(
    val userGold: Long,
    val items: List<Item>,
    val baseEquips: List<BaseEquip>,
    val selectedItemToBuy: Item?,
    val selectedEquipToBuy: BaseEquip?,
    val itemFilter: ItemFilterType,
    val itemSort: ShopSortType,
    val equipFilter: EquipFilterType,
    val equipSort: ShopSortType
) {
    companion object {
        fun init() = ShopData(
            userGold = 0L,
            items = emptyList(),
            baseEquips = emptyList(),
            selectedItemToBuy = null,
            selectedEquipToBuy = null,
            itemFilter = ItemFilterType.ALL,
            itemSort = ShopSortType.DEFAULT,
            equipFilter = EquipFilterType.ALL,
            equipSort = ShopSortType.DEFAULT
        )
    }
}