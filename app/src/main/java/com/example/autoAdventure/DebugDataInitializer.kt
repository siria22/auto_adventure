package com.example.autoAdventure

import com.example.domain.model.feature.actor.base.BaseActor
import com.example.domain.model.feature.party.Party
import com.example.domain.repository.feature.actor.ActorRepository
import com.example.domain.repository.feature.party.PartyRepository
import javax.inject.Inject

// 테스트용 코드
class DebugDataInitializer @Inject constructor(
    private val actorRepository: ActorRepository,
    private val partyRepository: PartyRepository
) {
    suspend fun init() {
        if (actorRepository.getAllActors().isEmpty()) {
            val jobs = listOf(
                Triple("전사 아로", 1L, "전사"),
                Triple("궁수 아롱", 2L, "궁수"),
                Triple("법사 아로롱", 3L, "마법사"),
                Triple("도적 아록", 4L, "도적")
            )

            jobs.forEach { (name, jobId, category) ->
                val actor = BaseActor(
                    id = 0,
                    name = name,
                    totalExp = 0,
                    maxHp = 100,
                    currentHp = 100,
                    maxMp = 50,
                    currentMp = 50,
                    strength = 10,
                    agility = 10,
                    luck = 10,
                    intelligence = 10,
                    remainSkillPoint = 0,
                    remainStatPoint = 0,
                    personalityId = 1,
                    actorCategory = category,
                    jobId = jobId,
                    race = "Human",
                    elementType = "None",
                    rank = "D",
                    adventureCount = 0,
                    killCount = 0,
                    recoveryEndTime = null,
                    recruitedAt = java.util.Date()
                )
                actorRepository.insertActor(actor)
            }
        }
        if (partyRepository.getAllParties().isEmpty()) {
            val party = Party(
                id = 0,
                name = "제 1파티 - 모험의 시작",
                isOnAdventure = false,
                adventureStartTime = null
            )
            partyRepository.insertParty(party)
        }
    }
}