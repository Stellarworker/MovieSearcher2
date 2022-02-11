package com.geekbrains.moviesearcher2.view

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import com.geekbrains.moviesearcher2.R
import com.geekbrains.moviesearcher2.databinding.MainFragmentBinding
import com.geekbrains.moviesearcher2.model.Movie
import com.geekbrains.moviesearcher2.viewmodel.AppState
import com.geekbrains.moviesearcher2.viewmodel.MainViewModel
import com.google.android.material.snackbar.Snackbar

class MainFragment : Fragment() {

    private var _binding: MainFragmentBinding? = null
    private val binding get() = _binding!!

    companion object {
        fun newInstance() = MainFragment()
    }

    private lateinit var viewModel: MainViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = MainFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(MainViewModel::class.java)
        val observer = Observer<AppState> {
            renderData(it)
        }
        viewModel.getLiveData().observe(viewLifecycleOwner, observer)
        binding.searchButton.setOnClickListener {
            startSearching(binding.searchLine.text.toString().trim())
            binding.searchLine.setText("")  // Очистка поисковой строки.
        }
    }

    // Метод обрабатывает поисковый запрос пользователя.
    private fun startSearching(searchText: String) {
        if (searchText != "") {
            binding.movieTable.visibility = View.GONE
            viewModel.getMovieFromLocalSource()
        } else {
            Snackbar.make(
                binding.mainFragment, getString(R.string.onEmptyRequest),
                Snackbar.LENGTH_LONG
            ).show()
        }
    }

    private fun renderData(appState: AppState) {
        when (appState) {
            is AppState.Success -> {
                val movieData = appState.movieData
                binding.loadingLayout.visibility = View.GONE
                Snackbar.make(binding.mainFragment, R.string.success, Snackbar.LENGTH_LONG).show()
                setData(movieData)
            }
            is AppState.Loading -> {
                binding.loadingLayout.visibility = View.VISIBLE
            }
            is AppState.Error -> {
                try {
                    throw Exception(appState.error)
                } catch (e: Throwable) {
                    binding.loadingLayout.visibility = View.GONE
                    Snackbar
                        .make(
                            binding.mainFragment,
                            getString(R.string.error),
                            Snackbar.LENGTH_INDEFINITE
                        )
                        .setAction(getString(R.string.reload)) {
                            viewModel.getMovieFromLocalSource()
                        }
                        .show()
                }
            }
        }
    }

    private fun setData(movieData: Movie) {
        binding.movieTitleData.text = movieData.title
        binding.movieGenreData.text = movieData.genre
        binding.movieReleaseYearData.text = movieData.releaseYear.toString()
        binding.movieUserScoreData.text = movieData.userScore.toString()
        binding.movieTable.visibility = View.VISIBLE
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}