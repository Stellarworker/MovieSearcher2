package com.geekbrains.moviesearcher2.viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.geekbrains.moviesearcher2.app.App
import com.geekbrains.moviesearcher2.config.DEBUG_MODE
import com.geekbrains.moviesearcher2.repository.local.LocalRepositoryImpl

class HistoryViewModel(
    val historyLiveData: MutableLiveData<AppStateHistory> = MutableLiveData(),
    private val historyRepositoryImpl: LocalRepositoryImpl = LocalRepositoryImpl(App.getHistoryDao())
) : ViewModel() {
    private val TAG = "HISTORY_VIEW_MODEL"

    fun getAllHistory() {
        historyLiveData.value = AppStateHistory.Loading
        try {
            Thread {
                historyLiveData.postValue(AppStateHistory.Success(historyRepositoryImpl.getAllHistory()))
            }.start()
        } catch (e: Throwable) {
            if (DEBUG_MODE) {
                e.message?.let {
                    Log.d(TAG, it)
                }
            }
        }
    }
}