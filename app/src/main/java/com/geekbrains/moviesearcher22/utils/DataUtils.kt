package com.geekbrains.moviesearcher22.utils

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.geekbrains.moviesearcher22.R
import com.geekbrains.moviesearcher22.common.*
import com.geekbrains.moviesearcher22.model.MovieDetails
import com.geekbrains.moviesearcher22.model.MovieDetailsInt
import com.geekbrains.moviesearcher22.model.room.HistoryEntity
import java.text.SimpleDateFormat

private const val DROP_FOR_GENRES = 1
private const val DROP_FOR_YEAR = 6

fun makeIPAddress(baseAddress: String, posterSize: String, posterPath: String) =
    String.format(baseAddress, posterSize, posterPath)

fun MovieDetails.convertGenresToString() =
    this.genres?.map { genres -> genres.name }?.toString()?.drop(DROP_FOR_GENRES)
        ?.dropLast(DROP_FOR_GENRES) ?: EMPTY_STRING

fun convertReleaseDateToYear(releaseDate: String?) =
    if (releaseDate == null) EMPTY_STRING else "(${releaseDate.dropLast(DROP_FOR_YEAR)})"

fun convertMovieDetailsToMovieDetailsInt(movieDetails: MovieDetails) =
    MovieDetailsInt(
        movieId = movieDetails.id ?: EMPTY_INT,
        title = movieDetails.title ?: EMPTY_STRING,
        posterPath = movieDetails.poster_path ?: EMPTY_STRING,
        tagLine = movieDetails.tagline ?: EMPTY_STRING,
        genres = movieDetails.convertGenresToString(),
        releaseDate = movieDetails.release_date ?: EMPTY_STRING,
        viewTime = EMPTY_LONG,
        voteAverage = movieDetails.vote_average ?: EMPTY_DOUBLE,
        note = EMPTY_STRING
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
        id = ZERO_LONG,
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
    fragmentFlags: Int = ZERO_INT
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