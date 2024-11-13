package com.example.miproyectocondb

import android.content.Context
import androidx.room.Room
import com.example.miproyectocondb.database.AppDatabase

object DatabaseProvider {
    private var database: AppDatabase? = null

    fun getDatabase(context: Context): AppDatabase {
        return database ?: synchronized(this) {
            val tempDb = Room.databaseBuilder(
                context.applicationContext,
                AppDatabase::class.java,
                "app_database"
            ).build()
            database = tempDb
            tempDb
        }
    }
}