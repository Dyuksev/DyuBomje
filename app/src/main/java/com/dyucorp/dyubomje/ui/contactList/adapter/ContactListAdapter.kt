package com.dyucorp.dyubomje.ui.contactList.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.dyucorp.dyubomje.R
import com.dyucorp.dyubomje.repository.local.ContactModel

class ContactListAdapter : RecyclerView.Adapter<ContactListAdapter.ContactViewHolder>() {

    private var dataList = ArrayList<ContactModel>()
    private lateinit var listener: onClickListener

    fun setListener(listener: onClickListener) {
        this.listener = listener
    }

    fun setData(list: List<ContactModel>) {
        dataList.clear()
        dataList.addAll(list)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ContactViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_contact, parent, false)
        return ContactViewHolder(view)
    }

    override fun onBindViewHolder(holder: ContactViewHolder, position: Int) {
        holder.bind(dataList[position])
    }

    override fun getItemCount(): Int = dataList.size

    inner class ContactViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        private val nameTextView = itemView.findViewById<TextView>(R.id.tv_name)
        private val phoneNumberTextView = itemView.findViewById<TextView>(R.id.tv_number)
        private val phoneImageView = itemView.findViewById<ImageView>(R.id.iv_phone)

        fun bind(itemContact: ContactModel) {
            nameTextView.text = itemContact.userName
            phoneNumberTextView.text = itemContact.phoneNumber
            phoneImageView.setOnClickListener {
                listener.onClick(itemContact)
            }
        }
    }

    interface onClickListener {
        fun onClick(itemContact: ContactModel)
    }
}