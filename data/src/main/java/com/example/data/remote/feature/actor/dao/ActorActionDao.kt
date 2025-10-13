package com.example.data.remote.feature.actor.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.data.remote.feature.actor.entity.ActorActionEntity

@Dao
interface ActorActionDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(action: ActorActionEntity)

    @Query("SELECT * FROM actor_action")
    suspend fun getAllActions(): List<ActorActionEntity>

    @Query("SELECT * FROM actor_action WHERE actor_id = :actorId")
    suspend fun getActionsByActorId(actorId: Long): List<ActorActionEntity>

    @Query("SELECT * FROM actor_action WHERE actor_id = :actorId AND `index` = :index")
    suspend fun getActionById(actorId: Long, index: Long): ActorActionEntity?
}
