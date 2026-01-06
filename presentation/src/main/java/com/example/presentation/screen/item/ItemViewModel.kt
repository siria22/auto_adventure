package com.example.presentation.screen.item

import androidx.lifecycle.viewModelScope
import com.example.domain.model.feature.inventory.InventoryItem
import com.example.domain.model.feature.inventory.Item
import com.example.domain.model.feature.types.ItemCategory
import com.example.domain.repository.feature.guild.GuildRepository
import com.example.domain.repository.feature.inventory.InventoryRepository
import com.example.domain.repository.feature.inventory.ItemRepository
import com.example.domain.usecase.inventory.GetInventoryUseCase
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
class ItemViewModel @Inject constructor(
    private val getInventoryUseCase: GetInventoryUseCase,
    private val itemRepository: ItemRepository,
    private val inventoryRepository: InventoryRepository,
    private val guildRepository: GuildRepository
) : BaseViewModel() {

    private val _state = MutableStateFlow<ItemState>(ItemState.Init)
    val state: StateFlow<ItemState> = _state

    private val _eventFlow = MutableSharedFlow<ItemEvent>()
    val eventFlow: SharedFlow<ItemEvent> = _eventFlow

    private val _inventoryItems = MutableStateFlow<List<InventoryItem>>(emptyList())
    val inventoryItems: StateFlow<List<InventoryItem>> = _inventoryItems

    private val _selectedFilter = MutableStateFlow(ItemFilterType.ALL)
    val selectedFilter: StateFlow<ItemFilterType> = _selectedFilter

    private val _selectedSort = MutableStateFlow(ItemSortType.DEFAULT)
    val selectedSort: StateFlow<ItemSortType> = _selectedSort

    private val _allItems = MutableStateFlow<List<Item>>(emptyList())

    val displayedItems: StateFlow<List<InventoryItem>> = combine(
        _inventoryItems, _allItems, _selectedFilter, _selectedSort
    ) { inventoryItems, allItems, filter, sort ->
        applyFilterAndSort(inventoryItems, allItems, filter, sort)
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )

    // TODO: totalWeight, maxWeight StateFlow 추가 필요

    private val _selectedItemDetail = MutableStateFlow<Item?>(null)
    val selectedItemDetail: StateFlow<Item?> = _selectedItemDetail

    fun onIntent(intent: ItemIntent) {
        when (intent) {
            is ItemIntent.OnItemClick -> {
                viewModelScope.launch {
                    loadItemDetail(intent.itemId)
                }
            }

            is ItemIntent.OnFilterChange -> {
                _selectedFilter.value = intent.filterType
            }

            is ItemIntent.OnSortChange -> {
                _selectedSort.value = intent.sortType
            }

            is ItemIntent.OnSellItem -> {
                viewModelScope.launch {
                    sellItem(intent.itemId, intent.quantity)
                }
            }

            else -> {
                // Do nothing
            }
        }
    }

    init {
        viewModelScope.launch {
            loadAllItems()
            loadInventory()
        }
    }

    private suspend fun sellItem(itemId: Long, quantity: Int) {
        val currentInventory = _inventoryItems.value.find { it.itemId == itemId } ?: return
        val itemInfo = _allItems.value.find { it.id == itemId } ?: return

        val gainGold = itemInfo.sellPrice * quantity

        val remainingAmount = currentInventory.amount - quantity

        runCatching {
            if (remainingAmount <= 0) {
                inventoryRepository.deleteInventory(currentInventory)
            } else {
                inventoryRepository.updateInventory(currentInventory.copy(amount = remainingAmount))
            }
            guildRepository.updateGold(gainGold)
        }.onSuccess {
            loadInventory()
        }.onFailure { ex ->
            emitErrorMessage("아이템 판매 중 오류가 발생했습니다.", ex)
        }
    }

    private suspend fun loadInventory() {
        _state.value = ItemState.OnProgress
        runCatching {
            getInventoryUseCase(partyId = 1L) // 임시 partyId
        }.onSuccess { result ->
            _inventoryItems.value = result.getOrThrow()
        }.onFailure { ex ->
            emitErrorMessage("인벤토리 정보를 불러오는데 실패했습니다.", ex)
        }
        _state.value = ItemState.Init
    }

    private suspend fun loadItemDetail(itemId: Long) {
        _selectedItemDetail.value = null
        runCatching {
            itemRepository.getItemById(itemId)
        }.onSuccess { result ->
            _selectedItemDetail.value = result.getOrNull()
        }.onFailure { ex ->
            emitErrorMessage("아이템 상세 정보를 불러오는데 실패했습니다.", ex)
        }
    }

    private suspend fun emitErrorMessage(userMessage: String, ex: Throwable) {
        _eventFlow.emit(
            ItemEvent.DataFetch.Error(
                userMessage = userMessage,
                exceptionMessage = ex.message
            )
        )
    }

    private fun applyFilterAndSort(
        inventoryItems: List<InventoryItem>,
        allItems: List<Item>,
        filter: ItemFilterType,
        sort: ItemSortType
    ): List<InventoryItem> {
        if (allItems.isEmpty()) {
            return inventoryItems
        }

        val itemDetailsMap = allItems.associateBy { it.id }
        val combinedList = inventoryItems.mapNotNull { inventoryItem ->
            itemDetailsMap[inventoryItem.itemId]?.let { itemDetail ->
                object {
                    val inventoryItem = inventoryItem
                    val detail = itemDetail
                }
            }
        }

        val filteredList = when (filter) {
            ItemFilterType.ALL -> combinedList
            ItemFilterType.CONSUMABLE -> combinedList.filter {
                it.detail.category == ItemCategory.HEALING ||
                        it.detail.category == ItemCategory.SCROLL
            }

            ItemFilterType.BUFF -> combinedList.filter {
                it.detail.category == ItemCategory.BUFF
            }

            ItemFilterType.INGREDIENT -> combinedList.filter {
                it.detail.category == ItemCategory.INGREDIENT
            }

            ItemFilterType.ETC -> combinedList.filter {
                it.detail.category == ItemCategory.ETC
            }
        }

        val sortedList = when (sort) {
            ItemSortType.DEFAULT -> filteredList
            ItemSortType.NAME -> filteredList.sortedBy { it.detail.name }
            ItemSortType.PRICE -> filteredList.sortedByDescending { it.detail.sellPrice }
            ItemSortType.QUANTITY -> filteredList.sortedByDescending { it.inventoryItem.amount }
        }
        return sortedList.map { it.inventoryItem }
    }

    private suspend fun loadAllItems() {
        runCatching {
            itemRepository.getItemList()
        }.onSuccess { items ->
            _allItems.value = items
        }.onFailure { ex ->
            emitErrorMessage("전체 아이템 정보를 불러오는데 실패했습니다.", ex)
        }
    }
}