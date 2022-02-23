package com.geekbrains.moviesearcher2.view

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
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
    private val viewModel: MainViewModel by lazy {
        ViewModelProvider(this).get(MainViewModel::class.java)
    }
    private var query: String = ""

    companion object {
        fun newInstance() = MainFragment()
    }

    private val adapter = MainFragmentAdapter(object : MainFragmentAdapter.OnItemViewClickListener {
        override fun onItemViewClick(movie: Movie) {
            activity?.supportFragmentManager?.apply {
                beginTransaction()
                    .replace(R.id.container, DetailsFragment.newInstance(Bundle().apply {
                        putParcelable(DetailsFragment.BUNDLE_EXTRA, movie)
                    }))
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
        viewModel.getLiveData().observe(viewLifecycleOwner) {
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
    private fun startSearching(searchText: String) {
        when (searchText) {
            "" -> binding.root.makeSnackbar(text = getString(R.string.emptyRequestLabelText))
            else -> viewModel.getMovies(searchText)
        }
    }

    private fun renderData(appState: AppState) {
        when (appState) {
            is AppState.Success -> {
                binding.loadingLayout.hide()
                when (appState.movieData.size) {
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
                            viewModel.getMovies(query)
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