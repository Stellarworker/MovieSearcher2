package com.geekbrains.moviesearcher2.view

import android.content.Intent
import android.content.IntentFilter
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.annotation.RequiresApi
import com.geekbrains.moviesearcher2.R
import com.geekbrains.moviesearcher2.databinding.MainActivityBinding
import com.geekbrains.moviesearcher2.utils.loadFragment
import com.geekbrains.moviesearcher2.view.contacts.ContactsFragment
import com.geekbrains.moviesearcher2.view.history.HistoryFragment
import com.geekbrains.moviesearcher2.view.settings.SettingsFragment
import okhttp3.internal.notifyAll

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
        binding.bottomNavigation.setOnNavigationItemSelectedListener { item ->

            when (item.itemId) {
                R.id.itemMenuSettings -> loadFragment(
                    SettingsFragment.newInstance(),
                    SettingsFragment.FRAGMENT_TAG,
                    supportFragmentManager
                )
                R.id.itemMenuHistory -> loadFragment(
                    HistoryFragment.newInstance(),
                    HistoryFragment.FRAGMENT_TAG,
                    supportFragmentManager
                )
                R.id.itemMenuContacts -> loadFragment(
                    ContactsFragment.newInstance(),
                    ContactsFragment.FRAGMENT_TAG,
                    supportFragmentManager
                )
                R.id.itemMenuHome ->
                    loadFragment(
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
        if (supportFragmentManager.backStackEntryCount == 1) {
            finish();
        } else {
            super.onBackPressed();
        }
    }
}