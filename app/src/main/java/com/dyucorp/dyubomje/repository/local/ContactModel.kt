package com.dyucorp.dyubomje.repository.local

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class ContactModel(
    var id: String? = null,
    var userName: String? = null,
    var phoneNumber: String? = null
) : Parcelable