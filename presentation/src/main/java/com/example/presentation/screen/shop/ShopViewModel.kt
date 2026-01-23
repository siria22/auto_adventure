package com.example.presentation.screen.shop

import androidx.lifecycle.viewModelScope
import com.example.domain.model.feature.inventory.BaseEquip
import com.example.domain.model.feature.inventory.CustomizedEquip
import com.example.domain.model.feature.inventory.Item
import com.example.domain.model.feature.types.EquipFilterType
import com.example.domain.model.feature.types.ItemFilterType
import com.example.domain.repository.feature.guild.GuildRepository
import com.example.domain.repository.feature.inventory.BaseEquipRepository
import com.example.domain.repository.feature.inventory.CustomizedEquipRepository
import com.example.domain.repository.feature.inventory.ItemRepository
import com.example.domain.usecase.inventory.AddOrUpdateItemUseCase
import com.example.domain.usecase.feature.user.GetPlayerMoneyUseCase
import com.example.presentation.utils.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ShopViewModel @Inject constructor(
    private val itemRepository: ItemRepository,
    private val baseEquipRepository: BaseEquipRepository,
    private val customizedEquipRepository: CustomizedEquipRepository,
    private val guildRepository: GuildRepository,
    private val addOrUpdateItemUseCase: AddOrUpdateItemUseCase,
    private val getPlayerMoneyUseCase: GetPlayerMoneyUseCase
) : BaseViewModel() {

    private val _shopState = MutableStateFlow<ShopState>(ShopState.Init)
    val shopState: StateFlow<ShopState> = _shopState

    private val _eventFlow = MutableSharedFlow<ShopEvent>()
    val eventFlow: SharedFlow<ShopEvent> = _eventFlow

    private val _originalItems = MutableStateFlow<List<Item>>(emptyList())
    private val _originalEquips = MutableStateFlow<List<BaseEquip>>(emptyList())

    private val _itemFilter = MutableStateFlow(ItemFilterType.ALL)
    val itemFilter: StateFlow<ItemFilterType> = _itemFilter

    private val _itemSort = MutableStateFlow(ShopSortType.DEFAULT)
    val itemSort: StateFlow<ShopSortType> = _itemSort

    private val _equipFilter = MutableStateFlow(EquipFilterType.ALL)
    val equipFilter: StateFlow<EquipFilterType> = _equipFilter

    private val _equipSort = MutableStateFlow(ShopSortType.DEFAULT)
    val equipSort: StateFlow<ShopSortType> = _equipSort

    private val _userGold = MutableStateFlow(0L)
    val userGold: StateFlow<Long> = _userGold

    private val _selectedItemToBuy = MutableStateFlow<Item?>(null)
    val selectedItemToBuy: StateFlow<Item?> = _selectedItemToBuy

    private val _selectedEquipToBuy = MutableStateFlow<BaseEquip?>(null)
    val selectedEquipToBuy: StateFlow<BaseEquip?> = _selectedEquipToBuy

    val items: StateFlow<List<Item>> = combine(_originalItems, _itemFilter, _itemSort) { items, filter, sort ->
        var result = items
        if (filter != ItemFilterType.ALL) {
            result = result.filter { filter.matches(it.category) }
        }
        applySort(result, sort) { it.name to it.buyPrice }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())


    val baseEquips: StateFlow<List<BaseEquip>> = combine(_originalEquips, _equipFilter, _equipSort) { equips, filter, sort ->
        var result = equips
        if (filter != EquipFilterType.ALL) {
            result = result.filter { filter.matches(it.category) }
        }
        applySort(result, sort) { it.name to it.buyPrice }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    private fun <T> applySort(
        list: List<T>,
        sort: ShopSortType,
        selector: (T) -> Pair<String, Long>
    ): List<T> {
        return when(sort) {
            ShopSortType.DEFAULT -> list
            ShopSortType.NAME -> list.sortedBy { selector(it).first }
            ShopSortType.PRICE_LOW -> list.sortedBy { selector(it).second }
            ShopSortType.PRICE_HIGH -> list.sortedByDescending { selector(it).second }
        }
    }

    init {
        loadShopData()
        observeGold()
    }

    private fun observeGold() {
        viewModelScope.launch {
            getPlayerMoneyUseCase().collectLatest { gold ->
                _userGold.value = gold
            }
        }
    }

    private fun loadShopData() {
        viewModelScope.launch {
            _shopState.value = ShopState.Loading
            runCatching {
                val itemList = itemRepository.getItemList().filter { it.buyPrice > 0 }
                val equipList = baseEquipRepository.getBaseEquipList().filter { it.buyPrice > 0 }

                _originalItems.value = itemList
                _originalEquips.value = equipList
            }.onSuccess {
                _shopState.value = ShopState.Success
            }.onFailure { e ->
                _eventFlow.emit(ShopEvent.Error(e.message ?: "상점 데이터를 불러오는데 실패했습니다."))
            }
        }
    }

    fun onIntent(intent: ShopIntent) {
        when (intent) {
            is ShopIntent.OnBuyItemClick -> {
                val item = items.value.find { it.id == intent.itemId }
                _selectedItemToBuy.value = item
            }
            is ShopIntent.OnBuyEquipClick -> {
                val equip = baseEquips.value.find { it.id == intent.equipId }
                _selectedEquipToBuy.value = equip
            }
            is ShopIntent.OnConfirmBuyItem -> {
                buyItem(intent.itemId, intent.quantity)
                _selectedItemToBuy.value = null
            }
            is ShopIntent.OnConfirmBuyEquip -> {
                buyEquip(intent.equipId)
                _selectedEquipToBuy.value = null
            }
            is ShopIntent.Refresh -> loadShopData()

            is ShopIntent.OnItemFilterChange -> _itemFilter.value = intent.filter
            is ShopIntent.OnItemSortChange -> _itemSort.value = intent.sort
            is ShopIntent.OnEquipFilterChange -> _equipFilter.value = intent.filter
            is ShopIntent.OnEquipSortChange -> _equipSort.value = intent.sort
        }
    }

    private fun buyItem(itemId: Long, quantity: Int) {
        viewModelScope.launch {
            val item = items.value.find { it.id == itemId } ?: return@launch
            val totalPrice = item.buyPrice * quantity

            if (_userGold.value < totalPrice) {
                _eventFlow.emit(ShopEvent.ShowToast("골드가 부족합니다."))
                return@launch
            }

            // TODO: 실제 파티 ID나 유저 ID 사용
            val result = addOrUpdateItemUseCase(1L, itemId, quantity.toLong())

            result.onSuccess {
                guildRepository.updateGold(-totalPrice)
            }.onFailure { e ->
                _eventFlow.emit(ShopEvent.ShowToast(e.message ?: "구매 실패"))
            }
        }
    }

    private fun buyEquip(equipId: Long) {
        viewModelScope.launch {
            val baseEquip = baseEquips.value.find { it.id == equipId } ?: return@launch
            val price = baseEquip.buyPrice

            if (_userGold.value < price) {
                _eventFlow.emit(ShopEvent.ShowToast("골드가 부족합니다."))
                return@launch
            }

            val newEquip = CustomizedEquip(
                id = 0L, // Auto-increment
                ownerId = 1L, // TODO: 실제 오너 ID
                equipId = baseEquip.id,
                category = baseEquip.category,
                customizedName = baseEquip.name,
                requiredStrength = baseEquip.baseRequiredStrength,
                requiredAgility = baseEquip.baseRequiredAgility,
                requiredIntelligence = baseEquip.baseRequiredIntelligence,
                requiredLuck = baseEquip.baseRequiredLuck,
                increaseStat = baseEquip.increaseStat,
                increaseAmount = baseEquip.increaseAmount,
                reinforcement = 0L,
                modifiedSellPrice = baseEquip.buyPrice / 4,
                modifiedWeight = baseEquip.weight.toLong(),
                rank = "Normal"
            )

            runCatching {
                customizedEquipRepository.insertEquip(newEquip)
                guildRepository.updateGold(-price)
            }.onSuccess {

            }.onFailure {
                _eventFlow.emit(ShopEvent.ShowToast("장비 구매 실패"))
            }
        }
    }
}