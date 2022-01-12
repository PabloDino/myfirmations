package com.example.myfirmations.data

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

@Dao
interface SettingsDao {
    @Query("Select * from Settings")
    fun getSettings(): LiveData<Settings>

    @Update
    suspend fun update(settings: Settings)

    @Insert
    suspend fun insert(settings: Settings)

}