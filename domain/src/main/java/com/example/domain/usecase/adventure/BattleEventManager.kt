package com.example.domain.usecase.adventure

import com.example.domain.model.feature.adv.BattleEntity
import com.example.domain.model.feature.adv.Party
import com.example.domain.model.feature.types.BattleEntityActionFlag
import com.example.domain.model.feature.types.LogCategory
import com.example.domain.model.feature.types.PartyType
import com.example.domain.utils.logging.Logger
import javax.inject.Inject

class BattleEventManager @Inject constructor(
    private val logger: Logger
) {
    fun runBattle(party: Party, enemies: List<BattleEntity>, logOpenGap: Int) {

        logger.appendLog(
            "${party.partyName}이 적을 마주쳤다!",
            description = "적 정보:\n${enemies}",
            category = LogCategory.BATTLE,
            revealAfter = logOpenGap
        )

        while (true) {
            val alivePartyMembers = party.partyMembers.filter { it.isAlive }
            val aliveEnemies = enemies.filter { it.isAlive }
            val entities = (alivePartyMembers + aliveEnemies).sortedByDescending { it.currentSpeed }

            entities.forEach { entity ->
                // Set target
                if (entity.partyType == PartyType.ALLEY) entity.scout(aliveEnemies)
                else entity.scout(alivePartyMembers)

                // Choice Actions
                if (entity.performPartyAction() == BattleEntityActionFlag.PERFORMED) return@forEach

                if (entity.performActorAction() == BattleEntityActionFlag.PERFORMED) return@forEach

                entity.performAttack()

            }

        }
    }
}