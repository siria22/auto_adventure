package com.example.domain.usecase.feature.party

import com.example.domain.model.feature.party.PartyMember
import com.example.domain.repository.feature.party.PartyMemberRepository
import javax.inject.Inject

class AddPartyMemberUseCase @Inject constructor(
    private val partyMemberRepository: PartyMemberRepository
) {
    suspend operator fun invoke(member: PartyMember): Result<Unit> = runCatching {
        partyMemberRepository.insertMember(member)
    }
}