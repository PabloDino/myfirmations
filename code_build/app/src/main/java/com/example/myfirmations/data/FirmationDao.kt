package com.example.myfirmations.data

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao

interface FirmationDao {

    @Query("Select * from Firmation")
    fun getFirms(): Flow<List<Firmation>>

    @Query("Select * from Firmation where id =:id")
    fun getFirmById(id:String): Flow<Firmation>

    @Insert(onConflict=  OnConflictStrategy.IGNORE)
    suspend fun insert(firm: Firmation)

    @Insert
    suspend fun insertList(firmList:List<Firmation>)

    @Update
    suspend fun update(firm: Firmation)

}