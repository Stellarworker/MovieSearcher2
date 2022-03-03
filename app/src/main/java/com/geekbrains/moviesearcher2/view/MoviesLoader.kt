package com.geekbrains.moviesearcher2.view

import android.os.Build
import android.os.Handler
import android.util.Log
import androidx.annotation.RequiresApi
import com.geekbrains.moviesearcher2.BuildConfig
import com.geekbrains.moviesearcher2.model.MoviesDTO
import com.google.gson.Gson
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.MalformedURLException
import java.net.URL
import java.util.stream.Collectors
import javax.net.ssl.HttpsURLConnection

@RequiresApi(Build.VERSION_CODES.N)
class MoviesLoader(
    private val listener: MoviesLoaderListener,
    private val query: String
) {

    @RequiresApi(Build.VERSION_CODES.N)
    fun loadMovies() =
        try {
            val uri =
                URL("https://api.themoviedb.org/3/search/movie?api_key=${BuildConfig.THEMOVIEDB_API_KEY}&query=$query")
            val handler = Handler()

            Thread {
                lateinit var urlConnection: HttpsURLConnection
                try {
                    urlConnection = (uri.openConnection() as HttpsURLConnection).apply {
                        requestMethod = "GET"
                        readTimeout = 10000
                    }

                    val bufferedReader =
                        BufferedReader(InputStreamReader(urlConnection.inputStream))

                    val response = getLines(bufferedReader)

                    val moviesDTO: MoviesDTO =
                        Gson().fromJson(response, MoviesDTO::class.java)

                    handler.post {
                        listener.onLoaded(moviesDTO)
                    }
                } catch (e: Exception) {
                    Log.e("", "Fail connection", e)
                    e.printStackTrace()
                    listener.onFailed(e)
                } finally {
                    urlConnection.disconnect()
                }
            }.start()
        } catch (e: MalformedURLException) {
            Log.e("", "Fail URI", e)
            e.printStackTrace()
            listener.onFailed(e)
        }

    @RequiresApi(Build.VERSION_CODES.N)
    private fun getLines(reader: BufferedReader): String {
        return reader.lines().collect(Collectors.joining("\n"))
    }

    interface MoviesLoaderListener {
        fun onLoaded(moviesDTO: MoviesDTO)
        fun onFailed(throwable: Throwable)
    }
}