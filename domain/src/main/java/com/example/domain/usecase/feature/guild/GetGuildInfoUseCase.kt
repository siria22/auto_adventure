package com.example.domain.usecase.feature.guild

import com.example.domain.model.feature.guild.GuildInfoData
import com.example.domain.repository.feature.actor.ActorRepository
import com.example.domain.repository.feature.guild.GuildRepository
import com.example.domain.scripts.guild.GuildStats
import com.example.domain.utils.zip
import javax.inject.Inject

class GetGuildInfoUseCase @Inject constructor(
    private val guildRepository: GuildRepository,
    private val actorRepository: ActorRepository
) {
    suspend operator fun invoke(): Result<GuildInfoData> = runCatching {

        val (guildName, guildRank, guildTotalExp, guildMemberCount) = zip(
            { guildRepository.getGuildName() },
            { guildRepository.getGuildRank() },
            { guildRepository.getGuildTotalExp() },
            { actorRepository.getRecruitedActorCount() }
        )
        val guildLevel = GuildStats.getGuildLevelFromTotalExp(guildTotalExp)
        val expForNextLevel = GuildStats.getExpForNextLevel(guildTotalExp)

        GuildInfoData(
            guildName = guildName,
            guildRank = guildRank,
            guildTotalExp = guildTotalExp,
            guildLevel = guildLevel,
            expForNextLevel = expForNextLevel,
            guildMemberCount = guildMemberCount
        )
    }

}
