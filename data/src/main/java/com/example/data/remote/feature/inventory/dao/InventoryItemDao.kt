package com.example.data.remote.feature.inventory.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.data.remote.feature.inventory.entity.InventoryItemEntity

@Dao
interface InventoryItemDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(inventory: InventoryItemEntity)

    @Query("SELECT * FROM inventory_item")
    suspend fun getAllInventories(): List<InventoryItemEntity>

    @Query("SELECT * FROM inventory_item WHERE party_id = :partyId")
    suspend fun getInventoryByPartyId(partyId: Long): List<InventoryItemEntity>

}
