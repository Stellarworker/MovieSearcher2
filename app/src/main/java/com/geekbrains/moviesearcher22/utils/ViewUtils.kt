package com.geekbrains.moviesearcher22.utils

import android.view.View
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.geekbrains.moviesearcher22.common.EMPTY_STRING
import com.google.android.material.snackbar.Snackbar

fun View.makeSnackbar(
    text: String = EMPTY_STRING,
    actionText: String = EMPTY_STRING,
    action: (View) -> Unit = {},
    length: Int = Snackbar.LENGTH_LONG,
    anchor: View? = null
) {
    Snackbar
        .make(this, text, length)
        .setAction(actionText, action)
        .setAnchorView(anchor)
        .show()
}

fun View.show() {
    visibility = View.VISIBLE
}

fun View.hide() {
    visibility = View.GONE
}

fun View.hideKeyboard() =
    ViewCompat.getWindowInsetsController(this)?.hide(WindowInsetsCompat.Type.ime())


