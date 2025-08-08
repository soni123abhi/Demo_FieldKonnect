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
import com.exp.clonefieldkonnect.model.LeadTaskModel

class LeadTaskListAdapter(var activity: Activity, var useractivitylist: ArrayList<LeadTaskModel.Data>, var onClickEmail1: OnEmailClick) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    class ItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.lead_task_listing_layout, parent, false)
        return StatementHandler(v)

    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {

        val statementHandler = holder as StatementHandler
        val item = useractivitylist[position]

        statementHandler.tvcompany_name.text = item.lead!!.companyName.toString()
        statementHandler.tvuser_name.text = item.contact!!.name.orEmpty()
        statementHandler.tvmobile.text = item.contact!!.phoneNumber.orEmpty()
        statementHandler.tvaddress.text = item.assignUser!!.name.orEmpty()
        statementHandler.tv_lead_status.text = item.status.orEmpty()
        statementHandler.tvuser_notee.text = item.description.toString()



        when (item.status) {
            "Pending" -> statementHandler.relll_statuss.setBackgroundColor(Color.parseColor("#FDA73E"))
            "Open" -> statementHandler.relll_statuss.setBackgroundColor(Color.parseColor("#1793D1"))
            "In Progress" -> statementHandler.relll_statuss.setBackgroundColor(Color.parseColor("#182D69"))
            "Completed" -> statementHandler.relll_statuss.setBackgroundColor(Color.parseColor("#47AA4E"))
            else -> statementHandler.relll_statuss.setBackgroundColor(Color.parseColor("#182D69"))
        }

        statementHandler.relll_statuss.setOnClickListener {
            onClickEmail1.onClicklead(item.id,item.status)
        }


    }




    private inner class StatementHandler internal constructor(itemView: View) :
        RecyclerView.ViewHolder(itemView) {

        var tvcompany_name: TextView = itemView.findViewById(R.id.tvcompany_name)
        var tvuser_name: TextView = itemView.findViewById(R.id.tvuser_name)
        var tvmobile: TextView = itemView.findViewById(R.id.tvmobile)
        var tvaddress: TextView = itemView.findViewById(R.id.tvaddress)
        var tvuser_notee: TextView = itemView.findViewById(R.id.tvuser_notee)
        var tv_lead_status: TextView = itemView.findViewById(R.id.tv_lead_status)
        var relll_statuss: RelativeLayout = itemView.findViewById(R.id.relll_statuss)

    }

    override fun getItemCount(): Int {
        return useractivitylist.size
    }


    interface OnEmailClick {
        fun onClicklead(id: Int?, status: String?)
    }
}