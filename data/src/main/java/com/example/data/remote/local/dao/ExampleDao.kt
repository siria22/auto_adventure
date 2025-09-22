package com.example.data.remote.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.data.remote.local.entity.ExampleEntity

@Dao
interface ExampleDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun createExampleEntity(exampleEntity: ExampleEntity)

    @Query("SELECT * FROM example")
    suspend fun getAllExampleEntity(): List<ExampleEntity>

    @Delete
    suspend fun deleteExampleEntity(exampleEntity: ExampleEntity)
}