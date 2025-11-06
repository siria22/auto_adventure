package com.example.domain.model.feature.item.effect

import com.example.domain.model.feature.actor.actor.Actor

interface ItemEffect {
    /**
     * 대상 [Actor]에게 효과를 적용하고,
     * 효과가 적용된 새로운 [Actor] 객체를 반환합니다.
     *
     * @param target 효과를 적용할 대상 액터
     * @return 효과가 적용된 새로운 액터
     */
    fun apply(target: Actor): Actor
}