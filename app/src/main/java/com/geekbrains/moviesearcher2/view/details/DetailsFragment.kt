package com.geekbrains.moviesearcher2.view.details

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.geekbrains.moviesearcher2.databinding.FragmentDetailsBinding
import com.geekbrains.moviesearcher2.model.Movie


class DetailsFragment : Fragment() {
    private var _binding: FragmentDetailsBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDetailsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        arguments?.getParcelable<Movie>(BUNDLE_EXTRA)?.let {
            with(binding) {
                movieTitleDataText.text = it.title
                movieDirectorDataText.text = it.director
                movieGenreDataText.text = it.genre
                movieReleaseYearDataText.text = it.releaseYear.toString()
                movieUserScoreDataText.text = it.userScore.toString()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        const val BUNDLE_EXTRA = "movie"
        fun newInstance(bundle: Bundle) = DetailsFragment().apply {
            arguments = bundle
        }
    }
}