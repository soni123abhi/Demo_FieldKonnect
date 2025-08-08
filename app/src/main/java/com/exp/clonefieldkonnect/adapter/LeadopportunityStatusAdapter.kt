package com.exp.clonefieldkonnect.adapter


import android.app.Activity
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.exp.clonefieldkonnect.R
import com.exp.clonefieldkonnect.model.OportunityDetailModel

class LeadopportunityStatusAdapter(var activity: Activity, var useractivitylist: ArrayList<OportunityDetailModel.Counter>, var onClickEmail1: OnEmailClick) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    class ItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.lead_status_layout, parent, false)
        return StatementHandler(v)

    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {

        val statementHandler = holder as StatementHandler
        val item = useractivitylist[position]

        statementHandler.status_count.text = item.totalOpportunities?.toInt().toString()
        statementHandler.status_type.text = item.statusName.toString()

        // Set background color based on ID
        when (item.statusId) {
            -1 -> statementHandler.relll_main.setBackgroundColor(Color.parseColor("#182D69"))
//            8 -> statementHandler.relll_main.setBackgroundColor(Color.parseColor("#47AA4E"))
            4 -> statementHandler.relll_main.setBackgroundColor(Color.parseColor("#FC4F38"))
            3 -> statementHandler.relll_main.setBackgroundColor(Color.parseColor("#FDA73E"))
            8 -> statementHandler.relll_main.setBackgroundColor(Color.parseColor("#1793D1"))
            else -> statementHandler.relll_main.setBackgroundColor(Color.parseColor("#182D69"))
        }

        statementHandler.relll_main.setOnClickListener {
            onClickEmail1.onClickEmail1(item.statusId)
        }

    }

    private inner class StatementHandler internal constructor(itemView: View) :
        RecyclerView.ViewHolder(itemView) {

        var status_count: TextView = itemView.findViewById(R.id.status_count)
        var status_type: TextView = itemView.findViewById(R.id.status_type)
        var relll_main: RelativeLayout = itemView.findViewById(R.id.relll_main)
    }

    override fun getItemCount(): Int {
        return useractivitylist.size
    }


    interface OnEmailClick {
        fun onClickEmail1(id: Int?)
    }
}