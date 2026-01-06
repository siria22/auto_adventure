package com.example.presentation.screen.item

import com.example.domain.model.feature.types.EquipCategory

data class EquipDetail(
    val id: Long,
    val name: String,
    val reinforcement: Long,
    val ownerName: String, // 일단 placeholder로 처리
    val description: String,
    val statDescription: String, // 예: 공격력 +3 (+3)
    val obtainMethod: String = "상점 구매", // 데이터 없음, 하드코딩
    val category: EquipCategory,
    val sellPrice: Long
)

// UI 표시에 필요한 정보만 모아놓은 새로운 데이터 클래스
data class DisplayedEquip(
    val customizedId: Long, // CustomizedEquip의 고유 ID
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