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
import androidx.recyclerview.widget.GridLayoutManager
import com.geekbrains.moviesearcher2.R
import com.geekbrains.moviesearcher2.databinding.MainFragmentBinding
import com.geekbrains.moviesearcher2.model.MovieDTO
import com.geekbrains.moviesearcher2.utils.*
import com.geekbrains.moviesearcher2.view.details.DetailsFragment
import com.geekbrains.moviesearcher2.viewmodel.AppState
import com.geekbrains.moviesearcher2.viewmodel.DetailsViewModel
import com.geekbrains.moviesearcher2.viewmodel.MainViewModel

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

    private val adapter = MainFragmentAdapter(object : MainFragmentAdapter.OnItemViewClickListener {
        override fun onItemViewClick(movieDTO: MovieDTO) {
            movieDTO.id?.let {
                detailsViewModel.getMovieDetails(it)
            }
            activity?.supportFragmentManager?.let {
                loadFragment(DetailsFragment.newInstance(), DetailsFragment.FRAGMENT_TAG, it)
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
        binding.mfIncludedLoadingLayout.loadingLayout.hide()
        binding.mfRecyclerView.layoutManager = GridLayoutManager(context, 3)
        activity?.let {
            includeAdult =
                it.getPreferences(Context.MODE_PRIVATE).getBoolean(SHOW_ADULT_CONTENT, false)
        }
        mainViewModel.moviesLiveData.observe(viewLifecycleOwner) {
            renderData(it)
        }
        binding.apply {
            mfRecyclerView.adapter = adapter
            mfSearchButton.setOnClickListener {
                view.hideKeyboard()
                mfSearchLine.run {
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
            "" -> binding.root.makeSnackbar(
                text = getString(R.string.emptyRequestLabelText),
                anchor = activity?.findViewById(R.id.bottomNavigation)
            )
            else -> mainViewModel.getMovies(includeAdult, query)
        }
    }

    @RequiresApi(Build.VERSION_CODES.N)
    private fun renderData(appState: AppState) {
        when (appState) {
            is AppState.Success -> {
                binding.mfIncludedLoadingLayout.loadingLayout.hide()
                when (appState.movieData.results?.size) {
                    0 -> binding.root.makeSnackbar(
                        text = getString(R.string.nothingFoundLabelText),
                        anchor = activity?.findViewById(R.id.bottomNavigation)
                    )
                    else -> adapter.setMovie(appState.movieData)
                }
            }
            is AppState.Loading -> binding.mfIncludedLoadingLayout.loadingLayout.show()
            is AppState.Error -> {
                binding.mfIncludedLoadingLayout.loadingLayout.hide()
                binding.root.makeSnackbar(
                    text = appState.error.message ?: getString(R.string.errorLabelText),
                    actionText = getString(R.string.reloadLabelText),
                    anchor = activity?.findViewById(R.id.bottomNavigation),
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

    companion object {
        const val FRAGMENT_TAG = "MAIN_FRAGMENT"

        @JvmStatic
        fun newInstance() = MainFragment()
    }
}