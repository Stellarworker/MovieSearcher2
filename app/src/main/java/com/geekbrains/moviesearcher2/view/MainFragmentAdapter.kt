package com.geekbrains.moviesearcher2.view

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.geekbrains.moviesearcher2.R
import com.geekbrains.moviesearcher2.model.Movie

class MainFragmentAdapter(private var onItemViewClickListener: OnItemViewClickListener?) :
    RecyclerView.Adapter<MainFragmentAdapter.MainViewHolder>() {

    private var movieData: List<Movie> = listOf()

    fun setMovie(data: List<Movie>) {
        movieData = data
        notifyDataSetChanged()
    }

    fun removeListener() {
        onItemViewClickListener = null
    }

    inner class MainViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        fun bind(movie: Movie) {
            with (itemView) {
                findViewById<TextView>(R.id.mainFragmentRecyclerItemTitle).text =
                    movie.title
                findViewById<TextView>(R.id.mainFragmentRecyclerItemYear).text =
                    String.format(
                        resources.getString(R.string.movieYearText),
                        movie.releaseYear.toString()
                    )
                setOnClickListener {
                    onItemViewClickListener?.onItemViewClick(movie)
                }
            }
        }
    }

    interface OnItemViewClickListener {
        fun onItemViewClick(movie: Movie)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
         MainViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.fragment_main_recycler_item,
                parent, false
            ) as View
         )


    override fun onBindViewHolder(holder: MainViewHolder, position: Int) {
        holder.bind(movieData[position])
    }

    override fun getItemCount() = movieData.size
}