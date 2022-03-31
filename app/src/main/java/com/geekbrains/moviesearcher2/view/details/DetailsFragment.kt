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
private const val POSTER_SIZE = "original"
private const val HIDE_ALL = 0
private const val SHOW_LOADING = 1
private const val SHOW_DATA = 2
private const val EMPTY_DETAIL = "none"

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
        binding.dfSaveNoteButton.setOnClickListener { button ->
            button.hideKeyboard()
            detailsViewModel.saveNoteToDb(binding.dfNoteContainer.text.toString())
            binding.root.makeSnackbar(
                text = getString(R.string.dfNoteSaved),
                anchor = activity?.findViewById(R.id.maBottomNavigation)
            )
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
                    text = appStateDetails.error.message ?: getString(R.string.errorLoadingData),
                    actionText = getString(R.string.reload),
                    anchor = activity?.findViewById(R.id.maBottomNavigation),
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
            dfMovieTitle.text = substituteIfBlank(details.title.trim(), EMPTY_DETAIL)
            dfMovieTaglineData.text = substituteIfBlank(details.tagLine.trim(), EMPTY_DETAIL)
            dfMovieGenreData.text = substituteIfBlank(details.genres.trim(), EMPTY_DETAIL)
            dfMovieReleaseDateData.text = substituteIfBlank(
                details.releaseDate.trim(),
                EMPTY_DETAIL
            )
            dfMovieVoteAverageData.text = substituteIfBlank(
                details.voteAverage.toString().trim(),
                EMPTY_DETAIL
            )
        }
        binding.dfNoteContainer.setText(details.note)
        binding.dfMoviePoster.load(
            makeIPAddress(
                resources.getString(R.string.baseMovieAddressString),
                POSTER_SIZE,
                details.posterPath
            )
        )
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
                    dfIncludedLoadingLayout.loadingLayout.hide()
                    dfDataContainer.hide()
                }
                SHOW_LOADING -> {
                    dfIncludedLoadingLayout.loadingLayout.show()
                    dfDataContainer.hide()
                }
                else -> {
                    dfIncludedLoadingLayout.loadingLayout.hide()
                    dfDataContainer.show()
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