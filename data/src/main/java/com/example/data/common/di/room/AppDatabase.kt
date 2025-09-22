package com.example.data.common.di.room

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.data.remote.local.dao.ExampleDao
import com.example.data.remote.local.entity.ExampleEntity

@Database(
    entities = [ExampleEntity::class],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun exampleDao(): ExampleDao
}