package com.geekbrains.moviesearcher2.viewmodel

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.geekbrains.moviesearcher2.model.MoviesDTO
import com.geekbrains.moviesearcher2.view.MoviesLoader

@RequiresApi(Build.VERSION_CODES.N)
class MainViewModel(
    private val liveDataToObserve: MutableLiveData<AppState> = MutableLiveData(),
) : ViewModel() {
    fun getLiveData() = liveDataToObserve
    fun getMovies(query: String) = getMoviesFromServer(query)
    private fun getMoviesFromServer(query: String) {
        liveDataToObserve.value = AppState.Loading
        val onLoadListener: MoviesLoader.MoviesLoaderListener =
            object : MoviesLoader.MoviesLoaderListener {
                override fun onLoaded(moviesDTO: MoviesDTO) {
                    liveDataToObserve.postValue(AppState.Success(moviesDTO))
                }

                override fun onFailed(throwable: Throwable) {
                    liveDataToObserve.postValue(AppState.Error(throwable))
                }
            }
        val loader = MoviesLoader(onLoadListener, query)
        loader.loadMovies()
    }
}