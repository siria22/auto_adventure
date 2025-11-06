package com.example.domain.model.feature.item.effect

import com.example.domain.model.feature.actor.actor.Actor

class ManaRestoreEffect(private val amount: Long) : ItemEffect {
    init {
        require(amount > 0)
    }

    override fun apply(target: Actor): Actor {
        return target.changeMp(amount)
    }
}