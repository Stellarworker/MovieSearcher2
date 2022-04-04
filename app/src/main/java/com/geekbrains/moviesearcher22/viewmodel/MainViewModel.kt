package com.geekbrains.moviesearcher22.viewmodel

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.geekbrains.moviesearcher22.common.CORRUPTED_DATA
import com.geekbrains.moviesearcher22.common.REQUEST_ERROR
import com.geekbrains.moviesearcher22.common.SERVER_ERROR
import com.geekbrains.moviesearcher22.model.MoviesDTO
import com.geekbrains.moviesearcher22.repository.movies.MoviesRepository
import com.geekbrains.moviesearcher22.repository.movies.MoviesRepositoryImpl
import com.geekbrains.moviesearcher22.repository.movies.MoviesRemoteDataSource
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

@RequiresApi(Build.VERSION_CODES.N)
class MainViewModel(
    val moviesLiveData: MutableLiveData<AppState> = MutableLiveData(),
    private val moviesRepositoryImpl: MoviesRepository =
        MoviesRepositoryImpl(MoviesRemoteDataSource())
) : ViewModel() {

    private val callBack = object : Callback<MoviesDTO> {

        override fun onResponse(call: Call<MoviesDTO>, response: Response<MoviesDTO>) {
            val serverResponse: MoviesDTO? = response.body()
            moviesLiveData.postValue(
                if (response.isSuccessful && serverResponse != null) {
                    checkResponse(serverResponse)
                } else {
                    AppState.Error(Throwable(SERVER_ERROR))
                }
            )
        }

        override fun onFailure(call: Call<MoviesDTO>, t: Throwable) {
            moviesLiveData.postValue(AppState.Error(Throwable(t.message ?: REQUEST_ERROR)))
        }

        private fun checkResponse(serverResponse: MoviesDTO) =
            if (serverResponse.results?.size == 0
                || serverResponse.results?.get(0) != null
            ) {
                AppState.Success(serverResponse)
            } else {
                AppState.Error(Throwable(CORRUPTED_DATA))
            }
    }

    fun getMovies(includeAdult: Boolean, query: String) {
        moviesLiveData.value = AppState.Loading
        moviesRepositoryImpl.getMoviesFromServer(includeAdult, query, callBack)
    }
}