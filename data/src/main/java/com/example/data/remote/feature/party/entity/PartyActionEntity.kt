package com.example.data.remote.feature.party.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index

@Entity(
    tableName = "party_action",
    primaryKeys = ["party_id", "index"],
    foreignKeys = [
        ForeignKey(
            entity = PartyEntity::class,
            parentColumns = ["party_id"],
            childColumns = ["party_id"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index(value = ["party_id"])]
)
data class PartyActionEntity(
    @ColumnInfo(name = "party_id")
    val partyId: Long,

    @ColumnInfo(name = "index")
    val index: Long,

    @ColumnInfo(name = "instruction")
    val instruction: String,

    @ColumnInfo(name = "is_activated")
    val isActivated: Boolean
)
