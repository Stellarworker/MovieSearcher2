package com.geekbrains.moviesearcher2.viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.geekbrains.moviesearcher2.app.App
import com.geekbrains.moviesearcher2.config.DEBUG_MODE
import com.geekbrains.moviesearcher2.repository.local.LocalRepositoryImpl
import java.io.IOException

private const val TAG = "HISTORY_VIEW_MODEL"

class HistoryViewModel(
    val historyLiveData: MutableLiveData<AppStateHistory> = MutableLiveData(),
    private val historyRepositoryImpl: LocalRepositoryImpl =
        LocalRepositoryImpl(App.getHistoryDao())
) : ViewModel() {

    fun getAllHistory() {
        historyLiveData.value = AppStateHistory.Loading
        Thread {
            try {
                historyLiveData.postValue(
                    AppStateHistory.Success(
                        historyRepositoryImpl.getAllHistory().sortedByDescending { it.viewTime })
                )
            } catch (e: IOException) {
                if (DEBUG_MODE) {
                    e.printStackTrace()
                    e.message?.let { message -> Log.d(TAG, message) }
                }
            }
        }.start()
    }
}
