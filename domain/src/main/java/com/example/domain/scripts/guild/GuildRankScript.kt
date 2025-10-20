package com.example.domain.scripts.guild

import com.example.domain.scripts.getLevelFromTotalExp

/* EXP로부터 모든 것을 추출해내야해!!!!
* 현재 레벨 검사
* 레벨 구간에 따른 랭크 파악
* 다음 레벨까지 필요한 경험치
*
* 다음으로 승급 가능? (Level이 안에 있고, 골드 충분한지?)
* 다음 랭크로 승급하기 위한 골드 양
* 다음 랭크로 승급하기 위한 레벨
* */

data object GuildStats {

    fun getGuildLevelFromTotalExp(totalExp: Long): Long {
        return getLevelFromTotalExp(totalExp)
    }

    fun getGuildRankFromTotalExp(totalExp: Long): GuildRank {
        val currentLevel = getGuildLevelFromTotalExp(totalExp)
        return when (currentLevel) {
            in 0L..9L -> GuildRank.E0
            in 10L..19L -> GuildRank.E1
            in 20L..29L -> GuildRank.E2
            in 30L..39L -> GuildRank.E3
            in 40L..49L -> GuildRank.E4
            in 50L..59L -> GuildRank.D0
            in 60L..69L -> GuildRank.D1
            in 70L..79L -> GuildRank.D2
            in 80L..89L -> GuildRank.D3
            in 90L..99L -> GuildRank.D4
            in 100L..109L -> GuildRank.C0
            in 110L..119L -> GuildRank.C1
            in 120L..129L -> GuildRank.C2
            in 130L..139L -> GuildRank.C3
            in 140L..149L -> GuildRank.C4
            in 150L..159L -> GuildRank.B0
            in 160L..169L -> GuildRank.B1
            in 170L..179L -> GuildRank.B2
            in 180L..189L -> GuildRank.B3
            in 190L..199L -> GuildRank.B4
            in 200L..209L -> GuildRank.A0
            in 210L..219L -> GuildRank.A1
            in 220L..229L -> GuildRank.A2
            in 230L..239L -> GuildRank.A3
            in 240L..249L -> GuildRank.A4
            in 250L..259L -> GuildRank.S0
            in 260L..269L -> GuildRank.S1
            in 270L..279L -> GuildRank.S2
            in 280L..289L -> GuildRank.S3
            in 290L..299L -> GuildRank.S4
            in 300L..309L -> GuildRank.L0
            in 310L..319L -> GuildRank.L1
            in 320L..329L -> GuildRank.L2
            in 330L..339L -> GuildRank.L3
            else -> GuildRank.L4
        }
    }

    fun getExpForNextLevel(totalExp: Long): Long {
        val currentLevel = getGuildLevelFromTotalExp(totalExp)
        return getExpForNextLevel(currentLevel)
    }

    fun getNextGuildRank(currentRank: GuildRank): GuildRank = when (currentRank) {
        GuildRank.E0 -> GuildRank.E1
        GuildRank.E1 -> GuildRank.E2
        GuildRank.E2 -> GuildRank.E3
        GuildRank.E3 -> GuildRank.E4
        GuildRank.E4 -> GuildRank.D0
        GuildRank.D0 -> GuildRank.D1
        GuildRank.D1 -> GuildRank.D2
        GuildRank.D2 -> GuildRank.D3
        GuildRank.D3 -> GuildRank.D4
        GuildRank.D4 -> GuildRank.C0
        GuildRank.C0 -> GuildRank.C1
        GuildRank.C1 -> GuildRank.C2
        GuildRank.C2 -> GuildRank.C3
        GuildRank.C3 -> GuildRank.C4
        GuildRank.C4 -> GuildRank.B0
        GuildRank.B0 -> GuildRank.B1
        GuildRank.B1 -> GuildRank.B2
        GuildRank.B2 -> GuildRank.B3
        GuildRank.B3 -> GuildRank.B4
        GuildRank.B4 -> GuildRank.A0
        GuildRank.A0 -> GuildRank.A1
        GuildRank.A1 -> GuildRank.A2
        GuildRank.A2 -> GuildRank.A3
        GuildRank.A3 -> GuildRank.A4
        GuildRank.A4 -> GuildRank.S0
        GuildRank.S0 -> GuildRank.S1
        GuildRank.S1 -> GuildRank.S2
        GuildRank.S2 -> GuildRank.S3
        GuildRank.S3 -> GuildRank.S4
        GuildRank.S4 -> GuildRank.L0
        GuildRank.L0 -> GuildRank.L1
        GuildRank.L1 -> GuildRank.L2
        GuildRank.L2 -> GuildRank.L3
        GuildRank.L3 -> GuildRank.L4
        GuildRank.L4 -> GuildRank.L4
    }

    fun isGuildRankUpAvailable(
        currentRank: GuildRank,
        totalExp: Long,
        money: Long
    ): Result<Boolean> = runCatching {
        if (getGuildRankFromTotalExp(totalExp) == currentRank) throw Exception("경험치가 부족해요!")
        val to = getNextGuildRank(currentRank)
        if (money < getRequiredGoldForNextRank(to)) throw Exception("골드가 부족해요!")
        true
    }.onFailure { ex ->
        Result.failure<Boolean>(ex)
    }

    fun getRequiredGoldForNextRank(targetRank: GuildRank): Long {
        return 1000L * targetRank.ordinal
        /* TODO : 길드 랭크 업 시 필요한 골드  */
    }

}

