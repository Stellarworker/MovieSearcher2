package com.geekbrains.moviesearcher22.config

import com.geekbrains.moviesearcher22.BuildConfig

const val DEBUG_MODE = (BuildConfig.BUILD_TYPE == "debug")
const val DB_NAME = "History.db"
/**
 * poster_sizes
 * 0   "w92"
 * 1   "w154"
 * 2   "w185"
 * 3   "w342"
 * 4   "w500"
 * 5   "w780"
 * 6   "original"
 */
const val MAIN_POSTER_SIZE = "w500"
const val DETAILS_POSTER_SIZE = "original"