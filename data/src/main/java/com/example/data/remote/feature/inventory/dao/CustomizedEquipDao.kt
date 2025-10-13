package com.example.data.remote.feature.inventory.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.data.remote.feature.inventory.entity.CustomizedEquipEntity

@Dao
interface CustomizedEquipDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(equip: CustomizedEquipEntity)

    @Query("SELECT * FROM customized_equip")
    suspend fun getAllEquips(): List<CustomizedEquipEntity>

    @Query("SELECT * FROM customized_equip WHERE id = :id")
    suspend fun getEquipById(id: Long): CustomizedEquipEntity?

    @Query("SELECT * FROM customized_equip WHERE owner_id = :ownerId")
    suspend fun getEquipsByOwnerId(ownerId: Long): List<CustomizedEquipEntity>
}
