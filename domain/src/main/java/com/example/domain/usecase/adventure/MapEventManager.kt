package com.example.domain.usecase.adventure

import com.example.domain.model.feature.types.ActionStatusFlag
import com.example.domain.model.feature.common.BaseAdventureEvents
import com.example.domain.model.feature.common.BattleEvent
import com.example.domain.model.feature.common.CommonEvent
import com.example.domain.model.feature.common.EmptyEvent
import com.example.domain.model.feature.types.MapEventFlag
import com.example.domain.model.feature.dungeon.DungeonEventTable
import com.example.domain.model.feature.dungeon.DungeonFloorWeight
import com.example.domain.model.feature.dungeon.DungeonMapAttribute
import com.example.domain.model.feature.adv.Party
import com.example.domain.utils.logging.Logger
import javax.inject.Inject
import kotlin.random.Random

/**
 * 모험 이벤트 생성
 * - 특정 Map의 발생 가능한 전체 이벤트(일반, 전투, 이벤트, 탐사 이벤트(미구현)) 가져오기
 * - 필수 등장 이벤트 (강제 이벤트) 지정
 * - 확률에 따라 이벤트 균등 배포
 *  - 일반 : 무작위 로그 지정
 *  - 전투 : 등장 몬스터 및 loot table 생성
 *  - 이벤트 : 해당 맵의 등장 가능한 이벤트 중 무작위 선택
 *  -
 */
class MapEventManager @Inject constructor(
    private val logger: Logger
) {
    /* TODO : Mocking objects <- UseCase로 가져오는 로직 추가 */
    val mockDungeonEventTables = listOf(
        DungeonEventTable(
            dungeonId = 1,
            floor = 1,
            eventId = listOf(1, 2, 3)
        ),
        DungeonEventTable(
            dungeonId = 1,
            floor = 2,
            eventId = listOf(1, 2, 3)
        ),
        DungeonEventTable(
            dungeonId = 1,
            floor = 3,
            eventId = listOf(1, 2, 3)
        ),
    )

    val mockDungeonMapAttributes = listOf(
        DungeonMapAttribute(
            dungeonId = 1,
            floor = 1,
            noneRate = 2,
            battleRate = 5,
            eventRate = 5
        ),
        DungeonMapAttribute(
            dungeonId = 1,
            floor = 2,
            noneRate = 2,
            battleRate = 7,
            eventRate = 5
        ),
        DungeonMapAttribute(
            dungeonId = 1,
            floor = 3,
            noneRate = 2,
            battleRate = 10,
            eventRate = 4
        ),
    )

    val mockDungeonFloorWeight = listOf(
        DungeonFloorWeight(
            dungeonId = 1,
            floor = 1,
            room = 1,
            monsterMaxWeight = 30,
            lootMinWeight = 5,
            lootMaxWeight = 10
        ),
        DungeonFloorWeight(
            dungeonId = 1,
            floor = 1,
            room = 2,
            monsterMaxWeight = 30,
            lootMinWeight = 5,
            lootMaxWeight = 10
        ),
        DungeonFloorWeight(
            dungeonId = 1,
            floor = 1,
            room = 3,
            monsterMaxWeight = 30,
            lootMinWeight = 5,
            lootMaxWeight = 10
        ),
        DungeonFloorWeight(
            dungeonId = 1,
            floor = 2,
            room = 1,
            monsterMaxWeight = 50,
            lootMinWeight = 5,
            lootMaxWeight = 10
        ),
        DungeonFloorWeight(
            dungeonId = 1,
            floor = 2,
            room = 2,
            monsterMaxWeight = 50,
            lootMinWeight = 5,
            lootMaxWeight = 10
        ),
        DungeonFloorWeight(
            dungeonId = 1,
            floor = 2,
            room = 3,
            monsterMaxWeight = 50,
            lootMinWeight = 5,
            lootMaxWeight = 10
        ),
        DungeonFloorWeight(
            dungeonId = 1,
            floor = 3,
            room = 1,
            monsterMaxWeight = 50,
            lootMinWeight = 10,
            lootMaxWeight = 20
        ),
        DungeonFloorWeight(
            dungeonId = 1,
            floor = 3,
            room = 2,
            monsterMaxWeight = 50,
            lootMinWeight = 10,
            lootMaxWeight = 20
        ),
        DungeonFloorWeight(
            dungeonId = 1,
            floor = 3,
            room = 3,
            monsterMaxWeight = 70,
            lootMinWeight = 20,
            lootMaxWeight = 40
        ),
    )

    suspend fun initialize() {
        logger.initialize()
        /* TODO : get map info usecases */
    }

    fun createDungeonEvents(floor: Int, room: Int, event: Int): List<BaseAdventureEvents> {
        val eventList = mutableListOf<BaseAdventureEvents>()

        for (f in 1..floor) {
            // 해당 floor의 속성 가져오기 (dungeonId는 1로 가정)
            val attributes = mockDungeonMapAttributes.find { it.floor == f } ?: continue

            // 해당 floor에서 발생 가능한 이벤트 ID 목록 (현재 로직에서는 미사용)
            val availableEvents = mockDungeonEventTables.find { it.floor == f }?.eventId

            for (r in 1..room) {
                // 해당 floor, room의 가중치 정보 (현재 로직에서는 미사용)
                val floorWeight = mockDungeonFloorWeight.find { it.floor == f && it.room == r }

                for (e in 1..event) {
                    val totalRate =
                        attributes.noneRate + attributes.battleRate + attributes.eventRate
                    val randomValue = Random.nextInt(totalRate)

                    val generatedEvent = when {
                        randomValue < attributes.noneRate -> {
                            EmptyEvent(floor = f, room = r, eventNo = e)
                        }

                        randomValue < attributes.noneRate + attributes.battleRate -> {
                            BattleEvent(floor = f, room = r, eventNo = e)
                        }

                        else -> {
                            CommonEvent(floor = f, room = r, eventNo = e)
                        }
                    }
                    eventList.add(generatedEvent)
                }
            }
        }
        return eventList
    }

    /* TODO : logOpenGap -> in sec */
    fun runAdventure(party: Party, eventList: List<BaseAdventureEvents>, logOpenGap: Int) {

        for (event in eventList) {
            when (event.createLog(party, logOpenGap)) {
                MapEventFlag.NONE -> {}
                MapEventFlag.RETREAT -> { /* TODO : retreat logic */ }
            }

            party.partyMembers.forEach { actor ->
                for (action in actor.actorActions) {
                    when (action.execute()) {
                        ActionStatusFlag.NONE -> {
                            continue
                        }

                        ActionStatusFlag.PERFORMED -> {
                            break
                        }
                    }
                }
            }

            for (action in party.partyActions) {
                when (action.execute()) {
                    ActionStatusFlag.NONE -> {
                        continue
                    }

                    ActionStatusFlag.PERFORMED -> {
                        break
                    }
                }
            }
        }
    }
}

