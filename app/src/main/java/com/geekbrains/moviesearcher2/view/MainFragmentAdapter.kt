package com.geekbrains.moviesearcher2.view

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.geekbrains.moviesearcher2.R
import com.geekbrains.moviesearcher2.model.MovieDTO
import com.geekbrains.moviesearcher2.model.MoviesDTO
import com.geekbrains.moviesearcher2.utils.makeIPAddress

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
private const val POSTER_SIZE = "w185"

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
                findViewById<TextView>(R.id.mainFragmentRecyclerItemTitle).text = movieDTO.title
                findViewById<TextView>(R.id.mainFragmentRecyclerItemReleaseDate).text =
                    String.format(
                        resources.getString(R.string.movieReleaseDateText),
                        movieDTO.release_date.toString().dropLast(6)
                    )
                movieDTO.poster_path?.let {
                    findViewById<ImageView>(R.id.moviePoster).load(
                        makeIPAddress(
                            resources.getString(R.string.baseMovieAddressString),
                            POSTER_SIZE,
                            it
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
        movieData.results?.let {
            holder.bind(it[position])
        }
    }

    override fun getItemCount() = movieData.results?.size ?: 0

}