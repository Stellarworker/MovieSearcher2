package com.geekbrains.moviesearcher2.app

import android.app.Application
import androidx.room.Room
import com.geekbrains.moviesearcher2.model.room.HistoryDao
import com.geekbrains.moviesearcher2.model.room.HistoryDataBase
import java.lang.IllegalStateException

private const val APP_ERROR_MESSAGE = "APP must not be null"

class App : Application() {
    override fun onCreate() {
        super.onCreate()
        appInstance = this
    }

    companion object {
        private var appInstance: App? = null
        private var db: HistoryDataBase? = null
        private const val DB_NAME = "History.db"

        fun getHistoryDao(): HistoryDao {

            synchronized(HistoryDataBase::class.java) {
                if (db == null) {
                    if (appInstance == null) {
                        throw IllegalStateException(APP_ERROR_MESSAGE)
                    }
                    db = Room.databaseBuilder(
                        appInstance!!.applicationContext,
                        HistoryDataBase::class.java,
                        DB_NAME
                    ).build()
                }
            }
            return db!!.historyDao()
        }
    }
}