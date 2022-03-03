package com.geekbrains.moviesearcher2.view.details

import android.os.Build
import android.os.Handler
import android.util.Log
import androidx.annotation.RequiresApi
import com.geekbrains.moviesearcher2.BuildConfig
import com.geekbrains.moviesearcher2.model.MovieDTO
import com.geekbrains.moviesearcher2.model.MovieDetails
import com.geekbrains.moviesearcher2.model.MoviesDTO
import com.google.gson.Gson
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.MalformedURLException
import java.net.URL
import java.util.stream.Collectors
import javax.net.ssl.HttpsURLConnection

@RequiresApi(Build.VERSION_CODES.N)
class DetailsLoader(
    private val listener: DetailsLoaderListener,
    private val id: Int
) {

    fun loadDetails() =
        try {
            val uri =
                URL("https://api.themoviedb.org/3/movie/$id?api_key=${BuildConfig.THEMOVIEDB_API_KEY}")
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

                    val details: MovieDetails =
                        Gson().fromJson(response, MovieDetails::class.java)

                    handler.post {
                        listener.onLoaded(details)
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

    private fun getLines(reader: BufferedReader): String {
        return reader.lines().collect(Collectors.joining("\n"))
    }

    interface DetailsLoaderListener {
        fun onLoaded(details: MovieDetails)
        fun onFailed(throwable: Throwable)
    }
}
