package com.geekbrains.moviesearcher2.view.settings

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.geekbrains.moviesearcher2.databinding.FragmentSettingsBinding
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.geekbrains.moviesearcher2.utils.ALLOW_ADULT_CONTENT

class SettingsFragment : Fragment() {

    private var _binding: FragmentSettingsBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSettingsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        restoreAdultContentStatus()
        binding.adultContentSwitch.setOnCheckedChangeListener { _, isChecked ->
            saveAdultContentStatus(isChecked)
        }
    }

    private fun restoreAdultContentStatus() {
        activity?.let {
            binding.adultContentSwitch.isChecked =
                it.getPreferences(Context.MODE_PRIVATE).getBoolean(ALLOW_ADULT_CONTENT, false)
        }
    }

    private fun saveAdultContentStatus(status: Boolean) {
        activity?.let {
            with(it.getPreferences(Context.MODE_PRIVATE).edit()) {
                putBoolean(ALLOW_ADULT_CONTENT, status)
                apply()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        fun newInstance(bundle: Bundle) = SettingsFragment().apply {
            arguments = bundle
        }
    }
}