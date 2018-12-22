package com.contactreader.ca.adapter

import android.content.Context
import android.support.v4.content.ContextCompat
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.contactreader.ca.R
import com.contactreader.ca.dto.ContactDTO
import kotlinx.android.synthetic.main.item_list_contacts.view.*

class ContactAdapter(items : List<ContactDTO>, mContext: Context) : RecyclerView.Adapter<ContactAdapter.ViewHolder>(){

    private var list = items
    private var context = mContext

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: ContactAdapter.ViewHolder, position: Int) {
        holder.name.text = list[position].name
        holder.number.text = list[position].number

        if(list[position].image != null)
            holder.profile.setImageBitmap(list[position].image)
        else
            holder.profile.setImageDrawable(ContextCompat.getDrawable(
                context, R.drawable.ic_contact_default))
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ContactAdapter.ViewHolder {
        return ViewHolder(LayoutInflater.from(context).inflate(
            R.layout.item_list_contacts,parent,false))
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view){
        val name = view.tvName!!
        val number = view.tvNumber!!
        val profile = view.ivProfile!!
    }
}