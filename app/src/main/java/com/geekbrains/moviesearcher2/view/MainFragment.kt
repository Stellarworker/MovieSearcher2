package com.geekbrains.moviesearcher2.view

import android.content.Context
import android.os.Build
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import com.geekbrains.moviesearcher2.R
import com.geekbrains.moviesearcher2.databinding.MainFragmentBinding
import com.geekbrains.moviesearcher2.model.MovieDTO
import com.geekbrains.moviesearcher2.utils.ALLOW_ADULT_CONTENT
import com.geekbrains.moviesearcher2.view.details.DetailsFragment
import com.geekbrains.moviesearcher2.viewmodel.AppState
import com.geekbrains.moviesearcher2.viewmodel.DetailsViewModel
import com.geekbrains.moviesearcher2.viewmodel.MainViewModel
import com.geekbrains.moviesearcher2.utils.makeSnackbar
import com.geekbrains.moviesearcher2.utils.show
import com.geekbrains.moviesearcher2.utils.hide

@RequiresApi(Build.VERSION_CODES.N)
class MainFragment : Fragment() {

    private var _binding: MainFragmentBinding? = null
    private val binding get() = _binding!!
    private val mainViewModel: MainViewModel by lazy {
        ViewModelProvider(requireActivity())[MainViewModel::class.java]
    }
    private val detailsViewModel: DetailsViewModel by lazy {
        ViewModelProvider(requireActivity())[DetailsViewModel::class.java]
    }
    private var query: String = ""
    private var includeAdult = false

    companion object {
        fun newInstance() = MainFragment()
    }

    private val adapter = MainFragmentAdapter(object : MainFragmentAdapter.OnItemViewClickListener {
        override fun onItemViewClick(movieDTO: MovieDTO) {
            movieDTO.id?.let {
                detailsViewModel.getMovieDetails(it)
            }
            activity?.supportFragmentManager?.apply {
                beginTransaction()
                    .replace(R.id.container, DetailsFragment.newInstance(Bundle()))
                    .addToBackStack("MainFragment")
                    .commitAllowingStateLoss()
            }
        }
    })

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = MainFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.mFIncludedLoadingLayout.loadingLayout.hide()
        activity?.let {
            includeAdult =
                it.getPreferences(Context.MODE_PRIVATE).getBoolean(ALLOW_ADULT_CONTENT, false)
        }
        mainViewModel.moviesLiveData.observe(viewLifecycleOwner) {
            renderData(it)
        }
        binding.apply {
            mainFragmentRecyclerView.adapter = adapter
            searchButton.setOnClickListener {
                searchLine.run {
                    query = text.toString().trim()
                    startSearching(query)
                    text?.clear()
                }
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.N)
    private fun startSearching(query: String) {
        when (query) {
            "" -> binding.root.makeSnackbar(text = getString(R.string.emptyRequestLabelText))
            else -> mainViewModel.getMovies(includeAdult, query)
        }
    }

    @RequiresApi(Build.VERSION_CODES.N)
    private fun renderData(appState: AppState) {
        when (appState) {
            is AppState.Success -> {
                binding.mFIncludedLoadingLayout.loadingLayout.hide()
                when (appState.movieData.results?.size) {
                    0 -> binding.root.makeSnackbar(
                        text = getString(R.string.nothingFoundLabelText)
                    )
                    else -> adapter.setMovie(appState.movieData)
                }
            }
            is AppState.Loading -> binding.mFIncludedLoadingLayout.loadingLayout.show()
            is AppState.Error -> {
                binding.mFIncludedLoadingLayout.loadingLayout.hide()
                binding.root.makeSnackbar(
                    text = appState.error.message ?: getString(R.string.errorLabelText),
                    actionText = getString(R.string.reloadLabelText),
                    action = {
                        mainViewModel.getMovies(includeAdult, query)
                    })
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}