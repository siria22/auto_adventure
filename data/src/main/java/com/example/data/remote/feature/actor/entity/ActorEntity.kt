package com.example.data.remote.feature.actor.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date

@Entity(tableName = "actor")
data class ActorEntity(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "actor_id")
    val actorId: Long = 0,

    @ColumnInfo(name = "name")
    val name: String,

    @ColumnInfo(name = "exp_total")
    val totalExp: Long,


    @ColumnInfo(name = "max_hp")
    val maxHp: Long,

    @ColumnInfo(name = "current_hp")
    val currentHp: Long,

    @ColumnInfo(name = "max_mp")
    val maxMp: Long,

    @ColumnInfo(name = "current_mp")
    val currentMp: Long,


    @ColumnInfo(name = "strength")
    val strength: Long,

    @ColumnInfo(name = "agility")
    val agility: Long,

    @ColumnInfo(name = "luck")
    val luck: Long,

    @ColumnInfo(name = "intelligence")
    val intelligence: Long,


    @ColumnInfo(name = "remain_skill_point")
    val remainSkillPoint: Long,

    @ColumnInfo(name = "remain_stat_point")
    val remainStatPoint: Long,


    @ColumnInfo(name = "personality_id")
    val personalityId: Long,

    @ColumnInfo(name = "character_category")
    val actorCategory: String,

    @ColumnInfo(name = "job_id")
    val jobId: Long,

    @ColumnInfo(name = "race")
    val race: String,

    @ColumnInfo(name = "element_type")
    val elementType: String,

    @ColumnInfo(name = "rank")
    val rank: String,


    @ColumnInfo(name = "adventure_count")
    val adventureCount: Long,

    @ColumnInfo(name = "kill_count")
    val killCount: Long,


    @ColumnInfo(name = "recovery_end_time")
    val recoveryEndTime: Date?,

    @ColumnInfo(name = "recruited_at")
    val recruitedAt: Date?
)
