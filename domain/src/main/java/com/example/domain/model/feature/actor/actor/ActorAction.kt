package com.example.domain.model.feature.actor.actor

import com.example.domain.model.feature.inventory.Item

interface ActorAction {
    fun execute(actor: Actor, target: Actor): Actor

    var priority: Int  // 우선순위
}

class AttackAction : ActorAction {
    override var priority: Int = 1

    override fun execute(actor: Actor, target: Actor): Actor {
        return actor.attack(target)
    }

    override fun toString(): String = "공격"
}

class UseSkillAction(private val skill: Skill) : ActorAction {
    override var priority: Int = 2

    override fun execute(actor: Actor, target: Actor): Actor {
        // TODO: 스킬 사용 로직 구현
        println("${actor.info.name}이(가) ${skill.name} 스킬을 사용했습니다!")
        return target
    }

    override fun toString(): String = "스킬 사용: ${skill.name}"
}

class UseItemAction(private val item: Item) : ActorAction {
    override var priority: Int = 3

    override fun execute(actor: Actor, target: Actor): Actor {
        return actor.useItem(item)
    }

    override fun toString(): String = "아이템 사용: ${item.name}"
}

data class Skill(val name: String)
