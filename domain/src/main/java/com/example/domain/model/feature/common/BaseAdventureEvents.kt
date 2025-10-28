package com.example.domain.model.feature.common

import com.example.domain.model.feature.adv.Party
import com.example.domain.model.feature.types.MapEventFlag

interface BaseAdventureEvents {

    var floor: Int
    var room: Int
    var eventNo: Int

    fun createLog(party: Party, logOpenGap: Int): MapEventFlag
}


class EmptyEvent(
    override var floor: Int,
    override var room: Int,
    override var eventNo: Int
) : BaseAdventureEvents {

    override fun createLog(party: Party, logOpenGap: Int): MapEventFlag {
        return MapEventFlag.NONE
    }
}

class CommonEvent(
    override var floor: Int,
    override var room: Int,
    override var eventNo: Int
) : BaseAdventureEvents {

    override fun createLog(party: Party, logOpenGap: Int): MapEventFlag {
        return MapEventFlag.NONE
    }
}

class BattleEvent(
    override var floor: Int,
    override var room: Int,
    override var eventNo: Int
) : BaseAdventureEvents {

    override fun createLog(party: Party, logOpenGap: Int): MapEventFlag {

        return MapEventFlag.NONE
    }


}
