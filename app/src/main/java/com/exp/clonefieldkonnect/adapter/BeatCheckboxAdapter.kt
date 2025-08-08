package com.exp.clonefieldkonnect.adapter


import android.app.Activity
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.recyclerview.widget.RecyclerView
import com.exp.clonefieldkonnect.R
import com.exp.clonefieldkonnect.model.BeatModel

class BeatCheckboxAdapter(var activity: Activity, var beatModel: ArrayList<BeatModel>) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    lateinit var mcontext: Context

    class ItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {

        mcontext = parent.context

        val v = LayoutInflater.from(mcontext).inflate(R.layout.adapter_beat_checkbox, parent, false)
        return StatementHandler(v)

    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {

        val statementHandler = holder as StatementHandler
        statementHandler.checkbox.setText(beatModel.get(position).beatName)

        statementHandler.checkbox.isChecked = beatModel.get(position).isChecked

        statementHandler.checkbox.setOnCheckedChangeListener { compoundButton, b ->
            beatModel.get(holder.adapterPosition).isChecked = b
            notifyDataSetChanged()
        }

    }


    private inner class StatementHandler internal constructor(itemView: View) :
        RecyclerView.ViewHolder(itemView) {

        var checkbox: CheckBox = itemView.findViewById(R.id.checkbox)

    }

    override fun getItemCount(): Int {
        return beatModel.size
    }


}