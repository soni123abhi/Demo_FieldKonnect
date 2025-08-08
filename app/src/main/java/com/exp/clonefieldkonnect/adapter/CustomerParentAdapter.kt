package com.exp.clonefieldkonnect.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.exp.clonefieldkonnect.R
import com.exp.clonefieldkonnect.model.CustomerParentModel

class CustomerParentAdapter(
    private var list: List<CustomerParentModel.Data>,
    private val onItemSelected: (List<CustomerParentModel.Data>) -> Unit
) : RecyclerView.Adapter<CustomerParentAdapter.ViewHolder>() {

    private val selectedItems = mutableListOf<CustomerParentModel.Data>()

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val textView: TextView = view.findViewById(R.id.text_view_item)
        val checkBox: CheckBox = view.findViewById(R.id.check_box_item)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_dropdown, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = list[position]
        holder.textView.text = item.name
        holder.checkBox.setOnCheckedChangeListener(null)
        holder.checkBox.isChecked = selectedItems.contains(item)

        holder.checkBox.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                if (!selectedItems.contains(item)) {
                    selectedItems.add(item)
                }
            } else {
                selectedItems.remove(item)
            }
            onItemSelected(selectedItems)
        }


        holder.itemView.setOnClickListener {
            holder.checkBox.isChecked = !holder.checkBox.isChecked
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }

    fun updateList(newList: List<CustomerParentModel.Data>) {
        list = newList
        notifyDataSetChanged()
    }
    fun getSelectedItems(): List<CustomerParentModel.Data> {
        return selectedItems
    }
}
