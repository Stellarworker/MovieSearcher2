package com.geekbrains.moviesearcher2.view.history

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.geekbrains.moviesearcher2.R
import com.geekbrains.moviesearcher2.databinding.FragmentHistoryBinding
import com.geekbrains.moviesearcher2.utils.hide
import com.geekbrains.moviesearcher2.utils.makeSnackbar
import com.geekbrains.moviesearcher2.utils.show
import com.geekbrains.moviesearcher2.view.settings.SettingsFragment
import com.geekbrains.moviesearcher2.viewmodel.AppStateHistory
import com.geekbrains.moviesearcher2.viewmodel.HistoryViewModel

class HistoryFragment : Fragment() {

    private var _binding: FragmentHistoryBinding? = null
    private val binding get() = _binding!!

    private val viewModel: HistoryViewModel by lazy {
        ViewModelProvider(requireActivity())[HistoryViewModel::class.java]
    }

    private val adapter: HistoryAdapter by lazy {
        HistoryAdapter()
    }

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
        binding.historyFragmentRecyclerView.adapter = adapter
        viewModel.historyLiveData.observe(viewLifecycleOwner, Observer {
            renderData(it)
        })
        viewModel.getAllHistory()
    }

    private fun renderData(appState: AppStateHistory) {
        when (appState) {
            is AppStateHistory.Success -> {
                with(binding) {
                    historyFragmentRecyclerView.show()
                    hFIncludedLoadingLayout.loadingLayout.hide()
                }
                adapter.setData(appState.movieDetailsInt)
            }

            is AppStateHistory.Loading -> {
                with(binding) {
                    historyFragmentRecyclerView.hide()
                    hFIncludedLoadingLayout.loadingLayout.show()
                }
            }
            is AppStateHistory.Error -> {
                with(binding) {
                    historyFragmentRecyclerView.show()
                    hFIncludedLoadingLayout.loadingLayout.hide()
                    historyFragmentRecyclerView.makeSnackbar(
                        text = getString(R.string.errorLabelText),
                        actionText = getString(R.string.reloadLabelText),
                        action = {
                            viewModel.getAllHistory()
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
        @JvmStatic
        fun newInstance() = HistoryFragment()
    }
}