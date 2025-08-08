package com.exp.clonefieldkonnect.adapter

import android.app.Dialog
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.exp.clonefieldkonnect.R
import com.exp.clonefieldkonnect.model.DistriutorModel

class CustomerDistributorAdapter(
    private var list: List<DistriutorModel.Datum>,
    var currentDialog: Dialog,
    private val onItemClicked: (DistriutorModel.Datum) -> Unit // Callback function
) : RecyclerView.Adapter<CustomerDistributorAdapter.ViewHolder>() {

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val textView: TextView = view.findViewById(R.id.text_view_item)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_dropdown_retailer, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = list[position]
        holder.textView.text = item.name

        holder.textView.setOnClickListener {
            onItemClicked(item) // Trigger the callback with the clicked item
            currentDialog.dismiss()
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }
    fun updateList(newList: List<DistriutorModel.Datum>) {
        list = newList
        notifyDataSetChanged()
    }
}

