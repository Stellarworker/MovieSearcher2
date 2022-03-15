package com.geekbrains.moviesearcher2

import android.view.View
import com.google.android.material.snackbar.Snackbar

fun View.makeSnackbar(
    text: String = "",
    actionText: String = "",
    action: (View) -> Unit = {},
    length: Int = Snackbar.LENGTH_LONG
) = also {
    Snackbar.make(this, text, length).setAction(actionText, action).show()
}

fun View.show() = apply {
    visibility = View.VISIBLE
}

fun View.hide() = apply {
    visibility = View.GONE
}
