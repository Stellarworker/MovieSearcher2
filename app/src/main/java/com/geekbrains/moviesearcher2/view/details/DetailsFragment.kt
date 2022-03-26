package com.geekbrains.moviesearcher2.view.details

import android.os.Build
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import coil.load
import com.geekbrains.moviesearcher2.R
import com.geekbrains.moviesearcher2.databinding.FragmentDetailsBinding
import com.geekbrains.moviesearcher2.model.MovieDetailsInt
import com.geekbrains.moviesearcher2.utils.*
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
        binding.noteContainer.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                detailsViewModel.saveNoteToDb(s.toString())
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }
        })
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
                    anchor = activity?.findViewById(R.id.bottomNavigation),
                    action = {
                        detailsViewModel.getMovieDetails(detailsViewModel.getMovieID())
                    }
                )
            }
        }
    }

    private fun setDetails(details: MovieDetailsInt) {
        saveMovieDetailsInt(details)
        with(binding) {
            movieTitleDataText.text = details.title
            movieTaglineDataText.text = details.tagLine
            movieGenreDataText.text = details.genres
            movieReleaseDateDataText.text = details.releaseDate
            movieVoteAverageDataText.text = details.voteAverage.toString()
        }
        binding.movieDetailsPoster.load(
            makeIPAddress(
                resources.getString(R.string.baseMovieAddressString),
                POSTER_SIZE,
                details.posterPath
            )
        )
        binding.noteContainer.setText(details.note)
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
                    dFIncludedLoadingLayout.loadingLayout.hide()
                    movieDetailsScrollContainer.hide()
                }
                SHOW_LOADING -> {
                    dFIncludedLoadingLayout.loadingLayout.show()
                    movieDetailsScrollContainer.hide()
                }
                else -> {
                    dFIncludedLoadingLayout.loadingLayout.hide()
                    movieDetailsScrollContainer.show()
                }
            }
        }
    }

    private fun saveMovieDetailsInt(movieDetailsInt: MovieDetailsInt) {
        detailsViewModel.saveMovieDetailsIntToDB(movieDetailsInt)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


    companion object {
        const val FRAGMENT_TAG = "DETAILS_FRAGMENT"

        @JvmStatic
        fun newInstance() = DetailsFragment()
    }
}