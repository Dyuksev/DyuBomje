package com.dyucorp.dyubomje.ui.contactList

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import com.dyucorp.dyubomje.base.BaseViewModel
import com.dyucorp.dyubomje.repository.local.ContactModel
import java.util.*
import kotlin.collections.ArrayList

class ContactListViewModel : BaseViewModel() {
    var contactList = ArrayList<ContactModel>()
    val search = object : MutableLiveData<String>() {
        override fun setValue(value: String?) {
            super.setValue(value)
        }
    }
    var contactListLD = Transformations.switchMap(search, ::getFilteredContacts)

    private fun getFilteredContacts(str: String?): MutableLiveData<ArrayList<ContactModel>> {
        Log.d("tag", "suka $str")
        val newFilteredList = ArrayList<ContactModel>()
        when {
            str.isNullOrBlank() -> {
                newFilteredList.addAll(contactList)
            }
            str.isNotEmpty() -> {
                newFilteredList.addAll(contactList.filter { it.userName?.toLowerCase(Locale.getDefault())?.startsWith(str.toLowerCase(Locale.getDefault())) ?: false })
            }

        }
        return MutableLiveData(newFilteredList)
    }
}