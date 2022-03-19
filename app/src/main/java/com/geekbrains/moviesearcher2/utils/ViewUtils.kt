package com.geekbrains.moviesearcher2.utils

import android.view.View
import com.google.android.material.snackbar.Snackbar

fun View.makeSnackbar(
    text: String = "",
    actionText: String = "",
    action: (View) -> Unit = {},
    length: Int = Snackbar.LENGTH_LONG
) {
    Snackbar.make(this, text, length).setAction(actionText, action).show()
}

fun View.show() {
    visibility = View.VISIBLE
}

fun View.hide() {
    visibility = View.GONE
}
