package com.dyucorp.dyubomje.ui.contactList

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.dyucorp.dyubomje.R
import com.dyucorp.dyubomje.base.BaseFragment
import com.dyucorp.dyubomje.databinding.FragmentContactListBinding
import com.dyucorp.dyubomje.repository.local.ContactModel
import com.dyucorp.dyubomje.ui.contactList.ContactListFragmentArgs.Companion.fromBundle
import com.dyucorp.dyubomje.ui.contactList.adapter.ContactListAdapter


class ContactListFragment : BaseFragment<ContactListViewModel, FragmentContactListBinding>() {

    private val args by lazy {
        fromBundle(requireArguments())
    }
    private val contactsClickListener = object : ContactListAdapter.onClickListener {
        override fun onClick(itemContact: ContactModel) {
            val callIntent = Intent(Intent.ACTION_CALL)
            val char = '#'.toInt().toChar()
            callIntent.data =
                Uri.parse("tel: *104*${itemContact.phoneNumber}" + Uri.encode("#"))
            startActivity(callIntent)
        }
    }

    private val contactListAdapter = ContactListAdapter()

    override fun viewModelClass(): Class<ContactListViewModel> = ContactListViewModel::class.java

    override fun layoutResId(): Int = R.layout.fragment_contact_list

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.contactList = ArrayList(args.contacts.toList())
        contactListAdapter.setListener(contactsClickListener)
        viewModel.search.value = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.contactListLD.observe(viewLifecycleOwner, Observer {
                contactListAdapter.setData(it)
        })

        initRecycler()
    }

    private fun initRecycler() {
        binding.rvContactList.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = contactListAdapter
        }
    }
}