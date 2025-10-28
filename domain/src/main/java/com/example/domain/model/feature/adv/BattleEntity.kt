package com.example.domain.model.feature.adv

import com.example.domain.model.feature.actor.actor.Actor
import com.example.domain.model.feature.actor.actor.ActorAction
import com.example.domain.model.feature.actor.actor.ActorInfo
import com.example.domain.model.feature.actor.actor.ActorSkills
import com.example.domain.model.feature.party.BasePartyMember
import com.example.domain.model.feature.types.BattleEntityActionFlag
import com.example.domain.model.feature.types.PartyPosition
import com.example.domain.model.feature.types.PartyType
import com.example.domain.model.feature.types.StatusEffectType

data class BattleEntity(
    val actorId: Long,
    val info: ActorInfo,
    val partyType: PartyType,

    var maxHp: Long,     // 최대 체력
    var baseAttack: Long,    // 공격
    var baseSpeed: Long,     // 속도
    var baseEvasion: Long,   // 회피
    var baseMagic: Long,     // 마력
    var maxMp: Long,     // 최대 MP
    var baseFortune: Long,   // 행운
    var baseAccuracy: Long,  // 명중
    var baseDef: Long,        // 방어
    var baseCriticalDamage: Double, // 치명타 배율

    var currentHp: Long,
    var currentAttack: Long,
    var currentSpeed: Long,
    var currentEvasion: Long,
    var currentMagic: Long,
    var currentMp: Long,
    var currentFortune: Long,
    var currentAccuracy: Long,
    var currentDef: Long,
    var currentCriticalDamage: Double,

    var selectedSkills: List<ActorSkills>,
    var actorActions: List<ActorAction>,
    var statusEffects: List<StatusEffectType>,

    var isPartyLeader: Boolean,
    var currentTarget: BattleEntity?,
    var position: PartyPosition,
    var isAlive: Boolean,
) {
    fun create(actor: Actor, partyRole: BasePartyMember, partyType: PartyType): BattleEntity {
        return BattleEntity(
            actorId = actor.id,
            info = actor.info,
            partyType = partyType,

            maxHp = actor.attribute.maxHp,
            baseAttack = actor.attribute.attack,
            baseSpeed = actor.attribute.speed,
            baseEvasion = actor.attribute.evasion,
            baseMagic = actor.attribute.magic,
            maxMp = actor.attribute.maxMp,
            baseFortune = actor.attribute.fortune,
            baseAccuracy = actor.attribute.accuracy,
            baseDef = actor.attribute.def,
            baseCriticalDamage = actor.attribute.criticalDamage,

            currentHp = actor.currentHp,
            currentAttack = actor.attribute.attack,
            currentSpeed = actor.attribute.speed,
            currentEvasion = actor.attribute.evasion,
            currentMagic = actor.attribute.magic,
            currentMp = actor.currentMp,
            currentFortune = actor.attribute.fortune,
            currentAccuracy = actor.attribute.accuracy,
            currentDef = actor.attribute.def,
            currentCriticalDamage = actor.attribute.criticalDamage,

            selectedSkills = actor.skills.filter { it.isEquipped },
            actorActions = actor.actions.filter { it.isActivated },
            statusEffects = emptyList(),

            isPartyLeader = partyRole.isPartyLeader,
            currentTarget = null,
            position = partyRole.position,
            isAlive = true
        )
    }

    /**
     * 현재 상태를 String으로 반환
     */
    fun showInfo(): String {
        return this.info.name /* TODO : actor info */
    }

    fun scout(entity: List<BattleEntity>) {
        val sortedEntity = entity.sortedByDescending {
            val positionFactor = if (it.position == PartyPosition.FRONT) 100 else 50
            val statusFactor =
                if (it.statusEffects.contains(StatusEffectType.PROVOCATION)) 200 else 0
            0 + positionFactor + statusFactor
        }
        this.currentTarget = sortedEntity.first()
    }

    fun performPartyAction(): BattleEntityActionFlag {
        return BattleEntityActionFlag.NONE
    }

    fun performActorAction(): BattleEntityActionFlag {
        return BattleEntityActionFlag.NONE
    }

    fun performAttack(): BattleEntityActionFlag {
        return BattleEntityActionFlag.NONE
    }
}