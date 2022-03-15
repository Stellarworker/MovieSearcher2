package com.geekbrains.moviesearcher2.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class DetailsViewModel() : ViewModel() {
    private val liveDataToObserve: MutableLiveData<Int> = MutableLiveData()
    fun getLiveData() = liveDataToObserve
    fun postMovie(id: Int) {
        liveDataToObserve.value = id
    }
}