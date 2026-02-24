package com.example.domain

import com.example.domain.model.feature.party.Party
import com.example.domain.model.feature.party.PartyMember
import com.example.domain.repository.feature.party.PartyMemberRepository
import com.example.domain.repository.feature.party.PartyRepository
import com.example.domain.usecase.feature.party.AddMemberToPartyUseCase
import com.example.domain.usecase.feature.party.CreatePartyUseCase
import com.example.domain.usecase.feature.party.DisbandPartyUseCase
import com.example.domain.usecase.feature.party.RemoveMemberFromPartyUseCase
import com.example.domain.usecase.feature.party.ReplacePartyMemberUseCase
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import com.example.domain.model.feature.types.PartyPosition

/**
 * 파티 시스템 전체 시나리오 테스트
 * (생성 -> 멤버추가 -> 교체 -> 제외/리더승계 -> 해체)
 */
class PartyUseCaseTest {

    // --- 1. Fake Repository (메모리 DB 역할) ---

    class FakePartyRepository : PartyRepository {
        val parties = mutableListOf<Party>()
        private var nextId = 1L

        override suspend fun insertParty(party: Party): Result<Unit> {
            val newParty = party.copy(id = nextId++)
            parties.add(newParty)
            return Result.success(Unit)
        }

        override suspend fun getAllParties(): List<Party> = parties
        override suspend fun getPartyById(id: Long): Party? = parties.find { it.id == id }
        override suspend fun getPartyByName(name: String): Party? = parties.find { it.name == name }
        override suspend fun deleteParty(party: Party) {
            parties.removeIf { it.id == party.id }
        }

        override suspend fun getPartyWithMembers(partyId: Long): Nothing? = null
    }

    class FakePartyMemberRepository : PartyMemberRepository {
        val members = mutableListOf<PartyMember>()

        override suspend fun insertMember(member: PartyMember) {
            members.add(member)
        }

        override suspend fun getMembersByPartyId(partyId: Long): List<PartyMember> =
            members.filter { it.partyId == partyId }

        override suspend fun getPartyByCharacterId(characterId: Long): PartyMember? =
            members.find { it.characterId == characterId }

        override suspend fun updatePartyLeader(actorId: Long, isPartyLeader: Boolean) {
            val idx = members.indexOfFirst { it.characterId == actorId }
            if (idx != -1) members[idx] = members[idx].copy(isPartyLeader = isPartyLeader)
        }

        override suspend fun deleteMember(member: PartyMember) {
            members.removeIf { it.characterId == member.characterId && it.partyId == member.partyId }
        }

        // 리더 승계 로직 (슬롯 번호가 가장 작은 멤버)
        override suspend fun getNextLeader(partyId: Long): PartyMember? =
            members.filter { it.partyId == partyId }.minByOrNull { it.slotIndex }

        override suspend fun getOccupiedSlots(partyId: Long): List<Int> =
            members.filter { it.partyId == partyId }.map { it.slotIndex }
    }

    // --- 2. UseCases & Setup ---

    private lateinit var fakePartyRepo: FakePartyRepository
    private lateinit var fakeMemberRepo: FakePartyMemberRepository

    private lateinit var createParty: CreatePartyUseCase
    private lateinit var disbandParty: DisbandPartyUseCase
    private lateinit var addMember: AddMemberToPartyUseCase
    private lateinit var removeMember: RemoveMemberFromPartyUseCase
    private lateinit var replaceMember: ReplacePartyMemberUseCase

    @Before
    fun setUp() {
        fakePartyRepo = FakePartyRepository()
        fakeMemberRepo = FakePartyMemberRepository()

        createParty = CreatePartyUseCase(fakePartyRepo)
        disbandParty = DisbandPartyUseCase(fakePartyRepo)
        addMember = AddMemberToPartyUseCase(fakeMemberRepo)
        removeMember = RemoveMemberFromPartyUseCase(fakeMemberRepo)
        replaceMember = ReplacePartyMemberUseCase(fakeMemberRepo)
    }

    // --- 3. Helper Function (상태 출력) ---

    private fun printPartyStatus(partyId: Long, stepTitle: String) = runBlocking {
        println("\n========================================")
        println("STEP: $stepTitle")
        println("========================================")

        val party = fakePartyRepo.getPartyById(partyId)
        if (party == null) {
            println("▶ 파티 상태: [해체됨/존재하지 않음]")
            return@runBlocking
        }

        println("▶ 파티명: ${party.name} (ID: ${party.id})")

        val members = fakeMemberRepo.getMembersByPartyId(partyId).sortedBy { it.slotIndex }
        if (members.isEmpty()) {
            println("▶ 멤버: 없음")
        } else {
            println("▶ 멤버 목록 (${members.size}/4):")
            members.forEach { m ->
                val role = if (m.isPartyLeader) "[★리더]" else "[멤버]"
                println("   - Slot ${m.slotIndex} | $role | 캐릭터 ID: ${m.characterId}")
            }
        }
        println("----------------------------------------\n")
    }

    // --- 4. Main Scenario Test ---

    @Test
    fun `partyFullScenarioTest`() = runBlocking {
        println(">>> 파티 시스템 전체 시나리오 테스트 시작 <<<")

        // 1. 파티 생성
        val createResult = createParty()
        assertTrue(createResult.isSuccess)
        val partyId = 1L
        printPartyStatus(partyId, "1. 파티 생성")

        // 2. 멤버 추가 (4명 풀 파티)
        // 100(리더), 101, 102, 103 순서로 추가됨
        addMember(partyId, 100L)
        addMember(partyId, 101L)
        addMember(partyId, 102L)
        addMember(partyId, 103L)
        printPartyStatus(partyId, "2. 멤버 4명 추가 (풀 파티)")

        val leader = fakeMemberRepo.getMembersByPartyId(partyId).find { it.isPartyLeader }
        assertEquals(100L, leader?.characterId)

        // 3. 파티원 교체 (Replace)
        // 상황: Slot 1에 있는 101번(멤버)을 -> 200번(새 멤버)으로 교체
        // 기대: 101번 삭제, 200번 추가 (Slot 1 유지, 리더 아님)
        val replaceResult = replaceMember(partyId, 101L, 200L)
        assertTrue("교체 성공해야 함", replaceResult.isSuccess)
        printPartyStatus(partyId, "3. 파티원 교체 (101 -> 200)")

        val newMember = fakeMemberRepo.getMembersByPartyId(partyId).find { it.characterId == 200L }
        assertNotNull(newMember)
        assertEquals("교체된 멤버는 기존 멤버의 Slot 1을 승계해야 함", 1, newMember!!.slotIndex)
        assertFalse("교체된 멤버는 리더가 아니어야 함", newMember.isPartyLeader)

        // 4. 멤버 제외 및 리더 승계 (Remove)
        // 상황: 100번(리더, Slot 0)을 파티에서 제외
        // 기대: 100번 삭제, 남은 멤버 중 가장 앞 슬롯(Slot 1의 200번)이 리더가 되어야 함
        val removeResult = removeMember(partyId, 100L)
        assertTrue("제외 성공해야 함", removeResult.isSuccess)
        printPartyStatus(partyId, "4. 리더(100) 제외 및 리더 승계")

        val nextLeader = fakeMemberRepo.getMembersByPartyId(partyId).find { it.isPartyLeader }
        assertNotNull("새로운 리더가 선출되어야 함", nextLeader)
        assertEquals("Slot 1에 있는 200번이 리더가 되어야 함", 200L, nextLeader!!.characterId)

        // 5. 파티 해체 (Disband)
        val disbandResult = disbandParty(partyId)
        assertTrue("해체 성공해야 함", disbandResult.isSuccess)
        printPartyStatus(partyId, "5. 파티 해체")

        val deletedParty = fakePartyRepo.getPartyById(partyId)
        assertNull("파티 데이터가 삭제되어야 함", deletedParty)

        println(">>> 테스트 검증 완료 <<<")
    }
}