package com.example.domain.model.feature.item.effect

import com.example.domain.model.feature.actor.actor.Actor

class HealEffect(private val amount: Long) : ItemEffect {
    init {
        require(amount > 0)
    }

    override fun apply(target: Actor): Actor {
        if (!target.isAlive) return target
        return target.changeHp(amount)
    }
}