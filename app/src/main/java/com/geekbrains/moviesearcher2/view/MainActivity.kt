package com.geekbrains.moviesearcher2.view

import android.content.Intent
import android.content.IntentFilter
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import com.geekbrains.moviesearcher2.R
import com.geekbrains.moviesearcher2.databinding.MainActivityBinding
import com.geekbrains.moviesearcher2.view.history.HistoryFragment
import com.geekbrains.moviesearcher2.view.settings.SettingsFragment

private const val HISTORY_FRAGMENT_STRING = "HISTORY_FRAGMENT_STRING"
private const val SETTINGS_FRAGMENT_STRING = "SETTINGS_FRAGMENT_STRING"

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
            supportFragmentManager.beginTransaction()
                .replace(R.id.container, MainFragment.newInstance())
                .commitNow()
        }
        registerReceiver(receiver, IntentFilter(Intent.ACTION_POWER_CONNECTED))
    }

    override fun onDestroy() {
        unregisterReceiver(receiver)
        super.onDestroy()
    }

    override fun onCreateOptionsMenu(menu: Menu?) = run {
        menuInflater.inflate(R.menu.main_screen_menu, menu)
        super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem) =
        when (item.itemId) {
            R.id.mainMenuHistory -> loadFragment(
                HistoryFragment.newInstance(),
                HISTORY_FRAGMENT_STRING
            )
            R.id.mainMenuSettings -> loadFragment(
                SettingsFragment.newInstance(Bundle()),
                SETTINGS_FRAGMENT_STRING
            )
            else -> super.onOptionsItemSelected(item)
        }


    private fun loadFragment(fragment: Fragment, fragmentString: String) = run {
        supportFragmentManager.apply {
            beginTransaction()
                .replace(R.id.container, fragment)
                .addToBackStack(fragmentString)
                .commitAllowingStateLoss()
        }
        true
    }
}