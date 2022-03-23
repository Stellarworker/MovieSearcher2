package com.geekbrains.moviesearcher2.view.history

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.geekbrains.moviesearcher2.R
import com.geekbrains.moviesearcher2.model.MovieDetailsInt
import com.geekbrains.moviesearcher2.utils.DATE_TIME_PATTERN
import com.geekbrains.moviesearcher2.utils.convertMillisToDate
import com.geekbrains.moviesearcher2.utils.convertReleaseDateToYear
import com.geekbrains.moviesearcher2.utils.show

class HistoryAdapter : RecyclerView.Adapter<HistoryAdapter.RecyclerItemViewHolder>() {
    private var data: List<MovieDetailsInt> = arrayListOf()

    fun setData(data: List<MovieDetailsInt>) {
        this.data = data
        notifyDataSetChanged()
    }

    inner class RecyclerItemViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        fun bind(data: MovieDetailsInt) {
            if (layoutPosition != RecyclerView.NO_POSITION) {
                with(itemView) {
                    findViewById<TextView>(R.id.hFRecyclerItemTitle).text =
                        String.format(
                            resources.getString(R.string.historyItemTitleText),
                            data.title
                        )
                    findViewById<TextView>(R.id.hFRecyclerItemReleaseDate).text =
                        String.format(
                            resources.getString(R.string.historyItemYearText),
                            convertReleaseDateToYear(data.releaseDate).drop(1).dropLast(1)
                        )
                    findViewById<TextView>(R.id.hFRecyclerItemTime).text =
                        String.format(
                            resources.getString(R.string.historyItemViewTimeText),
                            convertMillisToDate(
                                data.viewTime, DATE_TIME_PATTERN
                            )
                        )
                    if (data.note != "") {
                        findViewById<TextView>(R.id.hFRecyclerItemNote).apply {
                            text = String.format(
                                resources.getString(R.string.historyItemNoteText),
                                data.note
                            )
                            show()
                        }
                    }
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerItemViewHolder {
        return RecyclerItemViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.fragment_history_recycler_item, parent, false) as View
        )
    }

    override fun onBindViewHolder(holder: RecyclerItemViewHolder, position: Int) {
        holder.bind(data[position])
    }

    override fun getItemCount() = data.size
}