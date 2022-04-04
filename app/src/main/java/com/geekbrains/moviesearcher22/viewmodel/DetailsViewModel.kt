package com.geekbrains.moviesearcher22.viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.geekbrains.moviesearcher22.app.App.Companion.getHistoryDao
import com.geekbrains.moviesearcher22.common.CORRUPTED_DATA
import com.geekbrains.moviesearcher22.common.EMPTY_STRING
import com.geekbrains.moviesearcher22.common.REQUEST_ERROR
import com.geekbrains.moviesearcher22.common.SERVER_ERROR
import com.geekbrains.moviesearcher22.config.DEBUG_MODE
import com.geekbrains.moviesearcher22.model.MovieDetails
import com.geekbrains.moviesearcher22.model.MovieDetailsInt
import com.geekbrains.moviesearcher22.repository.details.MovieDetailsRemoteDataSource
import com.geekbrains.moviesearcher22.repository.details.MovieDetailsRepository
import com.geekbrains.moviesearcher22.repository.details.MovieDetailsRepositoryImpl
import com.geekbrains.moviesearcher22.repository.local.LocalRepositoryImpl
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import com.geekbrains.moviesearcher22.utils.*
import java.io.IOException

private const val TAG = "DETAILS_VIEW_MODEL"

class DetailsViewModel(
    val movieDetailsLiveData: MutableLiveData<AppStateDetails> = MutableLiveData(),
    private val movieDetailsRepositoryImpl: MovieDetailsRepository =
        MovieDetailsRepositoryImpl(MovieDetailsRemoteDataSource()),
    private val historyRepositoryImpl: LocalRepositoryImpl = LocalRepositoryImpl(getHistoryDao())
) : ViewModel() {

    private var movieID = -1
    private val callBack = object : Callback<MovieDetails> {

        override fun onResponse(call: Call<MovieDetails>, response: Response<MovieDetails>) {
            val serverResponse: MovieDetails? = response.body()
            Thread {
                try {
                    movieDetailsLiveData.postValue(
                        if (response.isSuccessful && serverResponse != null) {
                            checkResponse(serverResponse)
                        } else {
                            AppStateDetails.Error(Throwable(SERVER_ERROR))
                        }
                    )
                } catch (e: IOException) {
                    if (DEBUG_MODE) {
                        e.printStackTrace()
                        e.message?.let { message -> Log.d(TAG, message) }
                    }
                }
            }.start()
        }

        override fun onFailure(call: Call<MovieDetails>, t: Throwable) {
            movieDetailsLiveData.postValue(
                AppStateDetails.Error(
                    Throwable(
                        t.message ?: REQUEST_ERROR
                    )
                )
            )
        }

        private fun checkResponse(serverResponse: MovieDetails) =
            if (serverResponse.title != null) {
                val movieDetailsInt = convertMovieDetailsToMovieDetailsInt(serverResponse)
                movieDetailsInt.note = getNote(movieID) ?: EMPTY_STRING
                AppStateDetails.Success(movieDetailsInt)
            } else {
                AppStateDetails.Error(Throwable(CORRUPTED_DATA))
            }
    }

    fun getMovieID() = movieID

    fun getMovieDetails(id: Int) {
        movieID = id
        movieDetailsLiveData.value = AppStateDetails.Loading
        movieDetailsRepositoryImpl.getMovieDetailsFromServer(id, callBack)
    }

    fun saveMovieDetailsIntToDB(movieDetailsInt: MovieDetailsInt) {
        movieDetailsInt.viewTime = System.currentTimeMillis()
        historyRepositoryImpl.saveDetails(movieDetailsInt)
    }

    fun saveNoteToDb(note: String) {
        historyRepositoryImpl.updateNote(movieID, note)
    }

    fun getNote(movieID: Int) = historyRepositoryImpl.getNote(movieID)

}