package com.geekbrains.moviesearcher2.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.geekbrains.moviesearcher2.model.Movie
import com.geekbrains.moviesearcher2.model.Repository
import com.geekbrains.moviesearcher2.model.RepositoryImpl
import java.lang.Thread.sleep

class MainViewModel(
    private val liveDataToObserve: MutableLiveData<AppState> = MutableLiveData(),
    private val repositoryImpl: Repository = RepositoryImpl()
) : ViewModel() {
    fun getLiveData() = liveDataToObserve
    fun getMovies(query: String) = getMoviesFromServer(query)
    private fun getMoviesFromServer(query: String) {
        liveDataToObserve.value = AppState.Loading
        Thread {
            sleep(2000)
            liveDataToObserve.postValue(
                if (Math.random() > 0.4)
                    AppState.Success(repositoryImpl.getMoviesFromServer(query))
                else AppState.Error(Throwable())
            )
        }.start()
    }
}