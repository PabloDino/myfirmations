package com.example.myfirmations

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.myfirmations.data.Settings
import com.example.myfirmations.data.SettingsDao
import kotlinx.coroutines.launch

class SettingsViewModel(private val settingsDao: SettingsDao): ViewModel() {

     fun getSettings(): LiveData<Settings> = settingsDao.getSettings()

    val currentSettings = getSettings()

    fun update(settings: Settings)
    {
        viewModelScope.launch {
            settingsDao.update(settings)
        }
    }


    fun insert(settings: Settings){
        viewModelScope.launch{
            settingsDao.update(settings)
        }
    }


    fun updateSettings(
        scrollSpeed:Float,
        allowSpeaking:Boolean,
        currentVoice:String
    )
    {
        val updatedSetting = Settings(1, scrollSpeed, allowSpeaking,currentVoice)
        update(updatedSetting)
    }

    fun insertSetting(
        scrollSpeed:Float,
        allowSpeaking:Boolean,
        currentVoice:String
    )
    {
        val newSetting = Settings(0, scrollSpeed, allowSpeaking,currentVoice)
        insert(newSetting)
    }
}

class SettingsViewModelFactory(private val settingsDao: SettingsDao):ViewModelProvider.Factory{
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(SettingsViewModel::class.java))
        {
            @Suppress("UNCHECKED_CAST")
            return SettingsViewModel(settingsDao) as T
        }
        throw IllegalArgumentException("Unknown Viewmodel class")
    }
}