package com.example.data.remote.feature.quest.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import com.example.data.remote.feature.party.entity.PartyEntity


@Entity(
    tableName = "accepted_quest",
    foreignKeys = [ForeignKey(
        entity = PartyEntity::class,
        parentColumns = ["party_id"],
        childColumns = ["party_id"],
        onDelete = ForeignKey.CASCADE
    )]
)
data class AcceptedQuestEntity(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "accepted_quest_id")
    val acceptedQuestId: Long,

    @ColumnInfo(name = "quest_id")
    val questId: Long,

    @ColumnInfo(name = "party_id", index = true)
    val partyId: Long,

    @ColumnInfo(name = "is_cleared")
    val isCleared: Boolean
)