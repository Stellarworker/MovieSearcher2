package com.geekbrains.moviesearcher2.repository.local

import android.util.Log
import com.geekbrains.moviesearcher2.config.DEBUG_MODE
import com.geekbrains.moviesearcher2.model.MovieDetailsInt
import com.geekbrains.moviesearcher2.model.room.HistoryDao
import com.geekbrains.moviesearcher2.utils.convertHistoryEntityToMovieDetailsInt
import com.geekbrains.moviesearcher2.utils.convertMovieDetailsIntToHistoryEntity
import java.io.IOException

private const val TAG = "LOCAL_REPOSITORY_IMPL"

class LocalRepositoryImpl(private val localDataSource: HistoryDao) : LocalRepository {
    override fun getAllHistory() =
        convertHistoryEntityToMovieDetailsInt(localDataSource.all())

    override fun updateNote(movieID: Int, note: String) {
        Thread {
            try {
                localDataSource.updateNote(movieID, note)
            } catch (e: IOException) {
                if (DEBUG_MODE) {
                    e.printStackTrace()
                    e.message?.let { message -> Log.d(TAG, message) }
                }
            }
        }.start()
    }

    override fun saveDetails(movieDetailsInt: MovieDetailsInt) {
        Thread {
            try {
                when (localDataSource.getRecordsCount(movieDetailsInt.movieId)) {
                    0 -> localDataSource.insert(
                        convertMovieDetailsIntToHistoryEntity(movieDetailsInt)
                    )
                    else -> localDataSource.updateViewTime(
                        movieDetailsInt.movieId,
                        movieDetailsInt.viewTime
                    )
                }
            } catch (e: IOException) {
                if (DEBUG_MODE) {
                    e.printStackTrace()
                    e.message?.let { message -> Log.d(TAG, message) }
                }
            }
        }.start()
    }

    override fun getNote(movieID: Int) = localDataSource.getNote(movieID)

}