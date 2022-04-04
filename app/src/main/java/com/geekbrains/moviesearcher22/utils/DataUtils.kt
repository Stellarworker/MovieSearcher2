package com.geekbrains.moviesearcher22.utils

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.geekbrains.moviesearcher22.R
import com.geekbrains.moviesearcher22.model.MovieDetails
import com.geekbrains.moviesearcher22.model.MovieDetailsInt
import com.geekbrains.moviesearcher22.model.room.HistoryEntity
import java.text.SimpleDateFormat

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
    entityList.map { history ->
        MovieDetailsInt(
            movieId = history.movieId,
            title = history.title,
            posterPath = history.posterPath,
            tagLine = history.tagLine,
            genres = history.genres,
            releaseDate = history.releaseDate,
            voteAverage = history.voteAverage,
            viewTime = history.viewTime,
            note = history.note
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

fun loadFragment(
    fragment: Fragment,
    fragmentTag: String,
    manager: FragmentManager,
    containerID: Int = R.id.maFragmentContainer,
    fragmentFlags: Int = 0
) = run {
    val fragmentPopped = manager.popBackStackImmediate(fragmentTag, fragmentFlags)
    if (!fragmentPopped && manager.findFragmentByTag(fragmentTag) == null) {
        manager.apply {
            beginTransaction()
                .replace(containerID, fragment)
                .addToBackStack(fragmentTag)
                .commitAllowingStateLoss()
        }
    }
    true
}

fun substituteIfBlank(mainString: String, substString: String) =
    mainString.ifBlank { substString }