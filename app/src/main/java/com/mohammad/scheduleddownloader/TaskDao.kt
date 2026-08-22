package com.mohammad.scheduleddownloader
import androidx.room.*
import kotlinx.coroutines.flow.Flow
@Dao interface TaskDao { @Query("SELECT * FROM tasks ORDER BY scheduledAt DESC") fun all():Flow<List<Task>>; @Insert suspend fun insert(t:Task):Long; @Query("SELECT * FROM tasks WHERE id=:id") suspend fun get(id:Long):Task?; @Update suspend fun update(t:Task); @Query("DELETE FROM tasks WHERE id=:id") suspend fun delete(id:Long); @Query("SELECT * FROM tasks WHERE status='SCHEDULED'") suspend fun scheduled():List<Task> }
