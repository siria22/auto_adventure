package com.example.data.remote.feature.quest.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.data.remote.feature.quest.entity.AcceptedQuestEntity

@Dao
interface AcceptedQuestDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(quest: AcceptedQuestEntity)

    @Query("SELECT * FROM accepted_quest")
    suspend fun getAllAcceptedQuests(): List<AcceptedQuestEntity>

    @Query("SELECT * FROM accepted_quest WHERE accepted_quest_id = :id")
    suspend fun getAcceptedQuestById(id: Long): AcceptedQuestEntity?

    @Query("SELECT * FROM accepted_quest WHERE party_id = :partyId")
    suspend fun getAcceptedQuestsByPartyId(partyId: Long): List<AcceptedQuestEntity>
}
