package com.geekbrains.moviesearcher2.view.details

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.geekbrains.moviesearcher2.databinding.FragmentDetailsBinding
import com.geekbrains.moviesearcher2.model.MovieDetails
import com.geekbrains.moviesearcher2.viewmodel.DetailsViewModel

class DetailsFragment : Fragment() {
    private var _binding: FragmentDetailsBinding? = null
    private val binding get() = _binding!!
    private val detailsViewModel: DetailsViewModel by lazy {
        ViewModelProvider(requireActivity())[DetailsViewModel::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDetailsBinding.inflate(inflater, container, false)
        return binding.root
    }

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        detailsViewModel.getLiveData().observe(viewLifecycleOwner) {
            renderData(it)
        }
    }

    private fun renderData(details: MovieDetails) {
        with(binding) {
            movieTitleDataText.text = details.title
            movieTaglineDataText.text = details.tagline
            movieGenreDataText.text = details.genres?.map { genres -> genres.name }
                .toString().drop(1).dropLast(1)
            movieReleaseDateDataText.text = details.release_date
            movieVoteAverageDataText.text = details.vote_average.toString()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        fun newInstance(bundle: Bundle) = DetailsFragment().apply {
            arguments = bundle
        }
    }
}