package com.example.domain.model.feature.adv

import com.example.domain.model.feature.types.ActionStatusFlag
import com.example.domain.model.feature.common.BaseAction

data class PartyAction(
    val partyId: Long,
    val index: Long,
    val instruction: String, /* TODO : Parsed instruction actions in class */
    val isActivated: Boolean
) : BaseAction {

    override fun execute(): ActionStatusFlag {
        /* TODO : check condition */
        /* TODO : if satisfied -> execute */
        return ActionStatusFlag.NONE
    }
}