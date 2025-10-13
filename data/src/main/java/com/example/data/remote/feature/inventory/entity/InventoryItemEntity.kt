package com.example.data.remote.feature.inventory.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import com.example.data.remote.feature.party.entity.PartyEntity

@Entity(
    tableName = "inventory_item",
    primaryKeys = ["party_id", "item_id"],
    foreignKeys = [
        ForeignKey(
            entity = PartyEntity::class,
            parentColumns = ["party_id"],
            childColumns = ["party_id"],
            onDelete = ForeignKey.SET_DEFAULT
        )
    ]
)
data class InventoryItemEntity(
    @ColumnInfo(name="party_id", defaultValue = "0")
    val partyId: Long,
    
    @ColumnInfo(name="item_id")
    val itemId: Long,
    
    @ColumnInfo(name="amount")
    val amount: Long  // todo: 최대 스택 수 이상 가질 수 없도록 Domain에서 조절
)
