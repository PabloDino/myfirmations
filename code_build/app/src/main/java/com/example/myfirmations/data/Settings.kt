package com.example.myfirmations.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Settings(
    @PrimaryKey(autoGenerate = true) val id:Int,
    @ColumnInfo val scrollSpeed:Float,
    @ColumnInfo val allowSpeaking:Boolean,
    @ColumnInfo val selectedVoice:String
)
