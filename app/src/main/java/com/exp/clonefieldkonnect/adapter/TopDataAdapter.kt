package com.exp.clonefieldkonnect.adapter


import android.app.Activity
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.recyclerview.widget.RecyclerView
import com.exp.clonefieldkonnect.R
import com.exp.clonefieldkonnect.model.TopDataModel

class TopDataAdapter(var activity: Activity, var topDataArr : ArrayList<TopDataModel>) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    lateinit var mcontext: Context


    class ItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {

        mcontext = parent.context

        val v = LayoutInflater.from(mcontext).inflate(R.layout.adapter_top_data, parent, false)
        return StatementHandler(v)

    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {

            val statementHandler = holder as StatementHandler


                statementHandler.tvTitle.text = topDataArr[position].title
                statementHandler.tvValue.text = topDataArr[position].value


    }


    private inner class StatementHandler internal constructor(itemView: View) : RecyclerView.ViewHolder(itemView) {

        var tvTitle: TextView = itemView.findViewById(R.id.tvTitle)
        var tvValue: TextView = itemView.findViewById(R.id.tvValue)

    }

    override fun getItemCount(): Int {
        return topDataArr.size
    }


}