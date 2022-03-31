package com.geekbrains.moviesearcher2.repository.local

import com.geekbrains.moviesearcher2.model.MovieDetailsInt

interface LocalRepository {
    fun getAllHistory(): List<MovieDetailsInt>
    fun updateNote(movieID: Int, note: String)
    fun saveDetails(movieDetailsInt: MovieDetailsInt)
    fun getNote(movieID: Int): String?
}