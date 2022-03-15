package com.geekbrains.moviesearcher2.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Genres(
    val id: Int?,
    val name: String?
) : Parcelable

@Parcelize
data class BelongsToCollection(
    val id: Int?,
    val name: String?,
    val poster_path: String?,
    val backdrop_path: String?
) : Parcelable

@Parcelize
data class ProductionCompanies(
    val name: String?,
    val id: Int?,
    val logo_path: String?,
    val origin_country: String?
) : Parcelable

@Parcelize
data class ProductionCountries(
    val iso_3166_1: String?,
    val name: String?
) : Parcelable

@Parcelize
data class SpokenLanguages(
    val iso_639_1: String?,
    val name: String?
) : Parcelable

@Parcelize
data class MovieDetails(
    val adult: Boolean?,
    val backdrop_path: String?,
    val belongs_to_collection: BelongsToCollection?,
    val budget: Int?,
    val genres: List<Genres>?,
    val homepage: String?,
    val id: Int?,
    val imdb_id: String?,
    val original_language: String?,
    val original_title: String?,
    val overview: String?,
    val popularity: Double?,
    val poster_path: String?,
    val production_companies: List<ProductionCompanies>?,
    val production_countries: List<ProductionCountries>?,
    val release_date: String?,
    val revenue: Int?,
    val runtime: Int?,
    val spoken_languages: List<SpokenLanguages>?,
    val status: String?,
    val tagline: String?,
    val title: String?,
    val video: Boolean?,
    val vote_average: Double?,
    val vote_count: Int?
) : Parcelable