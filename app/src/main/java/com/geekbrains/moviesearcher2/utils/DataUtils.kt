package com.geekbrains.moviesearcher2.utils

const val SERVER_ERROR = "Server error"
const val REQUEST_ERROR = "Request error"
const val CORRUPTED_DATA = "Corrupted data"

fun makeIPAddress(baseAddress: String, posterSize: String, posterPath: String) =
    String.format(baseAddress, posterSize, posterPath)