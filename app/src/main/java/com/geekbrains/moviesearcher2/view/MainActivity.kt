package com.geekbrains.moviesearcher2.view

import android.content.Intent
import android.content.IntentFilter
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.annotation.RequiresApi
import com.geekbrains.moviesearcher2.R
import com.geekbrains.moviesearcher2.databinding.MainActivityBinding
import com.geekbrains.moviesearcher2.utils.loadFragment
import com.geekbrains.moviesearcher2.view.history.HistoryFragment
import com.geekbrains.moviesearcher2.view.maps.MapsFragment
import com.geekbrains.moviesearcher2.view.settings.SettingsFragment

class MainActivity : AppCompatActivity() {

    private lateinit var binding: MainActivityBinding
    private val receiver = MainBroadcastReceiver()

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = MainActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)
        savedInstanceState?.let {
            // TODO
        } ?: run {
            loadFragment(
                MainFragment.newInstance(),
                MainFragment.FRAGMENT_TAG,
                supportFragmentManager
            )
        }
        registerReceiver(receiver, IntentFilter(Intent.ACTION_POWER_CONNECTED))
        binding.maBottomNavigation.setOnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.bottomMenuItemSettings -> loadFragment(
                    SettingsFragment.newInstance(),
                    SettingsFragment.FRAGMENT_TAG,
                    supportFragmentManager
                )
                R.id.bottomMenuItemHistory -> loadFragment(
                    HistoryFragment.newInstance(),
                    HistoryFragment.FRAGMENT_TAG,
                    supportFragmentManager
                )
                R.id.bottomMenuItemMaps -> loadFragment(
                    MapsFragment.newInstance(),
                    MapsFragment.FRAGMENT_TAG,
                    supportFragmentManager
                )
                R.id.bottomMenuItemHome -> loadFragment(
                    MainFragment.newInstance(),
                    MainFragment.FRAGMENT_TAG,
                    supportFragmentManager
                )
                else -> false
            }
        }
    }

    override fun onDestroy() {
        unregisterReceiver(receiver)
        super.onDestroy()
    }

    override fun onBackPressed() {
        when (supportFragmentManager.backStackEntryCount) {
            1 -> finish()
            else -> super.onBackPressed()
        }
    }
}