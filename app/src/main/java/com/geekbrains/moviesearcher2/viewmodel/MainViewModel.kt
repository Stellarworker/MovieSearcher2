package com.geekbrains.moviesearcher2.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.geekbrains.moviesearcher2.model.Repository
import com.geekbrains.moviesearcher2.model.RepositoryImpl
import java.lang.Thread.sleep

class MainViewModel(
    private val liveDataToObserve: MutableLiveData<AppState> = MutableLiveData(),
    private val repositoryImpl: Repository = RepositoryImpl()
) : ViewModel() {
    private val errorLoadingDataMessage = "Error loading data!"
    fun getLiveData() = liveDataToObserve
    fun getMovieFromLocalSource() = getDataFromLocalSource()
    fun getMovieFromRemoteSource() = getDataFromLocalSource()
    private fun getDataFromLocalSource() {
        liveDataToObserve.value = AppState.Loading
        Thread {
            sleep(2000)
            if (Math.random() > 0.5) {
                liveDataToObserve.postValue(
                    AppState.Success(repositoryImpl.getMovieFromLocalStorage())
                )
            } else {
                liveDataToObserve.postValue(
                    AppState.Error(Throwable(errorLoadingDataMessage))
                )
            }
        }.start()
    }
}