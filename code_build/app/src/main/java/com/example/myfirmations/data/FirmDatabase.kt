package com.example.myfirmations.data

import android.content.Context
import android.os.AsyncTask
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

@Database(entities=[Firmation::class, Settings::class], version = 1)
abstract class FirmDatabase:RoomDatabase()
{
    abstract fun firmDao(): FirmationDao
    abstract fun settingsDao(): SettingsDao


    companion object{
        @Volatile private var INSTANCE: FirmDatabase? = null
        fun getDatabase(context: Context): FirmDatabase {
            val instance = Room.databaseBuilder(
                context.applicationContext,
                FirmDatabase::class.java,
                "firm_database"
            ).fallbackToDestructiveMigration()
                .addCallback(roomCallBack)
                .build()
            INSTANCE = instance
            return instance
        }

        val roomCallBack = object: RoomDatabase.Callback(){
            override fun onCreate(db:SupportSQLiteDatabase){
                super.onCreate(db)
                INSTANCE?.let{ PopulateDb(it).execute()}
            }
        }

        internal class PopulateDb(db: FirmDatabase):AsyncTask<Void,Void,Void>(){
            val settings = DefaultData().loadSettings()
            val firmations = DefaultData().loadFirmations()
            val thisdb = db
            override  fun doInBackground(vararg p0:Void?):Void?{
                GlobalScope.launch {
                    thisdb.settingsDao().insert(settings)
                    thisdb.firmDao().insertList(firmations)
                }
                return null

            }
        }


    }

}