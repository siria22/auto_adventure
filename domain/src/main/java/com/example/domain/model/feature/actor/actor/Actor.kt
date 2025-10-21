package com.example.domain.model.feature.actor.actor

import com.example.domain.model.feature.actor.base.BaseActor
import com.example.domain.model.feature.actor.base.Job
import com.example.domain.model.feature.actor.base.Personality
import com.example.domain.model.feature.inventory.CustomizedEquip
import com.example.domain.model.feature.types.ActorCategory
import com.example.domain.model.feature.types.EquipCategory

@ConsistentCopyVisibility
data class Actor private constructor(
    val id: Long,
    val currentHp: Long,
    val currentMp: Long,
    val remainSkillPoint: Long,
    val remainStatPoint: Long,
    val info: ActorInfo,
    val stats: ActorBaseStatus,
    val attribute: ActorAttributes,
    val equips: ActorEquips,
    val skills: List<ActorSkills>,
    val actions: List<ActorAction>,
    val adventureCount: Long,
    val killCount: Long,
    val currentTarget: Actor? = null
) {
    /**
     * Equips a new piece of armor to the actor.
     *
     * @param newEquip The new armor to equip.
     * @return The actor with the new armor equipped.
     */
    fun equipArmor(newEquip: CustomizedEquip): Actor {
        val newEquips = when (newEquip.category) {
            EquipCategory.WEAPON -> this.equips.copy(weapon = newEquip)
            EquipCategory.SIDEARM -> this.equips.copy(sidearm = newEquip)
            EquipCategory.ARMOR -> this.equips.copy(armor = newEquip)
            EquipCategory.ACCESSORY -> this.equips.copy(accessory = newEquip)
            EquipCategory.GLOVES -> this.equips.copy(gloves = newEquip)
            EquipCategory.SHOES -> this.equips.copy(shoes = newEquip)
        }
        val newAttributes = this.updateAttributes(
            stats = this.stats,
            equips = newEquips
        )
        return this.copy(equips = newEquips, attribute = newAttributes)
    }

    /**
     * Update the base status of the actor and return the new actor with the updated status.
     *
     * @param newStatus The new base status of the actor.
     * @return The actor with the updated base status.
     */
    fun updateBaseStatus(newStatus: ActorBaseStatus): Actor {
        val newAttributes = this.updateAttributes(
            stats = newStatus, equips = equips
        )
        return this.copy(stats = newStatus, attribute = newAttributes)
    }

    private fun updateAttributes(
        stats: ActorBaseStatus,
        equips: ActorEquips
    ): ActorAttributes {
        return calculateAttributes(info, stats, equips)
    }

    fun attack(target: Actor): Actor {
        val hitChance = (this.attribute.accuracy - target.attribute.evasion).coerceIn(0, 100)
        if ((1..100).random() > hitChance) {
            return target
        }

        var attackDamage = this.attribute.attack

        val criticalChance = (this.attribute.fortune * 0.5).toInt().coerceIn(0, 100)
        val isCritical = (1..100).random() <= criticalChance
        if (isCritical) {
            attackDamage = (attackDamage * this.attribute.criticalDamage).toLong()
        }

        return target.takeDamage(attackDamage)
    }

    fun takeDamage(incomingAttackDamage: Long): Actor {
        val finalDamage = (incomingAttackDamage - this.attribute.def).coerceAtLeast(1)
        val newHp = (this.currentHp - finalDamage).coerceAtLeast(0)
        return this.copy(currentHp = newHp)
    }

    fun useItem(/*item: Item*/): Actor {
        println("아이템을 사용했습니다!")

        return this
    }

    companion object {
        fun create(
            baseActor: BaseActor,
            job: Job,
            personality: Personality,
            actorEquips: ActorEquips,
            actorSkills: List<ActorSkills>,
            actorActions: List<ActorAction>
        ): Actor {
            val info = ActorInfo(
                name = baseActor.name,
                race = baseActor.race,
                job = job,
                actorCategory = ActorCategory.valueOf(baseActor.actorCategory),
                elementType = baseActor.elementType,
                personality = personality,
                rank = baseActor.rank,
                level = someExpToLevelConverterFunction(baseActor.totalExp),
                totalExp = baseActor.totalExp,
            )

            val stats = ActorBaseStatus(
                strength = baseActor.strength,
                agility = baseActor.agility,
                luck = baseActor.luck,
                intelligence = baseActor.intelligence,
            )

            val attribute = calculateAttributes(info, stats, actorEquips)

            return Actor(
                id = baseActor.id,
                currentHp = baseActor.currentHp,
                currentMp = baseActor.currentMp,
                remainSkillPoint = baseActor.remainSkillPoint,
                remainStatPoint = baseActor.remainStatPoint,
                info = info,
                stats = stats,
                attribute = attribute,
                equips = actorEquips,
                skills = actorSkills,
                actions = actorActions,
                adventureCount = baseActor.adventureCount,
                killCount = baseActor.killCount,
                currentTarget = null
            )
        }

        private fun calculateAttributes(
            info: ActorInfo,
            stats: ActorBaseStatus,
            equips: ActorEquips
        ): ActorAttributes {
            /* 간단한 계산식
            * maxHp    : 10 + (STR + 성격 보정치)*3 + 직업 보정치
            * attack   : 1 + 무기 공격력 + (STR + 성격 보정치)*2 + 직업 보정치
            * speed    : 5 + (AGL + 성격 보정치)*1 - 방어구 개수 + 직업 보정치
            * evasion  : 5 + (AGL + 성격 보정치)*1 + 직업 보정치
            * magic    : 1 + (INT + 성격 보정치)*2 + 직업 보정치
            * maxMp    : 5 + (INT + 성격 보정치)*3 + 직업 보정치
            * fortune  : 0 + (LUC + 성격 보정치)*1 + 직업 보정치
            * accuracy : 0 + (LUC + 성격 보정치)*2 + 직업 보정치
            */
            val job = info.job
            val modifiedStats = stats.applyPersonalityModifier(info.personality)

            return ActorAttributes(
                maxHp = (10 + modifiedStats.strength * 2) + job.getHpBonus(),
                attack = (1 + (equips.weapon?.getIncreasedAmount() ?: 0)
                        + modifiedStats.strength * 2) + job.getAttackBonus(),
                speed = (5 + modifiedStats.agility * 1 - equips.getEquippedArmorCount()),
                evasion = (5 + modifiedStats.agility * 1),
                magic = (1 + modifiedStats.intelligence * 2),
                maxMp = (5 + modifiedStats.intelligence * 3),
                fortune = (0 + modifiedStats.luck * 1),
                accuracy = (100 + modifiedStats.luck * 1),
                def = (0 + equips.getTotalDefenseAmount()),
                criticalDamage = 1.5
            )
        }
    }
}