package com.example.alarm.data

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.alarm.model.Note


@Database(entities = [Note::class], version = 1, exportSchema = false)
public abstract class AppDatabase : RoomDatabase() {
    abstract fun noteDao(): NoteDao?
}