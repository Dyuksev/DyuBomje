package com.dyucorp.dyubomje.ui.start

import android.Manifest
import android.content.ContentResolver
import android.content.pm.PackageManager
import android.database.Cursor
import android.os.Build
import android.os.Bundle
import android.provider.ContactsContract
import android.view.View
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.navigation.fragment.findNavController
import com.dyucorp.dyubomje.R
import com.dyucorp.dyubomje.base.BaseFragment
import com.dyucorp.dyubomje.databinding.FragmentStartBinding
import com.dyucorp.dyubomje.repository.local.ContactModel


class StartFragment : BaseFragment<StartViewModel, FragmentStartBinding>() {

    override fun viewModelClass(): Class<StartViewModel> = StartViewModel::class.java

    override fun layoutResId(): Int = R.layout.fragment_start

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.isClicked.observe(viewLifecycleOwner, {
            if (it == true) {
                checkReadContactsPermission()
            }
        })

    }

    private fun checkReadContactsPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && ContextCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.READ_CONTACTS
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            requestPermissions(
                arrayOf(Manifest.permission.READ_CONTACTS, Manifest.permission.CALL_PHONE ),
                PERMISSIONS_REQUEST_READ_CONTACTS
            )
            //callback onRequestPermissionsResult
        } else {
            onContactsReceived(loadAllContacts())
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int, permissions: Array<out String>,
        grantResults: IntArray
    ) {
        if (requestCode == PERMISSIONS_REQUEST_READ_CONTACTS) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                onContactsReceived(loadAllContacts())
            } else {
                (Toast.makeText(
                    requireContext(),
                    "Permission must be granted in order to display contacts information",
                    Toast.LENGTH_SHORT
                )).show()
            }
        }
    }

    private fun onContactsReceived(contactsList: List<ContactModel>) {
        findNavController().navigate(
            StartFragmentDirections
                .actionStartFragmentToContactListFragment(
                    contactsList.toTypedArray()
                )
        )
    }

    private fun loadAllContacts(): List<ContactModel> {
        val contactList = ArrayList<ContactModel>()
        val cr = activity?.contentResolver
        cr?.let { contentResolver ->
            val cur = contentResolver.query(
                ContactsContract.Contacts.CONTENT_URI,
                null, null, null, ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME + " ASC"
            )
            cur?.let { cursor ->
                if (cursor.count > 0) {
                    while (cursor.moveToNext()) {
                        val model = handleContactsCursor(cursor, contentResolver)
                        model?.let {
                            contactList.add(model)
                        }
                    }
                } else {
                    //todo
                    (Toast.makeText(
                        requireContext(),
                        "You have no contacts. Sorry, comrade!",
                        Toast.LENGTH_SHORT
                    )).show()
                }
            }
            cur?.close()
        }
        return contactList
    }

    private fun handleContactsCursor(
        cursor: Cursor,
        contentResolver: ContentResolver
    ): ContactModel? {

        val id = cursor.getString(
            cursor.getColumnIndex(ContactsContract.Contacts._ID)
        )

        val name = cursor.getString(
            cursor.getColumnIndex(
                ContactsContract.Contacts.DISPLAY_NAME
            )
        )
        if (cursor.getInt(cursor.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER)) > 0) {
            val phoneNumberCur = contentResolver.query(
                ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                null,
                ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ?",
                arrayOf(id),
                null
            )
            phoneNumberCur?.let {
                while (it.moveToNext()) {
                    val phoneNumber: String = it.getString(
                        it.getColumnIndex(
                            ContactsContract.CommonDataKinds.Phone.NUMBER
                        )
                    )

                    return ContactModel(
                        id = id,
                        userName = name,
                        phoneNumber = phoneNumber
                    )
                }
            }
            phoneNumberCur?.close()
        } else {
            return ContactModel(
                id = id,
                userName = name,
                phoneNumber = null
            )
        }
        return null
    }

    companion object {
        const val PERMISSIONS_REQUEST_READ_CONTACTS = 100
    }
}