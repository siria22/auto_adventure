package com.example.data.remote.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "example")
data class ExampleEntity(
    @PrimaryKey(autoGenerate = true) val id: Long,
    val name : String
)