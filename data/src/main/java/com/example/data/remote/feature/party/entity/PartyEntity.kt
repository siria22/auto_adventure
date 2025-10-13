package com.example.data.remote.feature.party.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date

@Entity(tableName = "party")
data class PartyEntity(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "party_id")
    val partyId: Long,
    // TODO : 파티는 최대 4개까지 구성 가능함, 앱 최초 실행 시 id=0인 공용 객체 생성

    @ColumnInfo(name = "name")
    val name: String,

    @ColumnInfo(name = "is_on_adventure")
    val isOnAdventure: Boolean,

    @ColumnInfo(name = "adventure_start_time")
    val adventureStartTime: Date?,
)
