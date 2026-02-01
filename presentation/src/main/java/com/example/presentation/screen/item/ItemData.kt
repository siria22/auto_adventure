package com.example.presentation.screen.item

import com.example.domain.model.feature.inventory.InventoryItem
import com.example.domain.model.feature.types.ItemFilterType

data class ItemData(
    val displayedItems: List<InventoryItem>,
    val selectedFilter: ItemFilterType,
    val selectedSort: ItemSortType
) {
    companion object {
        fun empty() = ItemData(
            displayedItems = emptyList(),
            selectedFilter = ItemFilterType.ALL,
            selectedSort = ItemSortType.DEFAULT
        )
    }
}