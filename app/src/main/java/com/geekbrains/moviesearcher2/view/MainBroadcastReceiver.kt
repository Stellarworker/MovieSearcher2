package com.geekbrains.moviesearcher2.view

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.res.Resources
import android.widget.Toast
import com.geekbrains.moviesearcher2.R

class MainBroadcastReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        StringBuilder().apply {
            append(context.resources.getString(R.string.mainBRSystemMessageText))
            append(
                String.format(
                    context.resources.getString(R.string.mainBRActionText),
                    intent.action
                )
            )
            toString().also {
                Toast.makeText(context, it, Toast.LENGTH_LONG).show()
            }
        }
    }
}