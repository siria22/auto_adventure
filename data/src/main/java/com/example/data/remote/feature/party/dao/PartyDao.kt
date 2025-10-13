package com.example.data.remote.feature.party.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.data.remote.feature.party.entity.PartyEntity

@Dao
interface PartyDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(party: PartyEntity)

    @Query("SELECT * FROM party")
    suspend fun getAllParties(): List<PartyEntity>

    @Query("SELECT * FROM party WHERE party_id = :id")
    suspend fun getPartyById(id: Long): PartyEntity?

    @Query("SELECT * FROM party WHERE name = :name")
    suspend fun getPartyByName(name: String): PartyEntity?
}
