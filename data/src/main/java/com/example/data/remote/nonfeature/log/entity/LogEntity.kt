package com.example.data.remote.nonfeature.log.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import java.time.LocalDate

@Entity(
    tableName = "log",
    indices = [Index(value = ["reveal_time"])]
)
data class LogEntity(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    val id: Long = 0,

    @ColumnInfo(name="depth")
    val depth: Int,

    @ColumnInfo(name="parent_id")
    val parentId: Long,

    @ColumnInfo(name="category")
    val category: String,

    @ColumnInfo(name = "title")
    val title: String,

    @ColumnInfo(name = "description")
    val description: String,

    @ColumnInfo(name="reveal_time")
    val revealTime: LocalDate,

    @ColumnInfo(name="creation_time")
    val creationTime: LocalDate,
)