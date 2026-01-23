package com.example.presentation.screen.item

import androidx.lifecycle.viewModelScope
import com.example.domain.model.feature.inventory.BaseEquip
import com.example.domain.model.feature.inventory.CustomizedEquip
import com.example.domain.model.feature.types.EquipFilterType
import com.example.domain.repository.feature.guild.GuildRepository
import com.example.domain.repository.feature.inventory.BaseEquipRepository
import com.example.domain.repository.feature.inventory.CustomizedEquipRepository
import com.example.domain.repository.feature.inventory.InventoryRepository
import com.example.presentation.R
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
    private val guildRepository: GuildRepository,
    private val inventoryRepository: InventoryRepository
) : BaseViewModel() {

    private val _eventFlow = MutableSharedFlow<ItemEvent>()
    val eventFlow: SharedFlow<ItemEvent> = _eventFlow

    private val _allBaseEquips = MutableStateFlow<List<BaseEquip>>(emptyList())
    private val _allCustomizedEquips = MutableStateFlow<List<CustomizedEquip>>(emptyList())

    private val _selectedEquipFilter = MutableStateFlow(EquipFilterType.ALL)
    val selectedEquipFilter: StateFlow<EquipFilterType> = _selectedEquipFilter

    private val _selectedEquipSort = MutableStateFlow(EquipSortType.DEFAULT)
    val selectedEquipSort: StateFlow<EquipSortType> = _selectedEquipSort

    private val _reinforceState = MutableStateFlow<ReinforceUiState?>(null)
    val reinforceState: StateFlow<ReinforceUiState?> = _reinforceState

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

            is ItemIntent.OnRequestReinforce -> {
                viewModelScope.launch {
                    prepareReinforce(intent.equipId)
                }
            }

            is ItemIntent.OnExecuteReinforce -> {
                viewModelScope.launch {
                    executeReinforce(intent.equipId)
                }
            }

            is ItemIntent.OnDismissReinforce -> {
                dismissReinforceDialog()
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

    private suspend fun prepareReinforce(equipId: Long) {
        val targetEquip = _allCustomizedEquips.value.find { it.id == equipId } ?: return
        val currentLevel = targetEquip.reinforcement.toLong()
        val nextLevel = currentLevel + 1

        // 비용 계산 (임시 로직)
        // 골드: (레벨 + 1) * 500
        val requiredGold = nextLevel * 500
        // 재료: 슬라임 점액(ID: 3) (레벨 + 1) * 2 개
        val requiredSlime = nextLevel * 2

        val currentGold = guildRepository.getGold()
        val slimeItem = inventoryRepository.findItem(1L, 3L) // ID 3: 슬라임 점액
        val currentSlime = slimeItem?.amount ?: 0L

        val materials = listOf(
            ReinforceMaterial(
                name = "슬라임 점액",
                iconResId = R.drawable.ic_potion_red, // TODO: 슬라임 아이콘으로 교체 필요
                currentAmount = currentSlime,
                requiredAmount = requiredSlime
            ),
            ReinforceMaterial(
                name = "골드",
                iconResId = R.drawable.ic_potion_red, // TODO: 골드 아이콘으로 교체 필요
                currentAmount = currentGold,
                requiredAmount = requiredGold
            )
        )

        val isAffordable = currentGold >= requiredGold && currentSlime >= requiredSlime

        val detail = createEquipDetail(targetEquip) ?: return

        _reinforceState.value = ReinforceUiState(
            targetEquip = detail,
            materials = materials,
            isAffordable = isAffordable,
            imageUrl = R.drawable.ic_ironsword // TODO: 실제 아이콘 매핑
        )
    }

    private suspend fun executeReinforce(equipId: Long) {
        val state = _reinforceState.value ?: return
        if (!state.isAffordable) {
            _eventFlow.emit(ItemEvent.ReinforceResult(false, "재료가 부족합니다."))
            return
        }

        val goldMaterial = state.materials.find { it.name == "골드" }!!
        val slimeMaterial = state.materials.find { it.name == "슬라임 점액" }!!

        runCatching {
            guildRepository.updateGold(-goldMaterial.requiredAmount)

            val slimeInventoryItem = inventoryRepository.findItem(1L, 3L)
            if (slimeInventoryItem != null) {
                val newAmount = slimeInventoryItem.amount - slimeMaterial.requiredAmount
                if (newAmount > 0) {
                    inventoryRepository.updateInventory(slimeInventoryItem.copy(amount = newAmount))
                } else {
                    inventoryRepository.deleteInventory(slimeInventoryItem)
                }
            }

            val targetEquip = customizedEquipRepository.getEquipById(equipId)
                ?: throw IllegalStateException("장비가 존재하지 않습니다.")

            val newEquip = targetEquip.copy(
                reinforcement = targetEquip.reinforcement + 1
            )
            customizedEquipRepository.insertEquip(newEquip)

            loadAllEquipments()
            loadEquipDetail(equipId)

            dismissReinforceDialog()
            _eventFlow.emit(
                ItemEvent.ReinforceResult(
                    true,
                    "강화에 성공했습니다! (+${newEquip.reinforcement})"
                )
            )

        }.onFailure { e ->
            _eventFlow.emit(ItemEvent.ReinforceResult(false, "강화 중 오류 발생: ${e.message}"))
        }
    }

    fun dismissReinforceDialog() {
        _reinforceState.value = null
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

        val filteredList = combinedList.filter {
            filter.matches(it.category)
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
                    .getOrNull()
                ?: throw NoSuchElementException("베이스 장비 정보를 찾을 수 없습니다.")

            EquipDetail(
                id = customized.id,
                name = base.name,
                reinforcement = customized.reinforcement,
                ownerName = if (customized.ownerId == 0L) "미착용" else "플레이어",
                ownerId = customized.ownerId,
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

    private suspend fun createEquipDetail(customized: CustomizedEquip): EquipDetail? {
        val base = _allBaseEquips.value.find { it.id == customized.equipId }
            ?: baseEquipRepository.getBaseEquipById(customized.equipId).getOrNull()
            ?: return null

        return EquipDetail(
            id = customized.id,
            name = base.name,
            reinforcement = customized.reinforcement,
            ownerName = if (customized.ownerId == 0L) "미착용" else "플레이어",
            ownerId = customized.ownerId,
            description = base.description,
            statDescription = "${base.increaseStat.name} +${base.increaseAmount} (+${customized.getIncreasedAmount()})",
            category = base.category,
            sellPrice = customized.modifiedSellPrice
        )
    }
}

data class ReinforceUiState(
    val targetEquip: EquipDetail,
    val materials: List<ReinforceMaterial>,
    val isAffordable: Boolean,
    val imageUrl: Int
)