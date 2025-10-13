package com.example.data.remote.feature.inventory.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ItemEntity(
    @SerialName("item_id")
    val itemId: Long,

    val name: String,

    @SerialName("short_desc")
    val shortDesc: String,

    @SerialName("full_desc")
    val fullDesc: String,

    val category: String,
    val weight: Double,

    @SerialName("max_stack_size")
    val maxStackSize: Int,

    @SerialName("obtain_method")
    val obtainMethod: List<String>,

    @SerialName("is_sellable")
    val isSellable: Boolean,

    @SerialName("buy_price")
    val buyPrice: Long,

    @SerialName("sell_price")
    val sellPrice: Long,

    @SerialName("is_usable")
    val isUsable: Boolean
)
