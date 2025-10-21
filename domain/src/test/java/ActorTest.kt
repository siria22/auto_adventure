package com.example.domain

import com.example.domain.model.feature.inventory.CustomizedEquip
import com.example.domain.model.feature.mockActor
import com.example.domain.model.feature.types.ActorAttributeType
import com.example.domain.model.feature.types.EquipCategory
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class ActorTest {

    @Test
    fun `test actor creation transfers base values correctly`() {
        val actor = mockActor

        assertEquals(0L, actor.id)
        assertEquals("Sairo", actor.info.name)
        assertEquals(120L, actor.currentHp)
        assertEquals(100L, actor.currentMp)
        assertEquals(10L, actor.adventureCount)
        assertEquals(173L, actor.killCount)
    }

    @Test
    fun `test equipArmor updates equips and attributes`() {
        val initialActor = mockActor
        val newWeapon = CustomizedEquip(
            id = 100L,
            ownerId = initialActor.id,
            equipId = 1L,
            category = EquipCategory.WEAPON,
            customizedName = "A mighty test sword",
            requiredStrength = 0,
            requiredAgility = 0,
            requiredIntelligence = 0,
            requiredLuck = 0,
            increaseStat = ActorAttributeType.ATTACK,
            increaseAmount = 5,
            reinforcement = 2,
            modifiedSellPrice = 0,
            modifiedWeight = 0,
            rank = "NORMAL"
        )

        val equippedActor = initialActor.equipArmor(newWeapon)

        assertEquals(newWeapon, equippedActor.equips.weapon)

        assertNotEquals(initialActor.attribute.attack, equippedActor.attribute.attack)

        val expectedAttack = initialActor.attribute.attack + newWeapon.getIncreasedAmount()
        assertEquals(expectedAttack, equippedActor.attribute.attack)
    }

    @Test
    fun `test attack function applies damage to target`() {
        val actor = mockActor
        println("공격 전 체력: ${actor.currentHp}")

        val result = actor.attack(actor)
        println("공격 후 체력: ${result.currentHp}")

        assertTrue(result.currentHp < actor.currentHp)
    }

    @Test
    fun `test attack can miss based on hit chance`() {
        val actor = mockActor
        var hitCount = 0
        val totalAttempts = 1000

        repeat(totalAttempts) {
            val beforeHp = actor.currentHp
            val result = actor.attack(actor)

            if (result.currentHp < beforeHp) {
                hitCount++
                println("공격 성공! 체력: $beforeHp -> ${result.currentHp}")
            } else {
                println("공격 빗나감! (체력: $beforeHp)")
            }
        }

        val hitRate = (hitCount.toDouble() / totalAttempts * 100).toInt()
        println("\n공격 성공률: $hitRate% ($hitCount/$totalAttempts)")

        assertTrue(hitCount > 0 && hitCount < totalAttempts)
    }

    @Test
    fun `test takeDamage reduces health correctly`() {
        val actor = mockActor
        val initialHp = actor.currentHp
        val damage = 30L
        println("공격 전 체력: ${actor.currentHp}")

        val result = actor.takeDamage(damage)
        println("공격 후 체력: ${result.currentHp}")

        assertEquals(initialHp - damage, result.currentHp)
    }

    @Test
    fun `test takeDamage does not reduce health below zero`() {
        val actor = mockActor
        val damage = actor.currentHp + 100
        println("공격 전 체력: ${actor.currentHp}")

        val result = actor.takeDamage(damage)
        println("공격 후 체력: ${result.currentHp}")

        assertEquals(0L, result.currentHp)
    }
}