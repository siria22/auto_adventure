package com.example.presentation.screen.item

import androidx.lifecycle.viewModelScope
import com.example.domain.model.feature.inventory.BaseEquip
import com.example.domain.model.feature.inventory.CustomizedEquip
import com.example.domain.model.feature.types.EquipFilterType
import com.example.domain.usecase.feature.equip.GetAllEquipsUseCase
import com.example.domain.usecase.feature.equip.GetEquipDetailUseCase
import com.example.domain.usecase.feature.equip.GetReinforceInfoUseCase
import com.example.domain.usecase.feature.equip.ReinforceEquipUseCase
import com.example.domain.usecase.feature.equip.SellEquipUseCase
import com.example.presentation.R
import com.example.presentation.component.ui.molecule.item.ReinforceMaterial
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
    private val getAllEquipsUseCase: GetAllEquipsUseCase,
    private val getEquipDetailUseCase: GetEquipDetailUseCase,
    private val sellEquipUseCase: SellEquipUseCase,
    private val getReinforceInfoUseCase: GetReinforceInfoUseCase,
    private val reinforceEquipUseCase: ReinforceEquipUseCase
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
                launch {
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
                launch {
                    sellEquip(intent.equipId)
                }
            }

            is ItemIntent.OnRequestReinforce -> {
                launch {
                    prepareReinforce(intent.equipId)
                }
            }

            is ItemIntent.OnExecuteReinforce -> {
                launch {
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
        launch {
            loadAllEquipments()
        }
    }

    private suspend fun loadAllEquipments() {
        getAllEquipsUseCase()
            .onSuccess { data ->
                _allBaseEquips.value = data.baseEquips
                _allCustomizedEquips.value = data.customizedEquips
            }
            .onFailure { ex ->
                _eventFlow.emit(
                    ItemEvent.DataFetch.Error(
                        userMessage = "전체 장비 정보를 불러오는데 실패했습니다.",
                        exceptionMessage = ex.message
                    )
                )
            }
    }

    private suspend fun prepareReinforce(equipId: Long) {
        getReinforceInfoUseCase(equipId)
            .onSuccess { info ->
                val materials = listOf(
                    ReinforceMaterial(
                        name = "슬라임 점액",
                        iconResId = R.drawable.ic_potion_red,
                        currentAmount = info.currentMaterialAmount,
                        requiredAmount = info.requiredMaterialAmount
                    ),
                    ReinforceMaterial(
                        name = "골드",
                        iconResId = R.drawable.ic_potion_red,
                        currentAmount = info.currentGold,
                        requiredAmount = info.requiredGold
                    )
                )

                val detail = createEquipDetail(info.targetEquip) ?: return

                _reinforceState.value = ReinforceUiState(
                    targetEquip = detail,
                    materials = materials,
                    isAffordable = info.isAffordable,
                    imageUrl = R.drawable.ic_ironsword
                )
            }.onFailure {

            }
    }


    private suspend fun executeReinforce(equipId: Long) {
        reinforceEquipUseCase(equipId)
            .onSuccess { newEquip ->
                loadAllEquipments()
                loadEquipDetail(equipId)
                dismissReinforceDialog()
                _eventFlow.emit(ItemEvent.ReinforceResult(true, "강화에 성공했습니다! (+${newEquip.reinforcement})"))
            }
            .onFailure { e ->
                _eventFlow.emit(ItemEvent.ReinforceResult(false, "강화 실패: ${e.message}"))
            }
    }

    fun dismissReinforceDialog() {
        _reinforceState.value = null
    }

    private suspend fun sellEquip(customizedId: Long) {
        sellEquipUseCase(customizedId)
            .onSuccess {
                loadAllEquipments()
            }
            .onFailure { ex ->
                _eventFlow.emit(
                    ItemEvent.DataFetch.Error(
                        userMessage = "장비 판매 중 오류가 발생했습니다.",
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
                    rank = customized.rank,
                    isEquipped = customized.ownerId != 0L
                )
            }
        }

        val filteredList = combinedList.filter {
            filter.matches(it.category)
        }

        return when (sort) {
            EquipSortType.DEFAULT -> filteredList
            EquipSortType.REINFORCEMENT -> filteredList.sortedByDescending { it.reinforcement }
            EquipSortType.RANK -> filteredList.sortedBy { it.rank } //TODO 등급 정렬 기준은 나중에 구체화 필요
        }
    }

    private suspend fun loadEquipDetail(equipId: Long) {
        _selectedEquipDetail.value = null
        getEquipDetailUseCase(equipId)
            .onSuccess { (customized, base) ->
                val detail = EquipDetail(
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
                _selectedEquipDetail.value = detail
            }
            .onFailure { ex ->
                _eventFlow.emit(
                    ItemEvent.DataFetch.Error(
                        userMessage = "장비 상세 정보를 불러오는데 실패했습니다.",
                        exceptionMessage = ex.message
                    )
                )
            }
    }

    private fun createEquipDetail(customized: CustomizedEquip): EquipDetail? {
        val base = _allBaseEquips.value.find { it.id == customized.equipId }
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