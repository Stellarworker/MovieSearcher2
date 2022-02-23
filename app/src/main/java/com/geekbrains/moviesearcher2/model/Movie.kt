package com.geekbrains.moviesearcher2.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Movie(
    val title: String,
    val genre: String,
    val director: String,
    val releaseYear: Int,
    val userScore: Int
) : Parcelable