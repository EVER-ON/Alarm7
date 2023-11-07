package com.example.alarm

import android.app.Application
import androidx.room.Room
import com.example.alarm.data.AppDatabase
import com.example.alarm.data.NoteDao

class App : Application() {

    private var database: AppDatabase? = null
    private var noteDao: NoteDao? = null

    companion object {
        private var instance: App? = null

        fun getInstance(): App {
            return instance!!
        }
    }

    override fun onCreate() {
        super.onCreate()
        instance = this
        database = Room.databaseBuilder(
            applicationContext,
            AppDatabase::class.java, "app-db-name"
        )
            .allowMainThreadQueries()
            .build()
        noteDao = database!!.noteDao()
    }

    fun getDatabase(): AppDatabase {
        return database!!
    }

    fun getNoteDao(): NoteDao {
        return noteDao!!
    }
}
