package com.geekbrains.moviesearcher2.utils

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.geekbrains.moviesearcher2.R
import com.geekbrains.moviesearcher2.model.MovieDetails
import com.geekbrains.moviesearcher2.model.MovieDetailsInt
import com.geekbrains.moviesearcher2.model.room.HistoryEntity
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import kotlin.math.max

const val SERVER_ERROR = "Server error"
const val REQUEST_ERROR = "Request error"
const val CORRUPTED_DATA = "Corrupted data"
const val SHOW_ADULT_CONTENT = "SHOW_ADULT_CONTENT"
const val DATE_TIME_PATTERN = "dd.MM.yyyy, HH:mm:ss"

fun makeIPAddress(baseAddress: String, posterSize: String, posterPath: String) =
    String.format(baseAddress, posterSize, posterPath)

fun MovieDetails.convertGenresToString() =
    this.genres?.map { genres -> genres.name }?.toString()?.drop(1)?.dropLast(1) ?: ""

fun convertReleaseDateToYear(releaseDate: String?) =
    if (releaseDate == null) "" else "(${releaseDate.dropLast(6)})"

fun convertMovieDetailsToMovieDetailsInt(movieDetails: MovieDetails) =
    MovieDetailsInt(
        movieId = movieDetails.id ?: -1,
        title = movieDetails.title ?: "",
        posterPath = movieDetails.poster_path ?: "",
        tagLine = movieDetails.tagline ?: "",
        genres = movieDetails.convertGenresToString(),
        releaseDate = movieDetails.release_date ?: "",
        viewTime = -1,
        voteAverage = movieDetails.vote_average ?: 0.0,
        note = ""
    )

fun convertHistoryEntityToMovieDetailsInt(entityList: List<HistoryEntity>) =
    entityList.map {
        MovieDetailsInt(
            movieId = it.movieId,
            title = it.title,
            posterPath = it.posterPath,
            tagLine = it.tagLine,
            genres = it.genres,
            releaseDate = it.releaseDate,
            voteAverage = it.voteAverage,
            viewTime = it.viewTime,
            note = it.note
        )
    }

fun convertMovieDetailsIntToHistoryEntity(movieDetailsInt: MovieDetailsInt) =
    HistoryEntity(
        id = 0,
        movieId = movieDetailsInt.movieId,
        title = movieDetailsInt.title,
        posterPath = movieDetailsInt.posterPath,
        tagLine = movieDetailsInt.tagLine,
        genres = movieDetailsInt.genres,
        releaseDate = movieDetailsInt.releaseDate,
        voteAverage = movieDetailsInt.voteAverage,
        viewTime = movieDetailsInt.viewTime,
        note = movieDetailsInt.note
    )

fun convertMillisToDate(time: Long, pattern: String) =
    SimpleDateFormat(pattern).format(time)

fun loadFragment(fragment: Fragment, fragmentTag: String, manager: FragmentManager) = run {
    val fragmentPopped = manager.popBackStackImmediate(
        fragmentTag,
        0
    );
    if (!fragmentPopped &&
        manager.findFragmentByTag(fragmentTag) == null
    ) {
        manager.apply {
            beginTransaction()
                .replace(R.id.fragmentContainer, fragment)
                .addToBackStack(fragmentTag)
                .commitAllowingStateLoss()
        }
    }
    true
}

