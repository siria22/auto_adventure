package com.example.domain.model.feature.common

import com.example.domain.model.feature.types.ActionStatusFlag

interface BaseAction {
    fun execute() : ActionStatusFlag
}
