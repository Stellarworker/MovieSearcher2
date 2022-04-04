package com.geekbrains.moviesearcher22.view.history

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.geekbrains.moviesearcher22.R
import com.geekbrains.moviesearcher22.databinding.FragmentHistoryBinding
import com.geekbrains.moviesearcher22.model.MovieDetailsInt
import com.geekbrains.moviesearcher22.utils.hide
import com.geekbrains.moviesearcher22.utils.loadFragment
import com.geekbrains.moviesearcher22.utils.makeSnackbar
import com.geekbrains.moviesearcher22.utils.show
import com.geekbrains.moviesearcher22.view.details.DetailsFragment
import com.geekbrains.moviesearcher22.viewmodel.AppStateHistory
import com.geekbrains.moviesearcher22.viewmodel.DetailsViewModel
import com.geekbrains.moviesearcher22.viewmodel.HistoryViewModel

class HistoryFragment : Fragment() {

    private var _binding: FragmentHistoryBinding? = null
    private val binding get() = _binding!!

    private val historyViewModel: HistoryViewModel by lazy {
        ViewModelProvider(requireActivity())[HistoryViewModel::class.java]
    }

    private val detailsViewModel: DetailsViewModel by lazy {
        ViewModelProvider(requireActivity())[DetailsViewModel::class.java]
    }

    private val adapter = HistoryAdapter(
        object : HistoryAdapter.OnItemViewClickListener {
            override fun onItemViewClick(movieDetailsInt: MovieDetailsInt) {
                detailsViewModel.getMovieDetails(movieDetailsInt.movieId)
                activity?.supportFragmentManager?.let {
                    loadFragment(DetailsFragment.newInstance(), DetailsFragment.FRAGMENT_TAG, it)
                }
            }
        })

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHistoryBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.hfRecyclerView.adapter = adapter
        historyViewModel.historyLiveData.observe(viewLifecycleOwner, Observer {
            renderData(it)
        })
        historyViewModel.getAllHistory()
    }

    private fun renderData(appState: AppStateHistory) {
        when (appState) {
            is AppStateHistory.Success -> {
                with(binding) {
                    hfDataContainer.show()
                    hfIncludedLoadingLayout.loadingLayout.hide()
                }
                adapter.setData(appState.movieDetailsInt)
            }

            is AppStateHistory.Loading -> {
                with(binding) {
                    hfDataContainer.hide()
                    hfIncludedLoadingLayout.loadingLayout.show()
                }
            }
            is AppStateHistory.Error -> {
                with(binding) {
                    hfDataContainer.show()
                    hfIncludedLoadingLayout.loadingLayout.hide()
                    hfRecyclerView.makeSnackbar(
                        text = getString(R.string.errorLoadingData),
                        actionText = getString(R.string.reload),
                        anchor = activity?.findViewById(R.id.maBottomNavigation),
                        action = {
                            historyViewModel.getAllHistory()
                        }
                    )
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        const val FRAGMENT_TAG = "HISTORY_FRAGMENT"

        @JvmStatic
        fun newInstance() = HistoryFragment()
    }
}