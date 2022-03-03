package com.geekbrains.moviesearcher2.viewmodel

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.geekbrains.moviesearcher2.model.MovieDetails

class DetailsViewModel() : ViewModel() {
    private val liveDataToObserve: MutableLiveData<MovieDetails> = MutableLiveData()
    fun getLiveData() = liveDataToObserve

    @RequiresApi(Build.VERSION_CODES.N)
    fun postMovie(details: MovieDetails) {
        liveDataToObserve.value = details
    }
}