package com.exp.clonefieldkonnect.adapter

import android.app.Dialog
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.exp.clonefieldkonnect.R
import com.exp.clonefieldkonnect.model.DistriutorModel

class CustomerretailerAdapter2(
    private var list: List<DistriutorModel.Datum>,
    var currentDialog: Dialog,
    private val onItemClicked: (DistriutorModel.Datum) -> Unit // Callback for single item click
) : RecyclerView.Adapter<CustomerretailerAdapter2.ViewHolder>() {

    // Set to store selected items
    private val selectedItems = mutableSetOf<DistriutorModel.Datum>()

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val checkbox: CheckBox = view.findViewById(R.id.checkbox)
        val textView: TextView = view.findViewById(R.id.retailer_name)

        fun bind(item: DistriutorModel.Datum) {
            textView.text = item.name
            checkbox.isChecked = selectedItems.contains(item)

            // Handle checkbox state changes
            checkbox.setOnCheckedChangeListener { _, isChecked ->
                if (isChecked) {
                    selectedItems.add(item)
                } else {
                    selectedItems.remove(item)
                }
            }

            // Handle item click (optional: toggle checkbox on item click)
            itemView.setOnClickListener {
                checkbox.isChecked = !checkbox.isChecked
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_dropdown_retailer2, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = list[position]
        holder.bind(item)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    // Function to update the list
    fun updateList(newList: List<DistriutorModel.Datum>) {
        list = newList
        notifyDataSetChanged()
    }

    // Function to get selected items
    fun getSelectedItems(): Set<DistriutorModel.Datum> {
        return selectedItems
    }
}