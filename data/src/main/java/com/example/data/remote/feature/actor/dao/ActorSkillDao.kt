package com.example.data.remote.feature.actor.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.data.remote.feature.actor.entity.ActorSkillEntity

@Dao
interface ActorSkillDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(skill: ActorSkillEntity)

    @Query("SELECT * FROM actor_skill WHERE actor_id = :actorId")
    suspend fun getSkillsByActorId(actorId: Long): List<ActorSkillEntity>

    @Query("SELECT * FROM actor_skill WHERE actor_id = :actorId AND skill_id = :skillId")
    suspend fun getSkillById(actorId: Long, skillId: Long): ActorSkillEntity?
}
