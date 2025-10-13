package com.example.data.remote.feature.party.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.data.remote.feature.party.entity.PartyMemberEntity

@Dao
interface PartyMemberDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(member: PartyMemberEntity)

    @Query("SELECT * FROM party_member WHERE party_id = :partyId")
    suspend fun getMembersByPartyId(partyId: Long): List<PartyMemberEntity>

    @Query("SELECT * FROM party_member WHERE actor_id = :actorId")
    suspend fun getPartyByActorId(actorId: Long): PartyMemberEntity?


    @Query("UPDATE party_member SET is_party_leader = :isPartyLeader WHERE actor_id = :actorId")
    suspend fun updatePartyLeader(actorId: Long, isPartyLeader: Boolean)
}
