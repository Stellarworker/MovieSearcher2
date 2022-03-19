package com.geekbrains.moviesearcher2.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.geekbrains.moviesearcher2.model.MovieDetails
import com.geekbrains.moviesearcher2.repository.details.MovieDetailsRemoteDataSource
import com.geekbrains.moviesearcher2.repository.details.MovieDetailsRepository
import com.geekbrains.moviesearcher2.repository.details.MovieDetailsRepositoryImpl
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import com.geekbrains.moviesearcher2.utils.*

class DetailsViewModel(
    val movieDetailsLiveData: MutableLiveData<AppStateDetails> = MutableLiveData(),
    private val movieDetailsRepositoryImpl: MovieDetailsRepository = MovieDetailsRepositoryImpl(
        MovieDetailsRemoteDataSource()
    )
) : ViewModel() {
    private var movieID = -1
    private val callBack = object : Callback<MovieDetails> {
        override fun onResponse(call: Call<MovieDetails>, response: Response<MovieDetails>) {
            val serverResponse: MovieDetails? = response.body()
            movieDetailsLiveData.postValue(
                if (response.isSuccessful && serverResponse != null) {
                    checkResponse(serverResponse)
                } else {
                    AppStateDetails.Error(Throwable(SERVER_ERROR))
                }
            )
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

        private fun checkResponse(serverResponse: MovieDetails): AppStateDetails =
            if (serverResponse.title != null) {
                AppStateDetails.Success(serverResponse)
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

}