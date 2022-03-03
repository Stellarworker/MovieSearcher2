package com.geekbrains.moviesearcher2.view

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
import com.geekbrains.moviesearcher2.model.MovieDetails
import com.geekbrains.moviesearcher2.view.details.DetailsFragment
import com.geekbrains.moviesearcher2.view.details.DetailsLoader
import com.geekbrains.moviesearcher2.viewmodel.AppState
import com.geekbrains.moviesearcher2.viewmodel.DetailsViewModel
import com.geekbrains.moviesearcher2.viewmodel.MainViewModel
import com.google.android.material.snackbar.Snackbar

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

    companion object {
        fun newInstance() = MainFragment()
    }

    private val adapter = MainFragmentAdapter(object : MainFragmentAdapter.OnItemViewClickListener {
        override fun onItemViewClick(movieDTO: MovieDTO) {
            val onLoadListener: DetailsLoader.DetailsLoaderListener =
                object : DetailsLoader.DetailsLoaderListener {
                    override fun onLoaded(details: MovieDetails) {
                        binding.loadingLayout.hide()
                        detailsViewModel.postMovie(details)
                        activity?.supportFragmentManager?.apply {
                            beginTransaction()
                                .replace(R.id.container, DetailsFragment.newInstance(Bundle()))
                                .addToBackStack("MainFragment")
                                .commitAllowingStateLoss()
                        }
                    }

                    override fun onFailed(throwable: Throwable) {
                        with(binding) {
                            loadingLayout.hide()
                            root.makeSnackbar(text = getString(R.string.errorLabelText))
                        }
                    }
                }
            val loader = movieDTO.id?.let { DetailsLoader(onLoadListener, it) }
            loader?.run { loadDetails() }
            binding.loadingLayout.show()
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
        mainViewModel.getLiveData().observe(viewLifecycleOwner) {
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

    // Метод обрабатывает поисковый запрос пользователя.
    @RequiresApi(Build.VERSION_CODES.N)
    private fun startSearching(searchText: String) {
        when (searchText) {
            "" -> binding.root.makeSnackbar(text = getString(R.string.emptyRequestLabelText))
            else -> mainViewModel.getMovies(searchText)
        }
    }

    @RequiresApi(Build.VERSION_CODES.N)
    private fun renderData(appState: AppState) {
        when (appState) {
            is AppState.Success -> {
                binding.loadingLayout.hide()
                when (appState.movieData.results?.size) {
                    0 -> binding.root.makeSnackbar(
                        text = getString(R.string.nothingFoundLabelText)
                    )
                    else -> adapter.setMovie(appState.movieData)
                }
            }
            is AppState.Loading -> binding.loadingLayout.show()
            is AppState.Error -> {
                try {
                    throw Exception(appState.error)
                } catch (e: Throwable) {
                    binding.loadingLayout.hide()
                    binding.root.makeSnackbar(
                        text = getString(R.string.errorLabelText),
                        actionText = getString(R.string.reloadLabelText),
                        action = {
                            mainViewModel.getMovies(query)
                        })
                }
            }
        }
    }

    private fun View.makeSnackbar(
        text: String = "",
        actionText: String = "",
        action: (View) -> Unit = {},
        length: Int = Snackbar.LENGTH_LONG
    ) = also {
        Snackbar.make(this, text, length).setAction(actionText, action).show()
    }

    private fun View.show() = apply {
        visibility = View.VISIBLE
    }

    private fun View.hide() = apply {
        visibility = View.GONE
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}