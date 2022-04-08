package com.geekbrains.moviesearcher22.model.room

import androidx.room.Database
import androidx.room.RoomDatabase

private const val CURRENT_DATABASE_VERSION = 1

@Database(
    entities = [HistoryEntity::class],
    version = CURRENT_DATABASE_VERSION, exportSchema = false
)
abstract class HistoryDataBase : RoomDatabase() {
    abstract fun historyDao(): HistoryDao
}