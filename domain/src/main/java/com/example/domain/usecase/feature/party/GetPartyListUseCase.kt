package com.example.domain.usecase.feature.party

import com.example.domain.model.feature.actor.actor.someExpToLevelConverterFunction
import com.example.domain.model.feature.party.PartyDetail
import com.example.domain.model.feature.party.PartyMemberDetail
import com.example.domain.repository.feature.actor.ActorRepository
import com.example.domain.repository.feature.actor.JobRepository
import com.example.domain.repository.feature.party.PartyMemberRepository
import com.example.domain.repository.feature.party.PartyRepository
import javax.inject.Inject

class GetPartyListUseCase @Inject constructor(
    private val partyRepository: PartyRepository,
    private val partyMemberRepository: PartyMemberRepository,
    private val actorRepository: ActorRepository,
    private val jobRepository: JobRepository
) {
    suspend operator fun invoke(): Result<List<PartyDetail>> {
        val parties = partyRepository.getAllParties()
        val jobs = jobRepository.getJobList().associateBy { it.jobId }

        val partyDetails = parties.map { party ->
            val members = partyMemberRepository.getMembersByPartyId(party.id)

            val memberDetails = members.mapNotNull { member ->
                val baseActor =
                    actorRepository.getActorById(member.characterId) ?: return@mapNotNull null
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

            PartyDetail(party, memberDetails)
        }

        return Result.success(partyDetails)
    }
}