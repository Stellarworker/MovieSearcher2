package com.geekbrains.moviesearcher2.view

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.geekbrains.moviesearcher2.R
import com.geekbrains.moviesearcher2.model.MovieDTO
import com.geekbrains.moviesearcher2.model.MoviesDTO

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
                findViewById<TextView>(R.id.mainFragmentRecyclerItemTitle).text =
                    movieDTO.title
                findViewById<TextView>(R.id.mainFragmentRecyclerItemReleaseDate).text =
                    String.format(
                        resources.getString(R.string.movieReleaseDateText),
                        movieDTO.release_date.toString().dropLast(6)
                    )
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
        movieData.results?.let { holder.bind(it[position]) }
    }

    override fun getItemCount() = movieData.results?.size ?: 0
}