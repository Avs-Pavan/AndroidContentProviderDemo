package com.kevin.androidcontentproviderdemo.recyclerview

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.kevin.androidcontentproviderdemo.Contact
import com.kevin.androidcontentproviderdemo.databinding.ContactRowBinding


class ContactAdapter(
    private val contactList: List<Contact>
) :
    RecyclerView.Adapter<ContactAdapter.ContactViewHolder>() {

    inner class ContactViewHolder(val binding: ContactRowBinding) : ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ContactViewHolder {
        val binding = ContactRowBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ContactViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return contactList.size
    }

    override fun onBindViewHolder(holder: ContactViewHolder, position: Int) {

        val contact = contactList[position]

        // Bind data with view
        holder.binding.name.text = contact.name
        holder.binding.numbers.text = contact.numbers.joinToString(", ")

    }

}