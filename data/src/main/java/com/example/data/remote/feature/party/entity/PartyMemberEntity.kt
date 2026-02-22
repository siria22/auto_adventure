package com.example.data.remote.feature.party.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import com.example.data.remote.feature.actor.entity.ActorEntity

@Entity(
    tableName = "party_member",
    primaryKeys = ["actor_id", "party_id"],
    foreignKeys = [
        ForeignKey(
            entity = ActorEntity::class,
            parentColumns = ["actor_id"],
            childColumns = ["actor_id"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = PartyEntity::class,
            parentColumns = ["party_id"],
            childColumns = ["party_id"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [
        Index(value = ["actor_id"]),
        Index(value = ["party_id"]),
    ]
)
data class PartyMemberEntity(
    @ColumnInfo(name = "actor_id")
    val actorId: Long,

    @ColumnInfo(name = "party_id")
    val partyId: Long,

    @ColumnInfo(name = "is_party_leader")
    val isPartyLeader: Boolean,

    @ColumnInfo(name = "position")
    val position: String,

    @ColumnInfo(name = "slot_index")
    val slotIndex: Int
)
