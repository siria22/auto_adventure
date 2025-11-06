package com.example.data.remote.feature.inventory.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.data.remote.feature.inventory.entity.InventoryItemEntity

@Dao
interface InventoryItemDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(inventory: InventoryItemEntity)

    @Update
    suspend fun update(inventoryItem: InventoryItemEntity)

    @Delete
    suspend fun delete(inventoryItem: InventoryItemEntity)

    @Query("SELECT * FROM inventory_item")
    suspend fun getAllInventories(): List<InventoryItemEntity>

    @Query("SELECT * FROM inventory_item WHERE party_id = :partyId")
    suspend fun getInventoryByPartyId(partyId: Long): List<InventoryItemEntity>

    @Query("SELECT * FROM inventory_item WHERE party_id = :partyId AND item_id = :itemId LIMIT 1")
    suspend fun findItem(partyId: Long, itemId: Long): InventoryItemEntity?
}
