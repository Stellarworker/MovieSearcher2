package com.geekbrains.moviesearcher22.view

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.geekbrains.moviesearcher22.R
import com.geekbrains.moviesearcher22.config.MAIN_POSTER_SIZE
import com.geekbrains.moviesearcher22.model.MovieDTO
import com.geekbrains.moviesearcher22.model.MoviesDTO
import com.geekbrains.moviesearcher22.utils.convertReleaseDateToYear
import com.geekbrains.moviesearcher22.utils.makeIPAddress

class MainFragmentAdapter(private var onItemViewClickListener: OnItemViewClickListener?) :
    RecyclerView.Adapter<MainFragmentAdapter.MainViewHolder>() {
    private var movieData: MoviesDTO = MoviesDTO(0, listOf())

    fun setMovie(data: MoviesDTO) {
        movieData = data
        notifyDataSetChanged()
    }

    inner class MainViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        fun bind(movieDTO: MovieDTO) {
            with(itemView) {
                findViewById<TextView>(R.id.mfRecyclerViewMovieTitle).text =
                    movieDTO.title
                findViewById<TextView>(R.id.mfRecyclerViewMovieYear).text =
                    convertReleaseDateToYear(movieDTO.release_date)
                if (!movieDTO.poster_path.isNullOrBlank()) {
                    findViewById<ImageView>(R.id.mfRecyclerViewMoviePoster).load(
                        makeIPAddress(
                            resources.getString(R.string.baseMovieAddressString),
                            MAIN_POSTER_SIZE,
                            movieDTO.poster_path
                        )
                    )
                }

                setOnClickListener {
                    onItemViewClickListener?.onItemViewClick(movieDTO)
                }
            }
        }
    }

    interface OnItemViewClickListener {
        fun onItemViewClick(movieDTO: MovieDTO)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        MainViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.fragment_main_recycler_item,
                parent, false
            ) as View
        )

    override fun onBindViewHolder(holder: MainViewHolder, position: Int) {
        movieData.results?.let { list ->
            holder.bind(list[position])
        }
    }

    override fun getItemCount() = movieData.results?.size ?: 0
}