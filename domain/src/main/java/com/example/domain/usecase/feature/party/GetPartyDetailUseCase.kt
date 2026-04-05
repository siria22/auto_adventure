package com.example.domain.usecase.feature.party

import com.example.domain.model.feature.actor.actor.someExpToLevelConverterFunction
import com.example.domain.model.feature.party.PartyDetail
import com.example.domain.model.feature.party.PartyMemberDetail
import com.example.domain.repository.feature.actor.ActorRepository
import com.example.domain.repository.feature.actor.JobRepository
import com.example.domain.repository.feature.party.PartyMemberRepository
import com.example.domain.repository.feature.party.PartyRepository
import javax.inject.Inject

class GetPartyDetailUseCase @Inject constructor(
    private val partyRepository: PartyRepository,
    private val partyMemberRepository: PartyMemberRepository,
    private val actorRepository: ActorRepository,
    private val jobRepository: JobRepository
) {
    suspend operator fun invoke(partyId: Long): Result<PartyDetail> {
        val party = partyRepository.getPartyById(partyId)
            ?: return Result.failure(IllegalArgumentException("파티를 찾을 수 없습니다."))

        val members = partyMemberRepository.getMembersByPartyId(partyId)
        val jobs = jobRepository.getJobList().associateBy { it.jobId }

        val memberDetails = members.mapNotNull { member ->
            val baseActor = actorRepository.getActorById(member.characterId) ?: return@mapNotNull null
            val jobName = jobs[baseActor.jobId]?.nameKor ?: "알수없음"
            val level = someExpToLevelConverterFunction(baseActor.totalExp)

            PartyMemberDetail(
                characterId = member.characterId,
                name = baseActor.name,
                jobName = jobName,
                level = level,
                currentHp = baseActor.currentHp,
                maxHp = baseActor.maxHp,
                currentMp = baseActor.currentMp,
                maxMp = baseActor.maxMp,
                position = member.position,
                slotIndex = member.slotIndex,
                isLeader = member.isPartyLeader
            )
        }.sortedBy { it.slotIndex }

        return Result.success(PartyDetail(party, memberDetails))
    }
}