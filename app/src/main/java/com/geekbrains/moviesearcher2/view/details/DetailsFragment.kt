package com.geekbrains.moviesearcher2.view.details

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import coil.load
import com.geekbrains.moviesearcher2.R
import com.geekbrains.moviesearcher2.databinding.FragmentDetailsBinding
import com.geekbrains.moviesearcher2.model.MovieDetails
import com.geekbrains.moviesearcher2.utils.hide
import com.geekbrains.moviesearcher2.utils.makeIPAddress
import com.geekbrains.moviesearcher2.utils.makeSnackbar
import com.geekbrains.moviesearcher2.utils.show
import com.geekbrains.moviesearcher2.viewmodel.AppStateDetails
import com.geekbrains.moviesearcher2.viewmodel.DetailsViewModel

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
private const val POSTER_SIZE = "w342"
private const val HIDE_ALL = 0
private const val SHOW_LOADING = 1
private const val SHOW_DATA = 2


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
        detailsViewModel.movieDetailsLiveData.observe(viewLifecycleOwner) {
            renderData(it)
        }
    }

    private fun renderData(appStateDetails: AppStateDetails) {
        when (appStateDetails) {
            is AppStateDetails.Success -> {
                setDetails(appStateDetails.movieDetails)
                showData()
            }
            is AppStateDetails.Loading -> showLoading()
            is AppStateDetails.Error -> {
                binding.root.makeSnackbar(
                    text = appStateDetails.error.message ?: getString(R.string.errorLabelText),
                    actionText = getString(R.string.reloadLabelText),
                    action = {
                        detailsViewModel.getMovieDetails(detailsViewModel.getMovieID())
                    })
            }
        }
    }

    private fun setDetails(details: MovieDetails) {
        with(binding) {
            movieTitleDataText.text = details.title
            movieTaglineDataText.text = details.tagline
            movieGenreDataText.text = details.genres?.map { genres -> genres.name }
                .toString().drop(1).dropLast(1)
            movieReleaseDateDataText.text = details.release_date
            movieVoteAverageDataText.text = details.vote_average.toString()
        }

        details.poster_path?.let {
            binding.movieDetailsPoster.load(
                makeIPAddress(
                    resources.getString(R.string.baseMovieAddressString),
                    POSTER_SIZE,
                    it
                )
            )
        }
        showData()
    }

    private fun hideAll() {
        setVisibleMode(HIDE_ALL)
    }

    private fun showLoading() {
        setVisibleMode(SHOW_LOADING)
    }

    private fun showData() {
        setVisibleMode(SHOW_DATA)
    }

    private fun setVisibleMode(mode: Int) {
        with(binding) {
            when (mode) {
                HIDE_ALL -> {
                    loadingLayoutDetails.hide()
                    movieDetailsScrollContainer.hide()
                }
                SHOW_LOADING -> {
                    loadingLayoutDetails.show()
                    movieDetailsScrollContainer.hide()
                }
                else -> {
                    loadingLayoutDetails.hide()
                    movieDetailsScrollContainer.show()
                }
            }
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