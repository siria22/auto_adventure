package com.example.data.remote.feature.party.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.data.remote.feature.party.entity.PartyActionEntity

@Dao
interface PartyActionDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(action: PartyActionEntity)

    @Query("SELECT * FROM party_action")
    suspend fun getAllActions(): List<PartyActionEntity>

    @Query("SELECT * FROM party_action WHERE party_id = :partyId")
    suspend fun getActionsByPartyId(partyId: Long): List<PartyActionEntity>

    @Query("SELECT * FROM party_action WHERE party_id = :partyId AND `index` = :index")
    suspend fun getActionById(partyId: Long, index: Long): PartyActionEntity?
}
