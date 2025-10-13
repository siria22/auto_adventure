package com.example.data.remote.feature.actor.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index

@Entity(
    tableName = "actor_action",
    primaryKeys = ["actor_id", "index"],
    foreignKeys = [
        ForeignKey(
            entity = ActorEntity::class,
            parentColumns = ["actor_id"],
            childColumns = ["actor_id"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index(value = ["actor_id"])]
)
data class ActorActionEntity(
    @ColumnInfo(name = "actor_id")
    val actorId: Long,

    @ColumnInfo(name = "index")
    val index: Long,

    @ColumnInfo(name = "instruction")
    val instruction: String,

    @ColumnInfo(name = "is_activated")
    val isActivated: Boolean
)