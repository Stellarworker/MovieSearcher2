package com.geekbrains.moviesearcher2.view.contacts

import android.Manifest
import android.content.ContentResolver
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.database.Cursor
import android.net.Uri
import android.os.Bundle
import android.provider.ContactsContract
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.AppCompatTextView
import androidx.core.content.ContextCompat
import com.geekbrains.moviesearcher2.R
import com.geekbrains.moviesearcher2.databinding.FragmentContactsBinding

const val READ_CONTACTS_REQUEST_CODE = 11
private const val SORT_ORDER_TAG = " ASC"

class ContactsFragment : Fragment() {

    private var _binding: FragmentContactsBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentContactsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        checkReadContactsPermission()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        const val FRAGMENT_TAG = "CONTACTS_FRAGMENT"

        @JvmStatic
        fun newInstance() = ContactsFragment()
    }

    private fun addView(context: Context, textToShow: String) {
        binding.contactsContainer.addView(AppCompatTextView(context).apply {
            text = textToShow
            textSize = resources.getDimension(R.dimen.contactsTextSize)
        })
    }

    private fun checkReadContactsPermission() {
        context?.let {
            when {
                ContextCompat.checkSelfPermission(it, Manifest.permission.READ_CONTACTS) ==
                        PackageManager.PERMISSION_GRANTED -> {
                    getContacts()
                }
                shouldShowRequestPermissionRationale(Manifest.permission.READ_CONTACTS) -> {
                    AlertDialog.Builder(it)
                        .setTitle(getString(R.string.contactsDialogTitle))
                        .setMessage(getString(R.string.contactsDialogMessage))
                        .setPositiveButton(getString(R.string.contactsDialogAccept)) { _, _ ->
                            requestReadContactsPermission()
                        }
                        .setNegativeButton(getString(R.string.contactsDialogDeny)) { dialog, _ -> dialog.dismiss() }
                        .create()
                        .show()
                }
                else -> {
                    requestReadContactsPermission()
                }
            }
        }
    }

    private fun requestReadContactsPermission() {
        requestPermissions(arrayOf(Manifest.permission.READ_CONTACTS), READ_CONTACTS_REQUEST_CODE)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        when (requestCode) {
            READ_CONTACTS_REQUEST_CODE -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    getContacts()
                } else {
                    context?.let {
                        AlertDialog.Builder(it)
                            .setTitle(getString(R.string.contactsDialogTitle))
                            .setMessage(getString(R.string.contactsDialogMessage))
                            .setNegativeButton(getString(R.string.contactsDialogClose)) { dialog, _ -> dialog.dismiss() }
                            .create()
                            .show()
                    }
                }
            }
        }
    }

    private fun getContacts() {
        context?.let {
            val contentResolver: ContentResolver = it.contentResolver
            val cursorWithContacts: Cursor? = contentResolver.query(
                ContactsContract.Contacts.CONTENT_URI,
                null,
                null,
                null,
                ContactsContract.Contacts.DISPLAY_NAME + SORT_ORDER_TAG
            )
            cursorWithContacts?.let { cursor ->
                for (i in 0..cursor.count) {
                    val namePos = cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME)
                    if (cursor.moveToPosition(i)) {
                        val name = cursor.getString(namePos)
                        addView(it, name)
                    }
                }
            }
            cursorWithContacts?.close()
        }
    }
}