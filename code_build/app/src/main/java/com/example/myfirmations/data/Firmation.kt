package com.example.myfirmations.data

import android.content.Context
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.databinding.BindingAdapter
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.myfirmations.R
import java.security.AccessController.getContext


@Entity

data class Firmation(
    @PrimaryKey(autoGenerate=true) val id:Int,
    @ColumnInfo val quote:String,
    @ColumnInfo val imgName:String,
    @ColumnInfo val sayThis:Boolean,
    @ColumnInfo val snoozeTill: Long
)


