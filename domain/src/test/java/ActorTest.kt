package com.example.domain

import com.example.domain.model.feature.inventory.InventoryItem
import com.example.domain.model.feature.inventory.Item
import com.example.domain.model.feature.mockActor
import com.example.domain.model.feature.types.ItemCategory
import com.example.domain.model.feature.types.ItemEffectType
import com.example.domain.repository.feature.inventory.InventoryRepository
import com.example.domain.usecase.inventory.AddOrUpdateItemUseCase
import com.example.domain.usecase.inventory.UseItemUseCase
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class ActorTest {
    class FakeInventoryRepository : InventoryRepository {
        private val inventory = mutableListOf<InventoryItem>()

        override suspend fun insertInventory(inventoryItem: InventoryItem) {
            inventory.add(inventoryItem)
        }

        override suspend fun updateInventory(inventoryItem: InventoryItem) {
            val index =
                inventory.indexOfFirst { it.itemId == inventoryItem.itemId && it.partyId == inventoryItem.partyId }
            if (index != -1) {
                inventory[index] = inventoryItem
            }
        }

        override suspend fun deleteInventory(inventoryItem: InventoryItem) {
            inventory.removeIf { it.itemId == inventoryItem.itemId && it.partyId == inventoryItem.partyId }
        }

        override suspend fun getInventoryByPartyId(partyId: Long): List<InventoryItem> {
            return inventory.filter { it.partyId == partyId }
        }

        override suspend fun getAllInventories(): List<InventoryItem> {
            return inventory
        }

        override suspend fun findItem(partyId: Long, itemId: Long): InventoryItem? {
            return inventory.find { it.partyId == partyId && it.itemId == itemId }
        }
    }

    private lateinit var useItemUseCase: UseItemUseCase
    private lateinit var addOrUpdateItemUseCase: AddOrUpdateItemUseCase
    private lateinit var fakeInventoryRepository: FakeInventoryRepository

    @Before
    fun setUp() {
        fakeInventoryRepository = FakeInventoryRepository()
        addOrUpdateItemUseCase = AddOrUpdateItemUseCase(fakeInventoryRepository)
        useItemUseCase = UseItemUseCase(fakeInventoryRepository, addOrUpdateItemUseCase)
    }

    @Test
    fun `마나포션 사용 시 MP가 정상적으로 회복된다`() = runBlocking {
        val actor = mockActor.changeMp(5 - mockActor.currentMp)
        println("\n[MP 정상 회복 테스트]")
        println("회복 전 MP: ${actor.currentMp} / ${actor.attribute.maxMp}")

        val manaPotion = Item(
            id = 3,
            name = "마나 포션",
            effectType = ItemEffectType.MANA_RESTORE,
            effectAmount = 10, // 회복량 10
            shortDescription = "",
            fullDescription = "",
            category = ItemCategory.HEALING,
            weight = 0.0,
            maxStackSize = 99,
            obtainMethods = emptyList(),
            isSellable = false,
            buyPrice = 0,
            sellPrice = 0,
            isUsable = true
        )
        addOrUpdateItemUseCase(actor.id, manaPotion.id, 1).getOrThrow()

        val result = useItemUseCase(actor, manaPotion)

        val updatedActor = result.getOrThrow()
        println("회복 후 MP: ${updatedActor.currentMp} / ${updatedActor.attribute.maxMp}")
        assertEquals(actor.currentMp + manaPotion.effectAmount, updatedActor.currentMp)
    }

    @Test
    fun `마나포션 사용 시 MP가 최대치를 초과하여 회복되지 않는다`() = runBlocking {
        val actor = mockActor.changeMp(15 - mockActor.currentMp)
        println("\n[MP 최대치 초과 테스트]")
        println("회복 전 MP: ${actor.currentMp} / ${actor.attribute.maxMp}")

        val superManaPotion = Item(
            id = 4,
            name = "슈퍼 마나 포션",
            effectType = ItemEffectType.MANA_RESTORE,
            effectAmount = 10, // 15 + 10 = 25 > 17
            shortDescription = "",
            fullDescription = "",
            category = ItemCategory.HEALING,
            weight = 0.0,
            maxStackSize = 99,
            obtainMethods = emptyList(),
            isSellable = false,
            buyPrice = 0,
            sellPrice = 0,
            isUsable = true
        )
        addOrUpdateItemUseCase(actor.id, superManaPotion.id, 1).getOrThrow()

        val result = useItemUseCase(actor, superManaPotion)

        val updatedActor = result.getOrThrow()
        println("회복 후 MP: ${updatedActor.currentMp} / ${updatedActor.attribute.maxMp}")
        assertEquals(actor.attribute.maxMp, updatedActor.currentMp)
    }

    @Test
    fun `죽은 대상에게 일반 포션 사용 시 HP가 회복되지 않는다`() = runBlocking {
        val deadActor = mockActor.takeDamage(mockActor.currentHp)
        println("\n[죽은 대상 회복 테스트]")
        println("사용 전 HP: ${deadActor.currentHp}")

        val potion = Item(
            id = 1, name = "포션", effectType = ItemEffectType.HEAL, effectAmount = 20,
            shortDescription = "", fullDescription = "", category = ItemCategory.HEALING,
            weight = 0.0, maxStackSize = 99, obtainMethods = emptyList(),
            isSellable = false, buyPrice = 0, sellPrice = 0, isUsable = true
        )
        addOrUpdateItemUseCase(deadActor.id, potion.id, 1).getOrThrow()

        val result = useItemUseCase(deadActor, potion)

        val updatedActor = result.getOrThrow()
        println("사용 후 HP: ${updatedActor.currentHp}")
        assertEquals(0, updatedActor.currentHp)
    }

    @Test
    fun `죽은 대상에게 부활 포션 사용 시 정상적으로 부활한다`() = runBlocking {
        val deadActor = mockActor.takeDamage(mockActor.currentHp)
        val resurrectionAmount = (deadActor.attribute.maxHp * 0.5).toLong() // 최대 HP의 50%만큼 부활
        println("\n[부활 테스트]")
        println("사용 전 HP: ${deadActor.currentHp}")

        val resurrectionPotion = Item(
            id = 5, name = "부활 포션", effectType = ItemEffectType.RESURRECTION, effectAmount = resurrectionAmount,
            shortDescription = "", fullDescription = "", category = ItemCategory.HEALING,
            weight = 0.0, maxStackSize = 99, obtainMethods = emptyList(),
            isSellable = false, buyPrice = 0, sellPrice = 0, isUsable = true
        )
        addOrUpdateItemUseCase(deadActor.id, resurrectionPotion.id, 1).getOrThrow()

        val result = useItemUseCase(deadActor, resurrectionPotion)

        val updatedActor = result.getOrThrow()
        println("사용 후 HP: ${updatedActor.currentHp}")
        assertEquals(resurrectionAmount, updatedActor.currentHp)
    }
}