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
import com.geekbrains.moviesearcher2.view.details.DetailsFragment
import com.geekbrains.moviesearcher2.viewmodel.AppState
import com.geekbrains.moviesearcher2.viewmodel.MainViewModel
import com.google.android.material.snackbar.Snackbar

class MainFragment : Fragment() {

    private var _binding: MainFragmentBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModel: MainViewModel
    private var query: String = ""

    companion object {
        fun newInstance() = MainFragment()
    }

    private val adapter = MainFragmentAdapter(object : MainFragmentAdapter.OnItemViewClickListener {
        override fun onItemViewClick(movie: Movie) {
            val manager = activity?.supportFragmentManager
            if (manager != null) {
                val bundle = Bundle()
                bundle.putParcelable(DetailsFragment.BUNDLE_EXTRA, movie)
                manager.beginTransaction()
                    .replace(R.id.container, DetailsFragment.newInstance(bundle))
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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.mainFragmentRecyclerView.adapter = adapter
        viewModel = ViewModelProvider(this).get(MainViewModel::class.java)
        val observer = Observer<AppState> {
            renderData(it)
        }
        viewModel.getLiveData().observe(viewLifecycleOwner, observer)
        binding.searchButton.setOnClickListener {
            query = binding.searchLine.text.toString().trim()
            startSearching(query)
            binding.searchLine.setText("")  // Очистка поисковой строки.
        }
    }

    // Метод обрабатывает поисковый запрос пользователя.
    private fun startSearching(searchText: String) {
        if (searchText != "") {
            viewModel.getMovies(searchText)
        } else {
            Snackbar.make(
                binding.mainFragment, getString(R.string.emptyRequestErrorLabelText),
                Snackbar.LENGTH_LONG
            ).show()
        }
    }

    private fun renderData(appState: AppState) {
        when (appState) {
            is AppState.Success -> {
                adapter.setMovie(appState.movieData)
                binding.loadingLayout.visibility = View.GONE
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
                            getString(R.string.errorLabelText),
                            Snackbar.LENGTH_INDEFINITE
                        )
                        .setAction(getString(R.string.reloadLabelText)) {
                            viewModel.getMovies(query)
                        }
                        .show()
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}