package com.example.myapplication

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

@Dao
interface TaskDAO {
    @Insert
    fun insert(task: Task): Long

    @Update
    fun update(task: Task)

    @Delete
    fun delete(task: Task)

    @Query("SELECT * FROM tasks WHERE userId = :userId")
    fun getTaskForUser(userId: Int): List<Task>

    @Query("SELECT * FROM tasks WHERE id = :id")
    fun getTaskByIdI(id: Int): Task?
}