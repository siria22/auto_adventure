package com.example.presentation.screen.item

import androidx.lifecycle.viewModelScope
import com.example.domain.model.feature.inventory.BaseEquip
import com.example.domain.model.feature.inventory.CustomizedEquip
import com.example.domain.model.feature.types.EquipCategory
import com.example.domain.repository.feature.guild.GuildRepository
import com.example.domain.repository.feature.inventory.BaseEquipRepository
import com.example.domain.repository.feature.inventory.CustomizedEquipRepository
import com.example.presentation.utils.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class EquipViewModel @Inject constructor(
    private val baseEquipRepository: BaseEquipRepository,
    private val customizedEquipRepository: CustomizedEquipRepository,
    private val guildRepository: GuildRepository
) : BaseViewModel() {

    private val _eventFlow = MutableSharedFlow<ItemEvent>()
    val eventFlow: SharedFlow<ItemEvent> = _eventFlow

    private val _allBaseEquips = MutableStateFlow<List<BaseEquip>>(emptyList())
    private val _allCustomizedEquips = MutableStateFlow<List<CustomizedEquip>>(emptyList())

    private val _selectedEquipFilter = MutableStateFlow(EquipFilterType.ALL)
    val selectedEquipFilter: StateFlow<EquipFilterType> = _selectedEquipFilter

    private val _selectedEquipSort = MutableStateFlow(EquipSortType.DEFAULT)
    val selectedEquipSort: StateFlow<EquipSortType> = _selectedEquipSort

    val displayedEquipments: StateFlow<List<DisplayedEquip>> = combine(
        _allBaseEquips, _allCustomizedEquips, _selectedEquipFilter, _selectedEquipSort
    ) { baseEquips, customizedEquips, filter, sort ->
        applyEquipFilterAndSort(baseEquips, customizedEquips, filter, sort)
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )

    private val _selectedEquipDetail = MutableStateFlow<EquipDetail?>(null)
    val selectedEquipDetail: StateFlow<EquipDetail?> = _selectedEquipDetail

    fun onIntent(intent: ItemIntent) {
        when (intent) {
            is ItemIntent.OnEquipClick -> {
                viewModelScope.launch {
                    loadEquipDetail(intent.equipId)
                }
            }

            is ItemIntent.OnEquipFilterChange -> {
                _selectedEquipFilter.value = intent.filterType
            }

            is ItemIntent.OnEquipSortChange -> {
                _selectedEquipSort.value = intent.sortType
            }

            is ItemIntent.OnSellEquip -> {
                viewModelScope.launch {
                    sellEquip(intent.equipId)
                }
            }

            else -> {

            }
        }
    }

    init {
        viewModelScope.launch {
            loadAllEquipments()
        }
    }

    private suspend fun sellEquip(customizedId: Long) {
        val targetEquip = _allCustomizedEquips.value.find { it.id == customizedId }
            ?: customizedEquipRepository.getEquipById(customizedId)
            ?: run {
                _eventFlow.emit(
                    ItemEvent.DataFetch.Error(
                        userMessage = "판매할 장비를 찾을 수 없습니다.",
                        exceptionMessage = null
                    )
                )
                return
            }

        runCatching {
            guildRepository.updateGold(targetEquip.modifiedSellPrice)
            customizedEquipRepository.deleteEquip(targetEquip)
        }.onSuccess {
            loadAllEquipments()
        }.onFailure { ex ->
            _eventFlow.emit(
                ItemEvent.DataFetch.Error(
                    userMessage = "장비 판매 중 오류가 발생했습니다.",
                    exceptionMessage = ex.message
                )
            )
        }
    }

    private suspend fun loadAllEquipments() {
        runCatching {
            _allBaseEquips.value = baseEquipRepository.getBaseEquipList()

            val playerEquips = customizedEquipRepository.getEquipsByOwnerId(1L)
            val unequippedEquips = customizedEquipRepository.getEquipsByOwnerId(0L)

            _allCustomizedEquips.value = playerEquips + unequippedEquips
        }.onFailure { ex ->
            _eventFlow.emit(
                ItemEvent.DataFetch.Error(
                    userMessage = "전체 장비 정보를 불러오는데 실패했습니다.",
                    exceptionMessage = ex.message
                )
            )
        }
    }

    private fun applyEquipFilterAndSort(
        baseEquips: List<BaseEquip>,
        customizedEquips: List<CustomizedEquip>,
        filter: EquipFilterType,
        sort: EquipSortType
    ): List<DisplayedEquip> {
        if (baseEquips.isEmpty() || customizedEquips.isEmpty()) {
            return emptyList()
        }

        val baseEquipMap = baseEquips.associateBy { it.id }

        val combinedList = customizedEquips.mapNotNull { customized ->
            baseEquipMap[customized.equipId]?.let { base ->
                DisplayedEquip(
                    customizedId = customized.id,
                    name = base.name,
                    category = base.category,
                    reinforcement = customized.reinforcement,
                    rank = customized.rank
                )
            }
        }

        val filteredList = when (filter) {
            EquipFilterType.ALL -> combinedList
            EquipFilterType.WEAPON -> combinedList.filter { it.category == EquipCategory.WEAPON || it.category == EquipCategory.SIDEARM }
            EquipFilterType.ARMOR -> combinedList.filter { it.category == EquipCategory.ARMOR || it.category == EquipCategory.GLOVES || it.category == EquipCategory.SHOES }
            EquipFilterType.ACCESSORY -> combinedList.filter { it.category == EquipCategory.ACCESSORY }
        }

        return when (sort) {
            EquipSortType.DEFAULT -> filteredList
            EquipSortType.REINFORCEMENT -> filteredList.sortedByDescending { it.reinforcement }
            EquipSortType.RANK -> filteredList.sortedBy { it.rank } // 등급 정렬 기준은 나중에 구체화 필요
        }
    }

    private suspend fun loadEquipDetail(equipId: Long) {
        _selectedEquipDetail.value = null
        runCatching {
            val customized = customizedEquipRepository.getEquipById(equipId)
                ?: throw NoSuchElementException("장비를 찾을 수 없습니다.")

            val base = _allBaseEquips.value.find { it.id == customized.equipId }
                ?: baseEquipRepository.getBaseEquipById(customized.equipId)
                    .getOrNull() // <-- 여기 수정 (getOrNull 추가)
                ?: throw NoSuchElementException("베이스 장비 정보를 찾을 수 없습니다.")

            EquipDetail(
                id = customized.id,
                name = base.name,
                reinforcement = customized.reinforcement,
                ownerName = if (customized.ownerId == 0L) "미착용" else "플레이어", // 미착용 처리
                description = base.description,
                statDescription = "${base.increaseStat.name} +${base.increaseAmount} (+${customized.getIncreasedAmount()})",
                category = base.category,
                sellPrice = customized.modifiedSellPrice
            )
        }.onSuccess { detail ->
            _selectedEquipDetail.value = detail
        }.onFailure { ex ->
            _eventFlow.emit(
                ItemEvent.DataFetch.Error(
                    userMessage = "장비 상세 정보를 불러오는데 실패했습니다.",
                    exceptionMessage = ex.message
                )
            )
        }
    }
}