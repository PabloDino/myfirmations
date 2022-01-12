package com.example.myfirmations

import android.app.Application
import com.example.myfirmations.data.FirmDatabase

class FirmApplication: Application(){
    val database: FirmDatabase by lazy{
        FirmDatabase.getDatabase(this)
    }
}