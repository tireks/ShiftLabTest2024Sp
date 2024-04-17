package com.tirexmurina.shiftlabtest2024sp.localstore

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.tirexmurina.shiftlabtest2024sp.localstore.model.UserModelLocal

@Database(entities = [UserModelLocal::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase(){

    abstract fun userDao(): UserDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null
        fun getDatabase(context: Context): AppDatabase {
            // Если база данных еще не инициализирована, то инициализируем ее.
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "app_database"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}