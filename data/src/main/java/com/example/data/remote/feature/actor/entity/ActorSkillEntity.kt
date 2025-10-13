package com.example.data.remote.feature.actor.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index

@Entity(
    tableName = "actor_skill",
    primaryKeys = ["actor_id", "skill_id"],
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
data class ActorSkillEntity(
    @ColumnInfo(name = "actor_id")
    val actorId: Long,

    @ColumnInfo(name = "skill_id")
    val skillId: Long,

    @ColumnInfo(name = "skill_level")
    val skillLevel: Long,

    @ColumnInfo(name = "is_equipped")
    val isEquipped: Boolean
)
