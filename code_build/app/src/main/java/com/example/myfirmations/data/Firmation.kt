package com.example.myfirmations.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity

data class Firmation(
    @PrimaryKey(autoGenerate=true) val id:Int,
    @ColumnInfo val quote:String,
    @ColumnInfo val imgName:String,
    @ColumnInfo val sayThis:Boolean,
    @ColumnInfo val snoozeTill: Long
)
