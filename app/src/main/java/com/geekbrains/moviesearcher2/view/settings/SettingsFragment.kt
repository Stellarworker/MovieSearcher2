package com.geekbrains.moviesearcher2.view.settings

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.geekbrains.moviesearcher2.databinding.FragmentSettingsBinding
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.geekbrains.moviesearcher2.common.SHOW_ADULT_CONTENT

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
        binding.sfAdultContentSwitch.setOnCheckedChangeListener { _, isChecked ->
            saveAdultContentStatus(isChecked)
        }
    }

    private fun restoreAdultContentStatus() {
        activity?.let {
            binding.sfAdultContentSwitch.isChecked =
                it.getPreferences(Context.MODE_PRIVATE).getBoolean(SHOW_ADULT_CONTENT, false)
        }
    }

    private fun saveAdultContentStatus(status: Boolean) {
        activity?.let {
            with(it.getPreferences(Context.MODE_PRIVATE).edit()) {
                putBoolean(SHOW_ADULT_CONTENT, status)
                apply()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        const val FRAGMENT_TAG = "SETTINGS_FRAGMENT"

        @JvmStatic
        fun newInstance() = SettingsFragment()
    }
}