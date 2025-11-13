package com.example.domain

import com.example.domain.model.feature.inventory.InventoryItem
import com.example.domain.model.feature.inventory.Item
import com.example.domain.model.feature.party.Party
import com.example.domain.model.feature.party.PartyWithMembers
import com.example.domain.model.feature.types.ItemCategory
import com.example.domain.model.feature.types.ItemEffectType
import com.example.domain.repository.feature.inventory.InventoryRepository
import com.example.domain.repository.feature.inventory.ItemRepository
import com.example.domain.repository.feature.party.PartyRepository
import com.example.domain.usecase.inventory.AddOrUpdateItemUseCase
import com.example.domain.usecase.party.PartyUseCase
import kotlinx.coroutines.runBlocking
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test

class AddOrUpdateItemUseCaseTest {

    // --- Fake Implementations ---

    class FakeInventoryRepository : InventoryRepository {
        private val inventory = mutableListOf<InventoryItem>()
        override suspend fun getInventoryByPartyId(partyId: Long): List<InventoryItem> =
            inventory.filter { it.partyId == partyId }

        override suspend fun getAllInventories(): List<InventoryItem> = inventory
        override suspend fun findItem(partyId: Long, itemId: Long): InventoryItem? =
            inventory.find { it.partyId == partyId && it.itemId == itemId }

        override suspend fun insertInventory(inventoryItem: InventoryItem) {
            inventory.add(inventoryItem)
        }

        override suspend fun updateInventory(inventoryItem: InventoryItem) {
            val index =
                inventory.indexOfFirst { it.partyId == inventoryItem.partyId && it.itemId == inventoryItem.itemId }
            if (index != -1) inventory[index] = inventoryItem
        }

        override suspend fun deleteInventory(inventoryItem: InventoryItem) {
            inventory.removeIf { it.partyId == inventoryItem.partyId && it.itemId == inventoryItem.itemId }
        }
    }

    class FakeItemRepository(private val items: List<Item>) : ItemRepository {
        override suspend fun getItemList(): List<Item> = items
        override suspend fun getItemById(id: Long): Result<Item> {
            return items.find { it.id == id }?.let { Result.success(it) } ?: Result.failure(
                NoSuchElementException("Item not found")
            )
        }
    }

    // PartyUseCase가 의존하는 PartyRepository의 Fake 구현
    class FakePartyRepository : PartyRepository {
        override suspend fun getPartyWithMembers(partyId: Long): PartyWithMembers? = null
        override suspend fun insertParty(party: Party): Result<Unit> = Result.success(Unit)
        override suspend fun getAllParties(): List<Party> = emptyList()
        override suspend fun getPartyById(id: Long): Party? = null
        override suspend fun getPartyByName(name: String): Party? = null // <-- 이 줄이 추가되었습니다.
    }

    // 이제 partyRepository를 제대로 넘겨줄 수 있음
    class FakePartyUseCase(private val maxWeight: Double) :
        PartyUseCase(partyRepository = FakePartyRepository()) {
        override suspend fun getMaxInventoryWeight(partyId: Long): Double {
            return maxWeight
        }
    }

    // --- Test Setup ---

    private lateinit var addOrUpdateItemUseCase: AddOrUpdateItemUseCase
    private lateinit var fakeInventoryRepository: FakeInventoryRepository
    private lateinit var fakeItemRepository: FakeItemRepository
    private lateinit var fakePartyUseCase: FakePartyUseCase

    private val heavyPotion = Item(id = 1L, name = "Heavy Potion", shortDescription = "", fullDescription = "", category = ItemCategory.HEALING, weight = 10.0, maxStackSize = 10, obtainMethods = emptyList(), isSellable = false, buyPrice = 0, sellPrice = 0, isUsable = true, effectType = ItemEffectType.HEAL, effectAmount = 100L)
    private val lightPotion = Item(id = 2L, name = "Light Potion", shortDescription = "", fullDescription = "", category = ItemCategory.HEALING, weight = 1.0, maxStackSize = 20, obtainMethods = emptyList(), isSellable = false, buyPrice = 0, sellPrice = 0, isUsable = true, effectType = ItemEffectType.HEAL, effectAmount = 20L)

    @Before
    fun setUp() {
        fakeInventoryRepository = FakeInventoryRepository()
        fakeItemRepository = FakeItemRepository(listOf(heavyPotion, lightPotion)) // 리스트에 포션 추가
        fakePartyUseCase = FakePartyUseCase(maxWeight = 100.0)

        addOrUpdateItemUseCase = AddOrUpdateItemUseCase(
            fakeInventoryRepository,
            fakeItemRepository,
            fakePartyUseCase
        )
    }

    // --- Tests ---

    @Test
    fun `invoke should add new item successfully`() = runBlocking {
        // Given
        println("--- 신규 아이템 추가 성공 테스트 시작 ---")
        val partyId = 1L
        val itemToAdd = lightPotion
        val amountToAdd = 5L
        println("테스트 준비: 인벤토리가 비어있음")

        // When
        println("실행: 무게 ${itemToAdd.weight}짜리 아이템 ${amountToAdd}개 추가 시도...")
        val result = addOrUpdateItemUseCase(partyId, itemToAdd.id, amountToAdd)

        // Then
        println("결과: $result")
        assertTrue(result.isSuccess)
        val inventoryItem = fakeInventoryRepository.findItem(partyId, itemToAdd.id)
        assertNotNull(inventoryItem)
        assertEquals(amountToAdd, inventoryItem?.amount)
        println("검증 완료: 인벤토리에 가벼운 포션 ${inventoryItem?.amount}개가 정상적으로 추가됨")
        println("-----------------------------------\n")
    }

    @Test
    fun `invoke should update existing item amount successfully`() = runBlocking {
        // Given
        println("--- 기존 아이템 수량 변경 성공 테스트 시작 ---")
        val partyId = 1L
        val itemToAdd = lightPotion
        val initialAmount = 10L
        val amountToAdd = 5L
        fakeInventoryRepository.insertInventory(InventoryItem(partyId, itemToAdd.id, initialAmount))
        println("테스트 준비: 인벤토리에 가벼운 포션 ${initialAmount}개 있음")

        // When
        println("실행: 가벼운 포션 ${amountToAdd}개 추가 시도...")
        val result = addOrUpdateItemUseCase(partyId, itemToAdd.id, amountToAdd)

        // Then
        println("결과: $result")
        assertTrue(result.isSuccess)
        val inventoryItem = fakeInventoryRepository.findItem(partyId, itemToAdd.id)
        assertNotNull(inventoryItem)
        assertEquals(initialAmount + amountToAdd, inventoryItem?.amount)
        println("검증 완료: 가벼운 포션의 총 수량이 ${inventoryItem?.amount}개로 정상적으로 변경됨")
        println("---------------------------------------\n")
    }

    @Test
    fun `invoke should fail when adding item exceeds max weight`() = runBlocking {
        // Given
        println("--- 무게 초과 실패 테스트 시작 ---")
        val partyId = 1L
        val itemToAdd = heavyPotion
        val amountToAdd = 1L

        fakeInventoryRepository.insertInventory(InventoryItem(partyId, heavyPotion.id, 9)) // 90.0
        fakeInventoryRepository.insertInventory(InventoryItem(partyId, lightPotion.id, 5)) // 5.0
        println("테스트 준비: 현재 총 무게 95.0 (최대 무게: 100.0)")

        // When
        println("실행: 무게 ${itemToAdd.weight}짜리 아이템 ${amountToAdd}개 추가 시도...")
        val result = addOrUpdateItemUseCase(partyId, itemToAdd.id, amountToAdd)

        // Then
        println("결과: $result")
        assertTrue(result.isFailure)
        assertEquals("인벤토리 무게 한도를 초과합니다.", result.exceptionOrNull()?.message)
        println("검증 완료: 무게 한도 초과로 정상적으로 실패함")
        println("-----------------------------\n")
    }

    @Test
    fun `invoke should add items partially when exceeding max weight`() = runBlocking {
        // Given
        println("--- 무게 한도 내 부분 추가 테스트 시작 ---")
        val partyId = 1L
        val itemToAdd = lightPotion // 무게 1.0
        val initialAmount = 97L
        val amountToAdd = 5L
        fakeInventoryRepository.insertInventory(InventoryItem(partyId, itemToAdd.id, initialAmount))
        println("테스트 준비: 현재 무게 97.0 (가벼운 포션 ${initialAmount}개), 최대 무게 100.0")

        // When
        println("실행: 무게 ${itemToAdd.weight}짜리 아이템 ${amountToAdd}개 추가 시도...")
        val result = addOrUpdateItemUseCase(partyId, itemToAdd.id, amountToAdd)

        // Then
        println("결과: $result")
        assertTrue(result.isSuccess)
        val inventoryItem = fakeInventoryRepository.findItem(partyId, itemToAdd.id)
        assertNotNull(inventoryItem)
        assertEquals(100L, inventoryItem?.amount) // 97 + 3 = 100
        println("검증 완료: 가벼운 포션 3개만 추가되어 총 수량이 ${inventoryItem?.amount}개가 됨")
        println("---------------------------------------")
    }
}