package com.example.data.remote.nonfeature.log.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.data.remote.nonfeature.log.entity.LogEntity
import java.time.LocalDate

@Dao
interface LogDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(logs: List<LogEntity>): List<Long>

    @Query("SELECT MAX(id) FROM log")
    suspend fun getLastLogId(): Long?

    /* without time constraint */

    @Query("SELECT * FROM log")
    suspend fun getAllLog(): List<LogEntity>

    @Query("SELECT * FROM log WHERE parent_id = :parentId")
    suspend fun getChildrenLogByParentId(parentId: Long): List<LogEntity>

    @Query("SELECT * FROM log WHERE category = :category")
    suspend fun getLogByCategory(category: String): List<LogEntity>


    /* with time constraint */

    @Query("SELECT * FROM log WHERE reveal_time <= :revealTime")
    suspend fun getAllLogWithTimeConstraint(revealTime: LocalDate = LocalDate.now()): List<LogEntity>

    @Query("SELECT * FROM log WHERE category = :category AND reveal_time <= :revealTime")
    suspend fun getLogByCategoryWithTimeConstraint(
        category: String,
        revealTime: LocalDate = LocalDate.now()
    ): List<LogEntity>

}
