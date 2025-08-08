package com.exp.clonefieldkonnect.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView

class PercentageAdapter(context: Context, resource: Int, private val percentages: Array<String>) :
    ArrayAdapter<String>(context, resource, percentages) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view: View = convertView ?: LayoutInflater.from(context)
            .inflate(android.R.layout.simple_list_item_1, parent, false)
        val textView: TextView = view.findViewById(android.R.id.text1)
        textView.text = percentages[position]
        return view
    }
}