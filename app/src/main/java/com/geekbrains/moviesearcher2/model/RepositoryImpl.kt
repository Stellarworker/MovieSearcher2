package com.geekbrains.moviesearcher2.model

class RepositoryImpl : Repository {

    override fun getMoviesFromServer(query: String) = getMoviesByQuery(query)

    private fun getMoviesByQuery(query: String) =
        getMoviesFromDatabase().filter {
            it.isMovieMatchQuery(query)
        }

    private fun Movie.isMovieMatchQuery(query: String) =
        title.lowercase().contains(query.lowercase()) ||
                genre.lowercase().contains(query.lowercase()) ||
                director.lowercase().contains(query.lowercase()) ||
                releaseYear.toString().lowercase().contains(query.lowercase()) ||
                userScore.toString().lowercase().contains(query.lowercase())

    private fun getMoviesFromDatabase() =
        listOf(
            Movie("The Terminator", "Action", "James Cameron", 1984, 76),
            Movie("First Blood", "Action", "Ted Kotcheff", 1982, 74),
            Movie("Armageddon", "Action", "Michael Bay", 1998, 68),
            Movie("2012", "Action", "Roland Emmerich", 2009, 58),
            Movie("Jumanji", "Adventure", "Joe Johnston", 1995, 75),
            Movie("Roman Holiday", "Romance", "William Wyler", 1953, 79),
            Movie("The Green Mile", "Fantasy", "Frank Darabont", 1999, 85),
            Movie("Cobra", "Action", "George P. Cosmatos", 1986, 60),
            Movie("Titanic", "Drama", "James Cameron", 1997, 79),
            Movie("The Day After Tomorrow", "Adventure", "Roland Emmerich", 2004, 65),
            Movie("Home Alone", "Comedy", "Chris Columbus", 1990, 74),
            Movie("Ruthless People", "Comedy", "Jerry Zucker", 1986, 66)
        )
}