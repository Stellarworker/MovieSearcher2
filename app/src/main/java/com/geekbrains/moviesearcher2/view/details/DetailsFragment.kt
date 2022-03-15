package com.geekbrains.moviesearcher2.view.details

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.geekbrains.moviesearcher2.R
import com.geekbrains.moviesearcher2.databinding.FragmentDetailsBinding
import com.geekbrains.moviesearcher2.model.MovieDetails
import com.geekbrains.moviesearcher2.viewmodel.DetailsViewModel
import kotlinx.android.synthetic.main.fragment_details.view.*
import com.geekbrains.moviesearcher2.makeSnackbar
import com.geekbrains.moviesearcher2.show
import com.geekbrains.moviesearcher2.hide
import com.google.android.material.snackbar.Snackbar

const val DETAILS_INTENT_FILTER = "DETAILS INTENT FILTER"
const val DETAILS_EXTRA = "DETAILS"
const val DETAILS_LOAD_RESULT_EXTRA = "LOAD RESULT"
const val DETAILS_INTENT_EMPTY_EXTRA = "INTENT IS EMPTY"
const val DETAILS_DATA_EMPTY_EXTRA = "DATA IS EMPTY"
const val DETAILS_RESPONSE_EMPTY_EXTRA = "RESPONSE IS EMPTY"
const val DETAILS_REQUEST_ERROR_EXTRA = "REQUEST ERROR"
const val DETAILS_REQUEST_ERROR_MESSAGE_EXTRA = "REQUEST ERROR MESSAGE"
const val DETAILS_URL_MALFORMED_EXTRA = "URL MALFORMED"
const val DETAILS_RESPONSE_SUCCESS_EXTRA = "RESPONSE SUCCESS"
const val ID_EXTRA = "ID"
private const val PROCESS_ERROR = "Error processing"

class DetailsFragment : Fragment() {
    private var _binding: FragmentDetailsBinding? = null
    private val binding get() = _binding!!
    private val detailsViewModel: DetailsViewModel by lazy {
        ViewModelProvider(requireActivity())[DetailsViewModel::class.java]
    }

    private val loadResultsReceiver: BroadcastReceiver = object : BroadcastReceiver() {
        @RequiresApi(Build.VERSION_CODES.N)
        override fun onReceive(context: Context, intent: Intent) {
            when (val result = intent.getStringExtra(DETAILS_LOAD_RESULT_EXTRA)) {
                DETAILS_RESPONSE_SUCCESS_EXTRA -> intent.getParcelableExtra<MovieDetails>(
                    DETAILS_EXTRA
                )?.let {
                    renderData(it)
                }
                else -> {
                    binding.root.makeSnackbar(
                        text = "Error: ${result.toString()}",
                        actionText = getString(R.string.reloadLabelText),
                        length = Snackbar.LENGTH_INDEFINITE,
                        action = {
                            detailsViewModel.getLiveData().value?.let {
                                getDetails(it)
                            }
                        }
                    )
                }
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        context?.let {
            LocalBroadcastManager.getInstance(it)
                .registerReceiver(loadResultsReceiver, IntentFilter(DETAILS_INTENT_FILTER))
        }
    }

    override fun onDestroy() {
        context?.let {
            LocalBroadcastManager.getInstance(it).unregisterReceiver(loadResultsReceiver)
        }
        super.onDestroy()
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
            getDetails(it)
        }
    }

    @RequiresApi(Build.VERSION_CODES.N)
    private fun getDetails(id: Int) {
        context?.let {
            it.startService(Intent(it, DetailsService::class.java).apply {
                putExtra(ID_EXTRA, id)
            })
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
        showData()
    }

    private fun hideAll() {
        setVisibleMode(0)
    }

    private fun showLoading() {
        setVisibleMode(1)
    }

    private fun showData() {
        setVisibleMode(2)
    }

    private fun setVisibleMode(mode: Int) {
        with(binding.root) {
            when (mode) {
                0 -> {
                    loadingLayoutDetails.hide()
                    movieTitleDataText.hide()
                    movieInfoTable.hide()
                }
                1 -> {
                    loadingLayoutDetails.show()
                    movieTitleDataText.hide()
                    movieInfoTable.hide()
                }
                else -> {
                    loadingLayoutDetails.hide()
                    movieTitleDataText.show()
                    movieInfoTable.show()
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