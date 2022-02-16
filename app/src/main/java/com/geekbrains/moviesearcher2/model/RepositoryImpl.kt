package com.geekbrains.moviesearcher2.model

class RepositoryImpl : Repository {
    override fun getMoviesFromServer(query: String) =
        listOf(
            Movie("The Terminator", "Action", 1984, 76),
            Movie("The Matrix", "Action", 1999, 82),
            Movie("Armageddon", "Action", 1998, 68),
            Movie("2012", "Action", 2009, 58),
            Movie("Jumanji", "Adventure", 1995, 75),
            Movie("Roman Holiday", "Romance", 1953, 79),
            Movie("The Green Mile", "Fantasy", 1999, 85),
        )
}