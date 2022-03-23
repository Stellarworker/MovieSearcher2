package com.geekbrains.moviesearcher2.repository.local

import android.util.Log
import com.geekbrains.moviesearcher2.model.MovieDetailsInt
import com.geekbrains.moviesearcher2.model.room.HistoryDao
import com.geekbrains.moviesearcher2.utils.convertHistoryEntityToMovieDetailsInt
import com.geekbrains.moviesearcher2.utils.convertMovieDetailsIntToHistoryEntity

class LocalRepositoryImpl(private val localDataSource: HistoryDao) : LocalRepository {
    private val TAG = "LOCAL_REPOSITORY_IMPL"
    override fun getAllHistory() =
        convertHistoryEntityToMovieDetailsInt(localDataSource.all())

    override fun saveEntity(movieDetailsInt: MovieDetailsInt) {
        try {
            Thread {
                localDataSource.insert(convertMovieDetailsIntToHistoryEntity(movieDetailsInt))
            }.start()
        } catch (e: Throwable) {
            e.message?.let { Log.d(TAG, it) }
        }
    }

    fun saveNote(note: String) {
        try {
            Thread {
                localDataSource.updateLastNote(note)
            }.start()
        } catch (e: Throwable) {
            e.message?.let { Log.d(TAG, it) }
        }
    }
}