package com.example.presentation.screen.item

import com.example.domain.model.feature.types.EquipCategory
import com.example.domain.model.feature.types.EquipFilterType

data class EquipDetail(
    val id: Long,
    val name: String,
    val reinforcement: Long,
    val ownerName: String, // 일단 placeholder로 처리
    val ownerId: Long, // [추가] 장착 대상 ID (0이면 미착용)
    val description: String,
    val statDescription: String, // 예: 공격력 +3 (+3)
    val obtainMethod: String = "상점 구매", // 데이터 없음, 하드코딩
    val category: EquipCategory,
    val sellPrice: Long
)

data class DisplayedEquip(
    val customizedId: Long,
    val name: String,
    val category: EquipCategory,
    val reinforcement: Long,
    val rank: String
)

data class EquipData(
    val displayedEquipments: List<DisplayedEquip>,
    val selectedFilter: EquipFilterType,
    val selectedSort: EquipSortType,
) {
    companion object {
        fun empty() = EquipData(
            displayedEquipments = emptyList(),
            selectedFilter = EquipFilterType.ALL,
            selectedSort = EquipSortType.DEFAULT,
        )
    }
}