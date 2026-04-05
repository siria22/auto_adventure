package com.example.data.remote.feature.actor.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.data.remote.feature.actor.entity.ActorEntity

@Dao
interface ActorDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(actor: ActorEntity): Long

    @Query("SELECT * FROM actor")
    suspend fun getAllActors(): List<ActorEntity>

    @Query("SELECT * FROM actor WHERE actor_id = :id")
    suspend fun getActorById(id: Long): ActorEntity?

    @Query("SELECT * FROM actor WHERE name = :name")
    suspend fun getActorByName(name: String): ActorEntity?

    @Query("SELECT COUNT(*) FROM actor WHERE recruited_at IS NOT NULL")
    suspend fun getRecruitedActorCount(): Int

    @Query("SELECT * FROM actor WHERE actor_id NOT IN (SELECT actor_id FROM party_member)")
    suspend fun getAvailableActors(): List<ActorEntity>
}
