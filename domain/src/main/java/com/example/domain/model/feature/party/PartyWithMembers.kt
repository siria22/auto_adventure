package com.example.domain.model.feature.party

import com.example.domain.model.feature.actor.actor.Actor

/**
 * Party 데이터와 해당 파티에 속한 멤버(Actor) 목록을 함께 가지는 데이터 클래스.
 * 주로 Repository에서 데이터를 가져올 때 사용.
 */
data class PartyWithMembers(
    val party: Party,
    val members: List<Actor>
)